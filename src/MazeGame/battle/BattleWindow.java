package MazeGame.battle;

import MazeGame.HUDMessageManager;
import MazeGame.Monster;
import MazeGame.Player;

import javax.swing.*;
import java.awt.*;

public class BattleWindow extends JDialog {

    private final BattleEngine battle;
    private boolean playerWon = false;

    public BattleWindow(JFrame owner, Player player, Monster monster) {
        super((JFrame) null, "Бой", true);
        this.battle = new BattleEngine(player, monster);
        initUI();
    }

    public boolean isPlayerWon() {
        return playerWon;
    }

    private void initUI() {
        setSize(420, 220);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        JButton attack = new JButton("⚔ Атака");
        add(attack, BorderLayout.CENTER);

        attack.addActionListener(e -> performTurn());

        HUDMessageManager.show("⚔ БОЙ НАЧАЛСЯ", Color.RED, 40);
    }

    private void performTurn() {
        BattleResult result = battle.resolveTurn(new PlayerTurn(null));

        for (String msg : result.messages) {
            HUDMessageManager.show(msg);
        }

        if (result.isBattleOver()) {
            playerWon = battle.isPlayerAlive();
            HUDMessageManager.show("🏁 БОЙ ОКОНЧЕН", Color.YELLOW, 36);
            dispose();
        }
    }
}
