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
    private Monster summon; // может стать null после смерти

    private Card selectedCard;
    private BattleResult lastResult;
    private BattleOutcome outcome;

    private UnitPanel enemyPanel;
    private UnitPanel activeAllyPanel;

    private JPanel centerPanel; // контейнер для динамической замены суммон ↔ игрок

    public BattleWindow(JFrame owner, Player player, Monster enemy, Monster summon) {
        super("Битва с " + enemy.getName());

        this.player = player;
        this.enemy  = enemy;
        this.summon = summon;

        this.battleEngine = new BattleEngine(player, enemy);
        battleEngine.setSummon(summon);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();

        HUDMessageManager.show("⚔ БОЙ НАЧАЛСЯ!", new Color(220, 40, 40), 50);
    }

    private void initUI() {
        // Фон
        JLabel background = new JLabel();
        ImageIcon bgIcon = getBackgroundImage();
        if (bgIcon != null) {
            background.setIcon(bgIcon);
        }
        background.setLayout(new BorderLayout());
        setContentPane(background);

        // Враг сверху
        enemyPanel = new UnitPanel(enemy, enemy.getName(), new Color(180, 60, 60));
        enemyPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(enemyPanel, BorderLayout.NORTH);

        // Центр — контейнер для активного союзника
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        add(centerPanel, BorderLayout.CENTER);

        // Изначально отображаем текущего активного
        updateActiveAllyPanel();

        // Нижняя панель с картами
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private void updateActiveAllyPanel() {
        centerPanel.removeAll();

        Monster currentSummon = battleEngine.getContext().getSummon();

        if (currentSummon != null && currentSummon.isAlive()) {
            activeAllyPanel = new UnitPanel(currentSummon, currentSummon.getName(), new Color(100, 200, 255));
        } else {
            activeAllyPanel = new UnitPanel(player, player.getName(), new Color(100, 200, 255));
        }

        activeAllyPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        centerPanel.add(activeAllyPanel, BorderLayout.CENTER);

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 40, 50));

        // Карты
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

        // Кнопка пропуска хода
        JButton turnButton = new JButton("Пропустить ход");
        turnButton.setFont(new Font("Arial", Font.BOLD, 20));
        turnButton.setPreferredSize(new Dimension(200, 80));
        turnButton.setBackground(new Color(120, 120, 120));
        turnButton.setForeground(Color.WHITE);
        turnButton.addActionListener(e -> {
            selectedCard = null;
            performTurn();
        });

        panel.add(turnButton, BorderLayout.EAST);

        return panel;
    }

    private void performTurn() {
        PlayerTurn turn = new PlayerTurn(selectedCard);
        lastResult = battleEngine.resolveTurn(turn);

        // Показываем все сообщения хода
        for (String msg : lastResult.messages) {
            HUDMessageManager.show(msg, Color.WHITE, 24);
        }

        // Обновляем UI
        enemyPanel.update();
        if (activeAllyPanel != null) {
            activeAllyPanel.update();
        }
        updateActiveAllyPanel();        // ← важно: меняем панель, если суммон умер

        // Проверка конца боя
        if (lastResult.isBattleOver()) {
            outcome = lastResult.getOutcome();

            String msg = (outcome == BattleOutcome.PLAYER_WIN) ? "ПОБЕДА!" : "ПОРАЖЕНИЕ...";
            Color color = (outcome == BattleOutcome.PLAYER_WIN) ? new Color(80, 220, 100) : new Color(220, 60, 60);
            HUDMessageManager.show(msg, color, 50);

            // Здесь можно добавить небольшую задержку перед закрытием, если хочешь
            // new Timer(2000, e -> dispose()).start();
            dispose();
        }

        selectedCard = null;
    }

    private ImageIcon getBackgroundImage() {
        var url = getClass().getResource("/images/battle.jpg");
        if (url == null) {
            System.err.println("battle.jpg не найден");
            return null;
        }
        ImageIcon original = new ImageIcon(url);
        Image scaled = original.getImage().getScaledInstance(
                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height,
                Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private Card createDummyCard() {
        return new Card(0, CardType.NONE, CardRarity.GRAY, TypeEffect.NONE, "") {
            @Override
            public void play(BattleContext context, BattleResult result) {}
            @Override
            public String getName() {
                return "";
            }
        };
    }

    public BattleResult getResult() {
        return lastResult;
    }

    public BattleOutcome getOutcome() {
        return outcome;
    }

    public BattleContext getContext() {
        return battleEngine.getContext();
    }

    // Или ещё проще и понятнее:
    public Monster getCurrentSummon() {
        return battleEngine.getContext().getSummon();
    }
}