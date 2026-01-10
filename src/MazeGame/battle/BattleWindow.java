package MazeGame.battle;

import MazeGame.HUDMessageManager;
import MazeGame.Monster;
import MazeGame.Player;
import MazeGame.cards.Card;
import MazeGame.cards.CombatDeckPanel;

import javax.swing.*;
import java.awt.*;

public class BattleWindow extends JDialog {

    private final BattleEngine battle;
    private final Player player;

    private Card selectedCard;

    private BattleResult lastResult;
    private BattleOutcome outcome;

    public BattleWindow(JFrame owner,
                        Player player,
                        Monster monster,
                        Monster summon) {

        super(owner, "Бой", true);

        this.player = player;

        this.battle = new BattleEngine(player, monster, summon);

        initUI();
    }

    public BattleResult getResult() {
        return lastResult;
    }

    public BattleOutcome getOutcome() {
        return outcome;
    }

    private void initUI() {
        setSize(600, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        JLabel info = new JLabel("Выберите карту (или атакуйте без неё)", SwingConstants.CENTER);
        add(info, BorderLayout.NORTH);

        CombatDeckPanel deckPanel =
                new CombatDeckPanel(player.getCombatDeck(), card -> {
                    selectedCard = card;
                    performTurn();
                });

        add(deckPanel, BorderLayout.SOUTH);

        HUDMessageManager.show("⚔ БОЙ НАЧАЛСЯ", Color.RED, 40);
    }

    private void performTurn() {

        PlayerTurn turn = new PlayerTurn(selectedCard);
        lastResult = battle.resolveTurn(turn);

        for (String msg : lastResult.messages) {
            HUDMessageManager.show(msg);
        }

        if (lastResult.isBattleOver()) {
            outcome = lastResult.getOutcome();
            dispose();
        }

        selectedCard = null;
    }
}
