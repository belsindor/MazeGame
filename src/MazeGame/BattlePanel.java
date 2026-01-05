package MazeGame;

import javax.swing.*;
import java.awt.*;

public class BattlePanel extends JPanel {

    private Image background;
    private JTextArea textArea;

    public BattlePanel() {
        setLayout(new BorderLayout());

        // Фон боя
        background = new ImageIcon("images/battle.jpg").getImage();

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // СТИЛЬ ТЕКСТА
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("Monospaced", Font.BOLD, 16));

        // ВАЖНО: полностью прозрачный текст
        textArea.setOpaque(false);

        JScrollPane scroll = new JScrollPane(textArea);

        // 👇 КЛЮЧЕВЫЕ СТРОКИ
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(scroll, BorderLayout.SOUTH);
        scroll.setPreferredSize(new Dimension(900, 220));

    }

    public void addText(String text) {
        textArea.append(text + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Рисуем фон
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        // Затемняем низ под текстом (чтобы читалось)
        g.setColor(new Color(0, 0, 0, 140));
        g.fillRect(0, getHeight() - 220, getWidth(), 220);
    }
}
