package MazeGame.battle;

import MazeGame.HUDMessageManager;
import MazeGame.Monster;
import MazeGame.Player;
import MazeGame.cards.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BattleWindow extends JFrame {

    private final BattleEngine battleEngine;
    private final Player player;
    private final Monster enemy;
    private final Monster summon; // может быть null

    private Card selectedCard;
    private BattleResult lastResult;
    private BattleOutcome outcome;

    // HP-полоски
    private JProgressBar playerHpBar;
    private JProgressBar enemyHpBar;
    private JProgressBar summonHpBar;

    public BattleWindow(JFrame owner, Player player, Monster enemy, Monster summon) {
        super("Битва с " + enemy.getName());

        this.player = player;
        this.enemy = enemy;
        this.summon = summon;

        this.battleEngine = new BattleEngine(player, enemy);
        if (summon != null) {
            battleEngine.getContext().setSummon(summon);
        }

        setExtendedState(JFrame.MAXIMIZED_BOTH); // на весь экран
        setUndecorated(true); // без рамки (опционально — можно убрать)
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();

        HUDMessageManager.show("⚔ БОЙ НАЧАЛСЯ!", new Color(220, 40, 40), 50);
    }

    private void initUI() {
        getContentPane().setBackground(new Color(20, 20, 35));

        // Верх: монстр
        add(createEnemyPanel(), BorderLayout.NORTH);

        // Центр: суммон (если есть)
        if (summon != null) {
            add(createSummonPanel(), BorderLayout.CENTER);
        } else {
            add(new JPanel(), BorderLayout.CENTER); // пустое место, если суммона нет
        }

        // Низ: боевые карты + кнопка хода
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createEnemyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        // Картинка монстра
        JLabel enemyLabel = new JLabel();
        ImageIcon enemyIcon = getMonsterIcon(enemy);
        enemyLabel.setIcon(enemyIcon);
        enemyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Tooltip с характеристиками
        enemyLabel.setToolTipText(buildMonsterTooltip(enemy));

        // HP-полоска под монстром
        enemyHpBar = new JProgressBar(0, enemy.getMaxHealth());
        enemyHpBar.setValue(enemy.getHealth());
        enemyHpBar.setStringPainted(true);
        enemyHpBar.setString(enemy.getHealth() + " / " + enemy.getMaxHealth());
        enemyHpBar.setForeground(new Color(220, 60, 60));
        enemyHpBar.setPreferredSize(new Dimension(400, 30));

        panel.add(enemyLabel, BorderLayout.CENTER);
        panel.add(enemyHpBar, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSummonPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));

        // Картинка суммона
        JLabel summonLabel = new JLabel();
        ImageIcon summonIcon = getMonsterIcon(summon);
        summonLabel.setIcon(summonIcon);
        summonLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Tooltip с характеристиками
        summonLabel.setToolTipText(buildMonsterTooltip(summon));

        // HP-полоска над суммоном
        summonHpBar = new JProgressBar(0, summon.getMaxHealth());
        summonHpBar.setValue(summon.getHealth());
        summonHpBar.setStringPainted(true);
        summonHpBar.setString(summon.getHealth() + " / " + summon.getMaxHealth());
        summonHpBar.setForeground(new Color(100, 200, 255));
        summonHpBar.setPreferredSize(new Dimension(400, 30));

        panel.add(summonHpBar, BorderLayout.NORTH);
        panel.add(summonLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 40, 50));

        // Панель с 8 слотами для боевых карт
        JPanel cardsPanel = new JPanel(new GridLayout(1, 8, 15, 0));
        cardsPanel.setOpaque(false);

        CombatDeck deck = player.getCombatDeck();
        Map<TypeEffect, Card> activeCards = deck.getActiveCards();

        for (int i = 0; i < 8; i++) {
            Card card = null;
            if (i < activeCards.size()) {
                card = activeCards.values().stream().toList().get(i);
            }

            CardPanel cardPanel = new CardPanel(card != null ? card : createDummyCard());
            cardPanel.setOnClick(() -> {
//                selectedCard = card;
                performTurn();
            });

            cardsPanel.add(cardPanel);
        }

        panel.add(cardsPanel, BorderLayout.CENTER);

        // Кнопка "Сделать ход"
        JButton turnButton = new JButton("Сделать ход");
        turnButton.setFont(new Font("Arial", Font.BOLD, 20));
        turnButton.setPreferredSize(new Dimension(200, 80));
        turnButton.setBackground(new Color(80, 180, 80));
        turnButton.setForeground(Color.WHITE);
        turnButton.addActionListener(e -> {
            selectedCard = null; // атака без карты
            performTurn();
        });

        panel.add(turnButton, BorderLayout.EAST);

        return panel;
    }

    private ImageIcon getMonsterIcon(Monster monster) {
        String path = monster.getImagePath();
        var url = getClass().getResource(path);
        if (url == null) {
            return new ImageIcon(); // пустая иконка
        }
        ImageIcon original = new ImageIcon(url);
        Image scaled = original.getImage().getScaledInstance(300, 450, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private String buildMonsterTooltip(Monster monster) {
        StringBuilder sb = new StringBuilder("<html>");
        sb.append("<b>").append(monster.getName()).append("</b><br>");
        sb.append("Уровень: ").append(monster.getLevel()).append("<br>");
        sb.append("Здоровье: ").append(monster.getHealth()).append(" / ").append(monster.getMaxHealth()).append("<br>");
        sb.append("Атака: ").append(monster.getTotalAttack()).append("<br>");
        sb.append("Защита: ").append(monster.getTotalDefense()).append("<br>");
        sb.append("</html>");
        return sb.toString();
    }

    // Заглушка для пустых слотов карт
    private Card createDummyCard() {
        return new Card(0, CardType.NONE, CardRarity.GRAY, TypeEffect.NONE, "") {
            @Override
            public void play(BattleContext context, BattleResult result) { }

            @Override
            public String getName() {
                return "";
            }
        };
    }

    private void performTurn() {
        PlayerTurn turn = new PlayerTurn(selectedCard);
        lastResult = battleEngine.resolveTurn(turn);

        for (String msg : lastResult.messages) {
            HUDMessageManager.show(msg, Color.WHITE, 24);
        }

        updateHpBars();

        if (lastResult.isBattleOver()) {
            outcome = lastResult.getOutcome();

            String msg = (outcome == BattleOutcome.PLAYER_WIN) ? "ПОБЕДА!" : "ПОРАЖЕНИЕ...";
            Color color = (outcome == BattleOutcome.PLAYER_WIN) ? new Color(80, 220, 100) : new Color(220, 60, 60);
            HUDMessageManager.show(msg, color, 50);

            dispose();
        }

        selectedCard = null;
    }

    private void updateHpBars() {
        enemyHpBar.setValue(enemy.getHealth());
        enemyHpBar.setString(enemy.getHealth() + " / " + enemy.getMaxHealth());

        if (summonHpBar != null && summon != null) {
            summonHpBar.setValue(summon.getHealth());
            summonHpBar.setString(summon.getHealth() + " / " + summon.getMaxHealth());
        }
    }

    public BattleResult getResult() {
        return lastResult;
    }

    public BattleOutcome getOutcome() {
        return outcome;
    }
}