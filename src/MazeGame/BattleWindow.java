package MazeGame;

import javax.swing.*;
import java.awt.*;

public class BattleWindow extends JFrame {

    public BattleWindow(Player player, Monster monster) {
        setTitle("Бой");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        BattleLogic logic = new BattleLogic(player, monster);
        BattlePanel panel = new BattlePanel();

        JPanel buttons = new JPanel();
        JButton attack = new JButton("Атаковать");
        JButton escape = new JButton("Сбежать");

        buttons.add(attack);
        buttons.add(escape);

        attack.addActionListener(e -> {
            panel.addText(logic.getStatus());
            panel.addText(logic.playerAttack());

            if (logic.isBattleOver()) {
                dispose();
            }
        });

        escape.addActionListener(e -> {
            if (logic.tryEscape()) {
                panel.addText("✅ Вы сбежали!");
                dispose();
            } else {
                panel.addText("❌ Не удалось сбежать!");
            }
        });

        add(panel, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        panel.addText("⚔ НАЧАЛО БОЯ ⚔");
        panel.addText(logic.getStatus());

        setVisible(true);
    }
}
