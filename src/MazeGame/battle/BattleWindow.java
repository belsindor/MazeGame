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

    // Панели юнитов (содержат картинку + красную HP-полоску)
    private UnitPanel enemyPanel;
    private UnitPanel summonPanel; // может быть null

    // Анимация урона (если потом добавишь)
    private Timer attackAnimationTimer;
    private int animationStep = 0;
    private JLabel damageLabel;

    public BattleWindow(JFrame owner, Player player, Monster enemy, Monster summon) {
        super("Битва с " + enemy.getName());

        this.player = player;
        this.enemy = enemy;
        this.summon = summon;

        this.battleEngine = new BattleEngine(player, enemy);
        if (summon != null) {
            battleEngine.getContext().setSummon(summon);
        }

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();

        HUDMessageManager.show("⚔ БОЙ НАЧАЛСЯ!", new Color(220, 40, 40), 50);
    }

    private void initUI() {
        // Фон battle.jpg на весь экран
        JLabel background = new JLabel();
        ImageIcon bgIcon = getBackgroundImage();
        if (bgIcon != null) {
            background.setIcon(bgIcon);
        }
        background.setLayout(new BorderLayout());
        setContentPane(background);

        // Верх: монстр + красная HP-полоска под ним
        enemyPanel = new UnitPanel(enemy, enemy.getName(), new Color(180, 60, 60));
        enemyPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(enemyPanel, BorderLayout.NORTH);

        // Центр: суммон + красная HP-полоска под ним
        if (summon != null) {
            summonPanel = new UnitPanel(summon, summon.getName(), new Color(100, 200, 255));
            summonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
            add(summonPanel, BorderLayout.CENTER);
        } else {
            // Если суммона нет — пустое пространство
            add(new JPanel(), BorderLayout.CENTER);
        }

        // Низ: панель карт + кнопка "Сделать ход"
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 40, 50));

        // 8 слотов для карт
        JPanel cardsPanel = new JPanel(new GridLayout(1, 8, 15, 0));
        cardsPanel.setOpaque(false);

        CombatDeck deck = player.getCombatDeck();
        Map<TypeEffect, Card> activeCards = deck.getActiveCards();

        for (int i = 0; i < 8; i++) {
            Card card = (i < activeCards.size()) ? activeCards.values().stream().toList().get(i) : null;

            CardPanel cardPanel = new CardPanel(card != null ? card : createDummyCard());
            cardPanel.setOnClick(() -> {
                selectedCard = card;
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
            selectedCard = null;
            performTurn();
        });

        panel.add(turnButton, BorderLayout.EAST);

        return panel;
    }

    private ImageIcon getBackgroundImage() {
        var url = getClass().getResource("/images/battle.jpg");
        if (url == null) {
            System.err.println("Фон battle.jpg не найден");
            return null;
        }
        ImageIcon original = new ImageIcon(url);
        Image scaled = original.getImage().getScaledInstance(
                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height,
                Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private ImageIcon getMonsterIcon(Monster monster) {
        String path = monster.getImagePath();
        var url = getClass().getResource(path);
        if (url == null) {
            System.err.println("Картинка монстра не найдена: " + path);
            return new ImageIcon();
        }
        ImageIcon original = new ImageIcon(url);
        Image scaled = original.getImage().getScaledInstance(300, 450, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private Card createDummyCard() {
        return new Card(0, CardType.NONE, CardRarity.GRAY, TypeEffect.NONE, "") {
            @Override
            public void play(BattleContext context, BattleResult result) {}
            @Override
            public String getName() { return ""; }
        };
    }

    private void performTurn() {
        PlayerTurn turn = new PlayerTurn(selectedCard);
        lastResult = battleEngine.resolveTurn(turn);

        for (String msg : lastResult.messages) {
            HUDMessageManager.show(msg, Color.WHITE, 24);
        }

        // Обновляем HP-бары
        enemyPanel.update();
        if (summonPanel != null) {
            summonPanel.update();
        }

        if (lastResult.isBattleOver()) {
            outcome = lastResult.getOutcome();

            String msg = (outcome == BattleOutcome.PLAYER_WIN) ? "ПОБЕДА!" : "ПОРАЖЕНИЕ...";
            Color color = (outcome == BattleOutcome.PLAYER_WIN) ? new Color(80, 220, 100) : new Color(220, 60, 60);
            HUDMessageManager.show(msg, color, 50);

            dispose();
        }

        selectedCard = null;
    }

    public BattleResult getResult() {
        return lastResult;
    }

    public BattleOutcome getOutcome() {
        return outcome;
    }
}