package MazeGame;

import MazeGame.item.Armor;
import MazeGame.item.Item;
import MazeGame.item.Weapon;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;


public class InventoryPanel extends JPanel {

    private static final int COLS = 5;
    private static final int ROWS = 4;
    private static final int SLOT_SIZE = 64;

    private final Inventory inventory;

    public InventoryPanel(Player player) {
        this.inventory = player.getInventory();

        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));

        add(createGridPanel(), BorderLayout.CENTER);
        add(createEquipmentPanel(), BorderLayout.EAST);
    }

    private JPanel createGridPanel() {
        JPanel grid = new JPanel(new GridLayout(ROWS, COLS, 8, 8));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        grid.setBackground(new Color(30, 30, 30));

        List<Item> items = inventory.getItems();

        System.out.println("InventoryPanel: в инвентаре предметов: " + items.size());

        for (int i = 0; i < ROWS * COLS; i++) {
            if (i < items.size()) {
                grid.add(createItemButton(items.get(i), i));
            } else {
                grid.add(createEmptySlot());
            }
        }

        return grid;
    }

    private JButton createItemButton(Item item, int index) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
        button.setBackground(new Color(50, 50, 50));
        button.setFocusPainted(false);

        // Безопасная загрузка иконки
        ImageIcon icon = safeLoadIcon(item);
        button.setIcon(icon);

        // Подсказка
        button.setToolTipText(buildTooltip(item));

        // Подсветка если надет
        if (isEquipped(item)) {
            button.setBorder(new LineBorder(Color.GREEN, 2));
        } else {
            button.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        }

        // Клик → экипировка
        button.addActionListener(e -> {
            inventory.equipItem(index);
            refresh();
        });

        return button;
    }

    /**
     * Безопасно загружает иконку предмета
     * Если путь null или картинка не найдена → возвращает плейсхолдер
     */
    private ImageIcon safeLoadIcon(Item item) {
        String path = item.getIconPath();

        if (path == null || path.isEmpty()) {
            System.err.println("У предмета нет иконки: " + item.getName());
            return createPlaceholderIcon();
        }

        var url = getClass().getResource(path);
        if (url == null) {
            System.err.println("Иконка не найдена по пути: " + path + " (предмет: " + item.getName() + ")");
            return createPlaceholderIcon();
        }

        ImageIcon original = new ImageIcon(url);
        Image scaled = original.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /**
     * Плейсхолдер для предметов без иконки
     */
    private ImageIcon createPlaceholderIcon() {
        JLabel placeholder = new JLabel("?");
        placeholder.setForeground(Color.LIGHT_GRAY);
        placeholder.setHorizontalAlignment(SwingConstants.CENTER);
        placeholder.setFont(new Font("Arial", Font.BOLD, 24));

        BufferedImage img = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(60, 60, 60));
        g2.fillRect(0, 0, 48, 48);
        placeholder.setSize(48, 48);
        placeholder.paint(g2);
        g2.dispose();

        return new ImageIcon(img);
    }

    private JButton createEmptySlot() {
        JButton empty = new JButton();
        empty.setPreferredSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
        empty.setEnabled(false);
        empty.setBackground(new Color(40, 40, 40));
        empty.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        return empty;
    }

    private JPanel createEquipmentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(25, 25, 25));

        panel.add(createEquipmentSlot("Оружие", inventory.getEquippedWeapon(), "weapon"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createEquipmentSlot("Верх", inventory.getEquippedTop(), "верх"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createEquipmentSlot("Низ", inventory.getEquippedBottom(), "низ"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createEquipmentSlot("Щит", inventory.getEquippedShield(), "shield"));

        return panel;
    }

    private JPanel createEquipmentSlot(String title, Item item, String type) {
        JPanel slot = new JPanel(new BorderLayout());
        slot.setPreferredSize(new Dimension(140, 80));
        slot.setBackground(new Color(35, 35, 35));
        slot.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.GRAY),
                title,
                0, 0,
                new Font("Arial", Font.BOLD, 12),
                Color.WHITE
        ));

        JButton button = new JButton();
        button.setFocusPainted(false);
        button.setBackground(new Color(50, 50, 50));

        if (item != null) {
            ImageIcon icon = safeLoadIcon(item);  // используем ту же безопасную функцию
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
            button.setToolTipText(buildTooltip(item));

            button.addActionListener(e -> {
                inventory.unequipItem(type);
                refresh();
            });
        } else {
            button.setEnabled(false);
        }

        slot.add(button, BorderLayout.CENTER);
        return slot;
    }

    private boolean isEquipped(Item item) {
        return item == inventory.getEquippedWeapon() ||
                item == inventory.getEquippedTop() ||
                item == inventory.getEquippedBottom() ||
                item == inventory.getEquippedShield();
    }

    private String buildTooltip(Item item) {
        StringBuilder sb = new StringBuilder("<html>");
        sb.append("<b>").append(item.getName()).append("</b><br>");
        sb.append("Прочность: ").append(item.getCurrentStrength()).append("/").append(item.getStrength()).append("<br>");

        if (item instanceof Weapon w) sb.append("Атака: +").append(w.getAttack());
        if (item instanceof Armor a) sb.append("Защита: +").append(a.getProtection());

        sb.append("</html>");
        return sb.toString();
    }

    public void refresh() {
        removeAll();
        add(createGridPanel(), BorderLayout.CENTER);
        add(createEquipmentPanel(), BorderLayout.EAST);
        revalidate();
        repaint();
    }
}