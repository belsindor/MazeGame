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
    private final Monster summon; // может быть null

    private Card selectedCard;
    private BattleResult lastResult;
    private BattleOutcome outcome;

    // UI-компоненты для динамического обновления
    private JProgressBar playerHpBar;
    private JProgressBar enemyHpBar;
    private JProgressBar summonHpBar;

    /**
     * Конструктор: owner — это главное окно игры (JFrame)
     */
    public BattleWindow(JFrame owner, Player player, Monster enemy, Monster summon) {
        super(owner, "Битва с " + enemy.getName(), ModalityType.APPLICATION_MODAL);

        this.player = player;
        this.enemy = enemy;
        this.summon = summon;

        // Создаём движок боя
        this.battleEngine = new BattleEngine(player, enemy);

        // Если суммон выбран — устанавливаем его в контекст
        if (summon != null) {
            battleEngine.getContext().setSummon(summon);
        }

        setSize(1000, 700);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(20, 20, 35));

        // Верхняя панель
        add(createTopInfoPanel(), BorderLayout.NORTH);

        // Центральная часть — бойцы
        add(createBattlefieldPanel(), BorderLayout.CENTER);

        // Нижняя часть — карты + кнопка атаки
        add(createActionPanel(), BorderLayout.SOUTH);

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
        JPanel panel = new JPanel(new GridLayout(1, 3, 30, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setOpaque(false);

        // Игрок
        panel.add(createUnitPanel((BattleUnit) player, "Вы", true, playerHpBar = new JProgressBar()));

        // VS
        JLabel vs = new JLabel("VS", SwingConstants.CENTER);
        vs.setFont(new Font("Arial", Font.BOLD, 48));
        vs.setForeground(new Color(220, 80, 80));
        panel.add(vs);

        // Враг
        panel.add(createUnitPanel(enemy, enemy.getName(), false, enemyHpBar = new JProgressBar()));

        // Суммон (если есть)
        if (summon != null) {
            JPanel summonPanel = createUnitPanel(summon, summon.getName(), true, summonHpBar = new JProgressBar());
            summonPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 200, 255), 3));
            panel.add(summonPanel);
        }

        return panel;
    }

    private JPanel createUnitPanel(BattleUnit unit, String title, boolean isPlayer, JProgressBar hpBar) {
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
        hpBar.setMaximum(unit.getMaxHealth());
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

        // Панель с картами
        CombatDeckPanel deckPanel = new CombatDeckPanel(
                player.getCombatDeck(),
                card -> {
                    selectedCard = card;
                    performTurn();
                }
        );
        JScrollPane scroll = new JScrollPane(deckPanel);
        scroll.setPreferredSize(new Dimension(0, 200));
        panel.add(scroll, BorderLayout.CENTER);

        // Кнопка "Атаковать без карты"
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

        // Показываем сообщения
        for (String msg : lastResult.messages) {
            HUDMessageManager.show(msg, Color.WHITE, 24);
        }

        // Обновляем HP-бары
        updateHpBars();

        // Проверяем конец боя
        if (lastResult.isBattleOver()) {
            outcome = lastResult.getOutcome();

            String msg = (outcome == BattleOutcome.PLAYER_WIN) ? "ПОБЕДА!" : "ПОРАЖЕНИЕ...";
            Color color = (outcome == BattleOutcome.PLAYER_WIN) ? new Color(80, 220, 100) : new Color(220, 60, 60);
            HUDMessageManager.show(msg, color, 50);

            dispose(); // закрываем окно боя
        }

        selectedCard = null;
    }

    private void updateHpBars() {
        playerHpBar.setValue(player.getHealth());
        playerHpBar.setString(player.getHealth() + " / " + player.getMaxHealth());

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