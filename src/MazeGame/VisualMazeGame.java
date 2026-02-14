package MazeGame;

import MazeGame.battle.*;
import MazeGame.cards.*;


import javax.swing.*;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class VisualMazeGame implements Serializable {

    private VisualLocation[][] map;
    private int[][] currentMaze;
    private boolean[][] visited;

    private int playerX;
    private int playerY;
    private int exitX;
    private int exitY;

    private final Player player;
    private final Random random = new Random();

    private static final int MONSTER_APPEARANCE_CHANCE = 25;
    private static final int HEAL_PER_STEP = 1;

    private boolean secondMazeLoaded = false;
    private boolean thirdMazeLoaded = false;

    public VisualMazeGame(Player player) {
        this.player = player;
        loadMaze(MAZE_1, 1, 1, 27, 9);

        // Добавляем стартового суммона
        SummonCard startingSummon = SummonFactory.ancestor_spirit();
        if (startingSummon != null) {
            player.getCardCollection().addCard(startingSummon);
//            player.getSummonDeck().addSummon(startingSummon);
//            player.getSummonDeck().selectSummon(startingSummon);
            player.getSummonDeck().refreshActive(player.getCardCollection());

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
            } else if (!thirdMazeLoaded) {
                HUDMessageManager.showInfo("🚪 Третий лабиринт открыт!");
                loadMaze(MAZE_3, 0, 11, 39, 11);
                thirdMazeLoaded = true;
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
//            System.out.println("Появился монстр уровня " + enemyLevel + " (игрок: " + player.getLevel() + ")");
        }
    }

    private void checkHeal() {
        if (player.getHealth() < player.getMaxHealth()) {
            player.heal(HEAL_PER_STEP);
            HUDMessageManager.showHeal("✨ +1 HP");
        }
    }

    /**
     * Запуск боя с монстром
     *
     * @param owner  родительское окно (для диалогов)
     * @param monster монстр, с которым начинается бой
     */
    public void startBattle(JFrame owner, Monster monster) {
        System.out.println("=== startBattle: " + monster.getName() + " (lvl " + monster.getLevel() + ") ===");

        // Подготовка UI
        GameWindow.getPanel().clearPendingMonster();
        GameWindow.setBattleActive(true);
        GameWindow.showBattleScreen();

        // 1. Выбор суммона игроком
        SummonDeck summonDeck = player.getSummonDeck();
        SummonSelectionWindow summonWindow = new SummonSelectionWindow(summonDeck);
        Optional<SummonCard> selectedOpt = summonWindow.showAndWait();

        Monster summonInstance = null;
        SummonCard selectedSummonCard = null;

        if (selectedOpt.isPresent()) {
            selectedSummonCard = selectedOpt.get();
            summonInstance = new Monster(selectedSummonCard.getMonsterTemplate());
            summonDeck.selectSummon(selectedSummonCard);
            System.out.println("Выбран суммон: " + summonInstance.getName() +
                    " (" + selectedSummonCard.getUnitType() + ")");
        } else {
            System.out.println("Суммон не выбран");
        }

        // 2. Запуск окна боя
        BattleWindow battleWindow = new BattleWindow(owner, player, monster, summonInstance);
        battleWindow.setVisible(true);

        // 3. Получаем результат после закрытия окна
        BattleOutcome outcome = battleWindow.getOutcome();
        BattleResult result = battleWindow.getResult();

        GameWindow.hideBattleScreen();
        GameWindow.setBattleActive(false);

        // 4. Поражение → конец игры
        if (outcome == BattleOutcome.PLAYER_LOSE) {
            JOptionPane.showMessageDialog(owner,
                    "Вы погибли в бою...",
                    "Поражение",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return;
        }

        // 5. Победа → награды, дроп, обработка
        if (outcome == BattleOutcome.PLAYER_WIN && result != null) {
            // Опыт
            BattleReward reward = result.getReward();
            if (reward != null && reward.experience() > 0) {
                player.gainExperience(reward.experience());
                HUDMessageManager.showInfo("✨ + " + reward.experience() + " опыта");
            }

            // Дроп
            List<CardDropService.DropEntry> drops = new CardDropService().generateDrop(monster);
            player.processDrop(drops);

            // Показ выпавшего (в JOptionPane)
            StringBuilder sb = new StringBuilder("Выпало:\n");
            boolean hasDrop = false;

            for (CardDropService.DropEntry drop : drops) {
                if (drop.getSummonCard() != null) {
                    sb.append("Суммон-карта: ").append(drop.getSummonCard().getName()).append("\n");
                    hasDrop = true;
                } else if (drop.getCard() != null) {
                    sb.append("Карта: ").append(drop.getCard().getName()).append("\n");
                    hasDrop = true;
                } else if (drop.getItem() != null) {
                    sb.append("Предмет: ").append(drop.getItem().getName()).append("\n");
                    hasDrop = true;
                }
            }

            if (hasDrop) {
                JOptionPane.showMessageDialog(owner, sb.toString(),
                        "Награда за победу", JOptionPane.INFORMATION_MESSAGE);
            } else {
                HUDMessageManager.showInfo("В этот раз ничего не выпало...");
            }

            // ── Специальное поведение стартового суммона ───────────────────────
            // Удаляем после первой победы (независимо от использования)
            if (!player.hasUsedStartingSummon()) {
                SummonCard starter = SummonFactory.ancestor_spirit();
                if (starter != null) {
                    player.getCardCollection().removeCard(starter);
                    player.getSummonDeck().removeFromActive(starter);  // используем метод по карте
                    player.markStartingSummonUsed();
//                    HUDMessageManager.showInfo("Стартовый суммон «Дух предка» израсходован после первого боя!");
//                    System.out.println("Стартовый суммон удалён из коллекции и active");
                }
            }
        }

        // 6. Проверка состояния суммона после боя (если он был выбран)
        Monster currentSummon = battleWindow.getContext().getSummon();

        if (summonInstance != null) {
            if (currentSummon == null || !currentSummon.isAlive()) {
                // Суммон погиб → удаляем из active и коллекции
                summonDeck.removeFromActive(selectedSummonCard);
                player.getCardCollection().removeCard(selectedSummonCard);

//                String name = selectedSummonCard.getName();
//                HUDMessageManager.show("☠ " + name + " погиб и был удалён из колоды");
//                System.out.println("Удалён погибший суммон: " + name + " (" + selectedSummonCard.getUnitType() + ")");
//            } else {
//                System.out.println("Суммон выжил: " + currentSummon.getName() +
//                        " (" + currentSummon.getHealth() + " HP осталось)");
            }
        }
    }

    // Геттеры
    public Player getPlayer() { return player; }
    public boolean[][] getVisited() { return visited; }
    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }
    public int[][] getCurrentMaze() { return currentMaze; }
    public boolean isSecondMazeLoaded() { return secondMazeLoaded; }
    public boolean isThirdMazeLoaded() { return thirdMazeLoaded; }

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
            {0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0},
            {0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0},
            {0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0},
            {0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
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

    private static final int[][] MAZE_3 = {
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0},
            {0,0,0,1,0,1,0,0,0,0,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,0,0,1,0,1,0,0,0,0,0,0,0},
            {0,0,0,1,0,1,1,1,1,1,1,1,0,1,0,1,0,0,0,1,0,1,1,1,0,1,1,1,1,1,0,1,0,0,0,0,0,0,0},
            {0,0,0,1,0,1,0,0,0,1,0,1,0,1,0,1,0,0,0,1,0,1,0,1,0,1,0,0,0,1,0,1,0,0,0,0,0,0,0},
            {0,0,0,1,0,1,0,0,0,1,0,1,0,1,0,1,0,0,0,1,0,1,0,1,0,1,0,1,0,1,1,1,0,0,0,0,0,0,0},
            {0,0,0,1,0,1,0,0,0,1,0,1,0,1,1,1,0,0,0,1,1,1,0,1,0,1,0,1,0,0,1,0,0,0,0,0,0,0,0},
            {0,0,0,1,0,1,0,0,0,1,0,1,0,1,0,1,0,0,0,1,0,1,0,1,1,1,0,1,1,1,1,0,0,0,0,0,0,0,0},
            {0,0,0,1,0,1,0,0,0,1,0,1,0,1,0,1,0,0,0,1,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,1,0,1,0,0,0,1,0,1,0,1,0,1,0,0,0,1,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {1,1,1,1,0,1,0,0,0,1,0,1,0,1,0,1,0,0,0,1,0,1,0,1,0,1,0,0,0,0,0,0,0,1,1,1,1,1,1},
            {0,0,0,1,1,1,1,1,1,1,0,1,0,1,0,1,1,1,1,1,0,1,0,1,0,1,0,0,0,0,0,0,0,1,0,1,0,0,0},
            {0,0,0,1,0,0,0,0,0,1,0,1,0,1,0,0,0,1,0,0,0,1,0,1,0,1,0,0,0,0,0,0,0,1,0,1,0,0,0},
            {0,0,0,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,0,1,0,1,0,0,0,0,0,0,0,1,0,1,1,0,0},
            {0,0,0,1,0,1,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,1,0,1,0,1,0,1,1,1,1,0,0,1,0,1,0,0,0},
            {0,0,0,1,0,1,0,0,1,1,1,1,0,1,1,1,1,1,1,1,0,1,0,1,0,1,0,1,0,0,1,0,0,1,0,1,0,1,0},
            {0,0,0,1,0,1,0,0,1,0,1,0,0,0,0,0,0,0,0,1,0,1,0,1,1,1,0,1,0,1,1,1,0,1,0,1,1,1,0},
            {0,0,0,1,0,1,0,0,1,0,0,0,0,1,1,1,0,0,0,1,0,1,0,1,0,1,0,0,0,1,0,1,0,1,0,0,0,0,0},
            {0,0,0,1,0,1,0,0,0,0,0,0,0,1,0,1,0,0,0,1,0,1,0,1,0,1,0,0,0,1,0,1,1,1,0,0,0,0,0},
            {0,0,0,1,0,1,0,0,0,0,0,0,0,1,0,1,1,1,1,1,0,1,0,1,0,1,1,1,1,1,0,1,0,0,0,0,0,0,0},
            {0,0,0,1,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,1,0,1,0,0,0,1,0,1,0,0,0,0,0,0,0},
            {0,0,0,1,1,1,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
    };

    public void loadFromSave(GameSaveData data) {
        this.playerX = data.playerX;
        this.playerY = data.playerY;
        this.secondMazeLoaded = data.secondMazeLoaded;
        this.thirdMazeLoaded = data.thirdMazeLoaded; // ДОБАВИТЬ

        int[][] maze;
        int startX, startY, exitX, exitY;

        if (thirdMazeLoaded) {
            maze = MAZE_3;
            startX = data.playerX;
            startY = data.playerY;
            exitX = 39;
            exitY = 11;
        } else if (secondMazeLoaded) {
            maze = MAZE_2;
            startX = data.playerX;
            startY = data.playerY;
            exitX = 28;
            exitY = 6;
        } else {
            maze = MAZE_1;
            startX = data.playerX;
            startY = data.playerY;
            exitX = 27;
            exitY = 9;
        }

        loadMaze(maze, startX, startY, exitX, exitY);
        this.visited = data.visited;

        restoreDecks(data);



    }

    public void restoreDecks(GameSaveData data) {

        player.getCardCollection().clear();

        if (data.regularCards != null) {
            for (var entry : data.regularCards.entrySet()) {

                int cardId = entry.getKey();
                int amount = entry.getValue();

                Card card = CardLibrary.getCardById(cardId);
                if (card == null) {
                    card = SummonFactory.ALL_SUMMON_CARDS.stream()
                            .filter(s -> s.getId() == cardId)
                            .findFirst()
                            .orElse(null);
                }

                if (card != null) {
                    player.getCardCollection().restoreCard(card, amount);
                }
            }
        }

        // пересобираем активные колоды
        player.getSummonDeck().updateFromCollection(player.getCardCollection());
        player.getCombatDeck().updateFromCollection(player.getCardCollection());
    }



}