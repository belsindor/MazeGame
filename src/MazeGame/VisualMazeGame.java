package MazeGame;

import MazeGame.battle.*;
import MazeGame.cards.*;
import MazeGame.item.Item;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class VisualMazeGame {

    private VisualLocation[][] map;
    private int[][] currentMaze;
    private boolean[][] visited;

    private int playerX;
    private int playerY;
    private int exitX;
    private int exitY;

    private final Player player;
    private final Random random = new Random();

    private static final int MONSTER_APPEARANCE_CHANCE = 100;
    private static final int HEAL_PER_STEP = 1;

    private boolean secondMazeLoaded = false;

    public VisualMazeGame(Player player) {
        this.player = player;
        loadMaze(MAZE_1, 4, 3, 27, 9);

        // Добавляем стартового суммона
        SummonCard startingSummon = SummonFactory.ancestor_spirit();
        if (startingSummon != null) {
            player.getCardCollection().addCard(startingSummon);
            player.getSummonDeck().addSummon(startingSummon);
            player.getSummonDeck().selectSummon(startingSummon);
            System.out.println("Стартовый суммон 'Дух предка' добавлен в коллекцию и активные суммоны");
        } else {
            System.err.println("Ошибка: ancestor_spirit() не найден в SummonFactory");
        }
    }

    private void loadMaze(int[][] maze, int startX, int startY, int exitX, int exitY) {
        this.currentMaze = maze;
        this.map = buildMapFromMaze(maze);

        this.playerX = startX;
        this.playerY = startY;
        this.exitX = exitX;
        this.exitY = exitY;

        visited = new boolean[map.length][map[0].length];
        visited[playerY][playerX] = true;
    }

    private VisualLocation[][] buildMapFromMaze(int[][] maze) {
        int height = maze.length;
        int width = maze[0].length;

        VisualLocation[][] result = new VisualLocation[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (maze[y][x] == 0) {
                    result[y][x] = null;
                    continue;
                }

                boolean north = y > 0 && maze[y - 1][x] == 1;
                boolean south = y < height - 1 && maze[y + 1][x] == 1;
                boolean west = x > 0 && maze[y][x - 1] == 1;
                boolean east = x < width - 1 && maze[y][x + 1] == 1;

                result[y][x] = new VisualLocation(north, east, south, west);
            }
        }
        return result;
    }

    public VisualLocation getCurrentLocation() {
        return map[playerY][playerX];
    }

    public String getCurrentImageName() {
        VisualLocation loc = getCurrentLocation();
        return loc != null ? loc.getImageName() : "";
    }

    // ДВИЖЕНИЕ
    public void moveNorth() {
        if (playerY > 0 && getCurrentLocation() != null && getCurrentLocation().hasNorth()) {
            playerY--;
            afterMove();
        }
    }

    public void moveSouth() {
        if (playerY < map.length - 1 && getCurrentLocation() != null && getCurrentLocation().hasSouth()) {
            playerY++;
            afterMove();
        }
    }

    public void moveWest() {
        if (playerX > 0 && getCurrentLocation() != null && getCurrentLocation().hasWest()) {
            playerX--;
            afterMove();
        }
    }

    public void moveEast() {
        if (playerX < map[0].length - 1 && getCurrentLocation() != null && getCurrentLocation().hasEast()) {
            playerX++;
            afterMove();
        }
    }

    private void afterMove() {
        visited[playerY][playerX] = true;
        GameWindow.getPanel().clearPendingMonster();

        if (playerX == exitX && playerY == exitY) {
            if (!secondMazeLoaded) {
                HUDMessageManager.showInfo("🚪 Второй лабиринт открыт!");
                loadMaze(MAZE_2, 1, 1, 28, 6);
                secondMazeLoaded = true;
            } else {
                HUDMessageManager.showInfo("🏁 Поздравляем! Вы нашли выход!");
            }
            return;
        }

        checkMonsterAppearance();
        checkHeal();
    }

    private void checkMonsterAppearance() {
        if (random.nextInt(100) < MONSTER_APPEARANCE_CHANCE) {
            // Уровень монстра: от 1 до уровня игрока
            int maxEnemyLevel = player.getLevel();
            int enemyLevel = random.nextInt(maxEnemyLevel) + 1;

            Monster enemy = MonsterFactory.createEnemyForLevel(enemyLevel);
            GameWindow.getPanel().showPendingMonster(enemy);
            System.out.println("Появился монстр уровня " + enemyLevel + " (игрок: " + player.getLevel() + ")");
        }
    }

    private void checkHeal() {
        if (player.getHealth() < player.getMaxHealth()) {
            player.heal(HEAL_PER_STEP);
            HUDMessageManager.showHeal("✨ +1 HP");
        }
    }

    /**
     * Запуск боя
     */
    public void startBattle(JFrame owner, Monster monster) {
        System.out.println("=== startBattle ЗАПУЩЕН для монстра: " + monster.getName() + " ===");

        GameWindow.getPanel().clearPendingMonster();
        GameWindow.setBattleActive(true);
        GameWindow.showBattleScreen();

        System.out.println("Открываем окно выбора суммона...");
        SummonDeck summonDeck = player.getSummonDeck();
        SummonSelectionWindow summonWindow = new SummonSelectionWindow(summonDeck);
        Optional<SummonCard> selectedOpt = summonWindow.showAndWait();

        System.out.println("Выбор суммона завершён. Результат: " + (selectedOpt.isPresent() ? "выбран" : "отменён/пусто"));

        Monster summonInstance = null;
        if (selectedOpt.isPresent()) {
            SummonCard selectedCard = selectedOpt.get();
            summonInstance = new Monster(selectedCard.getMonsterTemplate());
            summonDeck.selectSummon(selectedCard);
            System.out.println("Суммон создан: " + summonInstance.getName());
        } else {
            System.out.println("Суммон НЕ выбран (игрок отменил или закрыл окно)");
        }

        System.out.println("Создаём контекст боя...");
        BattleContext context = new BattleContext(player, monster);
        if (summonInstance != null) {
            context.setSummon(summonInstance);
        }

        System.out.println("Создаём BattleWindow...");
        BattleWindow battleWindow = new BattleWindow(
                owner,
                player,
                monster,
                summonInstance
        );

        System.out.println("Устанавливаем BattleWindow visible...");
        battleWindow.setVisible(true);
        System.out.println("BattleWindow открыт!");

        GameWindow.hideBattleScreen();
        GameWindow.setBattleActive(false);

        BattleOutcome outcome = battleWindow.getOutcome();
        System.out.println("Результат боя: " + outcome);

        if (outcome == BattleOutcome.PLAYER_LOSE) {
            JOptionPane.showMessageDialog(owner, "Вы погибли...");
            System.exit(0);
        }

        BattleResult result = battleWindow.getResult();
        if (result != null && result.isPlayerWin()) {
            BattleReward reward = result.getReward();
            player.gainExperience(reward.experience());
            HUDMessageManager.showInfo("✨ Получено опыта: +" + reward.experience());

            // Генерируем и обрабатываем дроп
            List<CardDropService.DropEntry> drops = new CardDropService().generateDrop(monster);
            player.processDrop(drops);

            // УДАЛЕНИЕ СТАРТОВОГО СУММОНА ПОСЛЕ ПЕРВОГО УСПЕШНОГО БОЯ
            if (!player.hasUsedStartingSummon()) {
                SummonCard startingSummon = SummonFactory.ancestor_spirit();
                if (startingSummon != null) {
                    // Удаляем из regularCards (по объекту карты)
                    player.getCardCollection().removeCard(startingSummon);

                    // Удаляем из активных суммонов по типу
                    player.getCardCollection().removeCardById(10000);  // ID ancestor_spirit = 10000
                    player.getSummonDeck().removeSummon(startingSummon.getUnitType());

                    // Помечаем как использованный
                    player.markStartingSummonUsed();

                    HUDMessageManager.showInfo("Стартовый суммон 'Дух предка' израсходован после первого боя!");
                    System.out.println("Стартовый суммон ancestor_spirit удалён из regularCards и active после первого боя");
                }
            }

            // Показываем дроп игроку
            StringBuilder sb = new StringBuilder("Выпало:\n");
            for (CardDropService.DropEntry d : drops) {
                if (d.getSummonCard() != null) sb.append(" - ").append(d.getSummonCard().getName()).append("\n");
                if (d.getCard() != null) sb.append(" - ").append(d.getCard().getId()).append("\n");
                if (d.getItem() != null) sb.append(" - ").append(d.getItem().getName()).append("\n");
            }
            JOptionPane.showMessageDialog(owner, sb.toString(), "Награда за бой", JOptionPane.INFORMATION_MESSAGE);
        }

        summonDeck.resetSelection();
        System.out.println("=== startBattle ЗАВЕРШЁН ===");
    }

    // Геттеры
    public Player getPlayer() { return player; }
    public boolean[][] getVisited() { return visited; }
    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }
    public int[][] getCurrentMaze() { return currentMaze; }
    public boolean isSecondMazeLoaded() { return secondMazeLoaded; }

    public void showHelp() {
        JOptionPane.showMessageDialog(null,
                """
                        Управление:
                        W / S / A / D — движение
                        H — помощь
                        M — карта
                        Esc — выход
                        """,
                "Помощь", JOptionPane.INFORMATION_MESSAGE);
    }
    // ЛАБИРИНТЫ
    private static final int[][] MAZE_1 = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
    };

    private static final int[][] MAZE_2 = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0},
    };

    public void loadFromSave(GameSaveData data) {
        this.playerX = data.playerX;
        this.playerY = data.playerY;
        this.secondMazeLoaded = data.secondMazeLoaded;

        loadMaze(
                secondMazeLoaded ? MAZE_2 : MAZE_1,
                data.playerX,
                data.playerY,
                secondMazeLoaded ? 28 : 27,
                secondMazeLoaded ? 6 : 9
        );

        this.visited = data.visited;
    }
}