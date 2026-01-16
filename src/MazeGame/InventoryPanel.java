package MazeGame;

import MazeGame.item.Armor;
import MazeGame.item.Item;
import MazeGame.item.Weapon;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.util.Objects;
//+
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

    // ================= СЕТКА =================

    private JPanel createGridPanel() {
        JPanel grid = new JPanel(new GridLayout(ROWS, COLS, 8, 8));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        grid.setBackground(new Color(30, 30, 30));

        List<Item> items = inventory.getItems();

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

        // иконка
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(item.getIconPath())));
        Image img = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(img));

        // tooltip
        button.setToolTipText(buildTooltip(item));

        // подсветка если надет
        if (isEquipped(item)) {
            button.setBorder(new LineBorder(Color.GREEN, 2));
        } else {
            button.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        }

        // клик
        button.addActionListener(e -> {
            inventory.equipItem(index);
            refresh();
        });

        return button;
    }

    private JButton createEmptySlot() {
        JButton empty = new JButton();
        empty.setPreferredSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
        empty.setEnabled(false);
        empty.setBackground(new Color(40, 40, 40));
        empty.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        return empty;
    }

    // ================= ЭКИПИРОВКА =================

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
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(item.getIconPath())));
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

    // ================= ВСПОМОГАТЕЛЬНОЕ =================

    private boolean isEquipped(Item item) {
        return item == inventory.getEquippedWeapon()
                || item == inventory.getEquippedTop()
                || item == inventory.getEquippedShield()
                || item == inventory.getEquippedBottom();
    }

    private String buildTooltip(Item item) {
        StringBuilder sb = new StringBuilder("<html>");
        sb.append("<b>").append(item.getName()).append("</b><br>");
        sb.append("Прочность: ")
                .append(item.getCurrentStrength())
                .append("/")
                .append(item.getStrength())
                .append("<br>");

        if (item instanceof Weapon w) {
            sb.append("Атака: +").append(w.getAttack());
        }
        if (item instanceof Armor a) {
            sb.append("Защита: +").append(a.getProtection());
        }

        sb.append("</html>");
        return sb.toString();
    }

    private void refresh() {
        removeAll();
        add(createGridPanel(), BorderLayout.CENTER);
        add(createEquipmentPanel(), BorderLayout.EAST);
        revalidate();
        repaint();
    }
}
