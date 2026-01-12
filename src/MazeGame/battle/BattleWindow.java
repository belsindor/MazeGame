package MazeGame.battle;

import MazeGame.HUDMessageManager;
import MazeGame.Monster;
import MazeGame.Player;
import MazeGame.cards.Card;
import MazeGame.cards.CombatDeckPanel;

import javax.swing.*;
import java.awt.*;

public class BattleWindow extends JDialog {

    private final BattleEngine battleEngine;
    private final Player player;
    private final Monster enemy;
    private final Monster summon;

    private Card selectedCard;
    private BattleResult lastResult;
    private BattleOutcome outcome;

    public BattleWindow(JFrame owner, Player player, Monster enemy, Monster summon) {
        super(owner, "Бой", true);
        this.player = player;
        this.enemy = enemy;
        this.summon = summon;

        this.battleEngine = new BattleEngine(player, enemy, summon);

        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        initUI();
    }

    public BattleResult getResult() {
        return lastResult;
    }

    public BattleOutcome getOutcome() {
        return outcome;
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(20, 20, 35));

        // Верхняя панель — информация о бое
        JPanel topPanel = createTopInfoPanel();
        add(topPanel, BorderLayout.NORTH);

        // Центральная часть — бойцы
        JPanel battlefield = createBattlefieldPanel();
        add(battlefield, BorderLayout.CENTER);

        // Нижняя часть — карты и кнопка атаки
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);

        HUDMessageManager.show("⚔ БОЙ НАЧАЛСЯ!", new Color(220, 40, 40), 42);
    }

    private JPanel createTopInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panel.setBackground(new Color(40, 40, 60));

        JLabel title = new JLabel("Битва с " + enemy.getName() + " (Lv. " + enemy.getLevel() + ")", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBattlefieldPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setOpaque(false);

        // Левый — игрок
        panel.add(createUnitPanel(player, "Вы", true));

        // Центр — vs
        JLabel vs = new JLabel("VS", SwingConstants.CENTER);
        vs.setFont(new Font("Arial", Font.BOLD, 48));
        vs.setForeground(new Color(220, 80, 80));
        panel.add(vs);

        // Правый — враг
        panel.add(createUnitPanel(enemy, enemy.getName(), false));

        return panel;
    }

    private JPanel createUnitPanel(BattleUnit unit, String title, boolean isPlayer) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isPlayer ? new Color(60, 180, 80) : new Color(180, 60, 60), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(new Color(35, 35, 55));

        JLabel nameLabel = new JLabel(title + " (Lv. " + unit.getLevel() + ")", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        panel.add(nameLabel, BorderLayout.NORTH);

        // Полоска HP
        JProgressBar hpBar = new JProgressBar(0, unit.getMaxHealth());
        hpBar.setValue(unit.getHealth());
        hpBar.setString(unit.getHealth() + " / " + unit.getMaxHealth());
        hpBar.setStringPainted(true);
        hpBar.setForeground(isPlayer ? new Color(80, 220, 100) : new Color(220, 60, 60));
        panel.add(hpBar, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        panel.setBackground(new Color(25, 25, 45));

        // Панель карт
        CombatDeckPanel deckPanel = new CombatDeckPanel(
                player.getCombatDeck(),
                card -> {
                    selectedCard = card;
                    performTurn();
                }
        );
        JScrollPane scroll = new JScrollPane(deckPanel);
        scroll.setPreferredSize(new Dimension(0, 180));
        panel.add(scroll, BorderLayout.CENTER);

        // Кнопка атаки без карты
        JButton attackButton = new JButton("Атаковать без карты");
        attackButton.setFont(new Font("Arial", Font.BOLD, 16));
        attackButton.setBackground(new Color(180, 60, 60));
        attackButton.setForeground(Color.WHITE);
        attackButton.addActionListener(e -> {
            selectedCard = null;
            performTurn();
        });
        panel.add(attackButton, BorderLayout.EAST);

        return panel;
    }

    private void performTurn() {
        PlayerTurn turn = new PlayerTurn(selectedCard);
        lastResult = battleEngine.resolveTurn(turn);

        // Показываем все сообщения хода
        for (String msg : lastResult.messages) {
            HUDMessageManager.show(msg, Color.WHITE, 24);
        }

        // Обновляем интерфейс (если бы здесь были динамические полоски HP — обновляли бы)

        if (lastResult.isBattleOver()) {
            outcome = lastResult.getOutcome();

            if (outcome == BattleOutcome.PLAYER_WIN) {
                HUDMessageManager.show("ПОБЕДА!", new Color(80, 220, 100), 50);
            } else {
                HUDMessageManager.show("ПОРАЖЕНИЕ...", new Color(220, 60, 60), 50);
            }

            dispose();
        }

        selectedCard = null;
    }
}