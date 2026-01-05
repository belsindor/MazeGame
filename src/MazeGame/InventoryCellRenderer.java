package MazeGame;

import javax.swing.*;
import java.awt.*;

public class InventoryCellRenderer extends JPanel
        implements ListCellRenderer<Item> {

    private JLabel icon = new JLabel();
    private JLabel text = new JLabel();

    public InventoryCellRenderer() {
        setLayout(new BorderLayout(10, 0));
        add(icon, BorderLayout.WEST);
        add(text, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends Item> list,
            Item item,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        icon.setIcon(new ImageIcon(item.getIconPath()));
        text.setText(item.getName() +
                " (" + item.getCurrentStrength() + "/" + item.getStrength() + ")");

        setBackground(isSelected ? new Color(70,70,70) : new Color(40,40,40));
        setOpaque(true);
        text.setForeground(Color.WHITE);

        return this;
    }
}
