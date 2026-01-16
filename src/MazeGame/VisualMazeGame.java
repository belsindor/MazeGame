package MazeGame;

import MazeGame.battle.*;
import MazeGame.cards.SummonCard;
import MazeGame.cards.SummonSelectionWindow;
import MazeGame.item.Item;

import javax.swing.*;
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

    private static final int MONSTER_APPEARANCE_CHANCE = 100; // шанс появления монстра после хода
    private static final int HEAL_PER_STEP = 1;

    private boolean secondMazeLoaded = false;

    public VisualMazeGame(Player player) {
        this.player = player;
        loadMaze(MAZE_1, 4, 3, 27, 9);
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
        return getCurrentLocation().getImageName();
    }

    // ДВИЖЕНИЕ
    public void moveNorth() {
        if (playerY > 0 && getCurrentLocation().hasNorth()) {
            playerY--;
            afterMove();
        }
    }

    public void moveSouth() {
        if (playerY < map.length - 1 && getCurrentLocation().hasSouth()) {
            playerY++;
            afterMove();
        }
    }

    public void moveWest() {
        if (playerX > 0 && getCurrentLocation().hasWest()) {
            playerX--;
            afterMove();
        }
    }

    public void moveEast() {
        if (playerX < map[0].length - 1 && getCurrentLocation().hasEast()) {
            playerX++;
            afterMove();
        }
    }

    private void afterMove() {
        visited[playerY][playerX] = true;
        GameWindow.getPanel().clearPendingMonster();

        // Проверка выхода
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
            Monster enemy = MonsterFactory.createEnemyForPlayer(player.getLevel());
            GameWindow.getPanel().showPendingMonster(enemy);
        }
    }

    private void checkHeal() {
        if (player.getHealth() < player.getMaxHealth()) {
            player.healStep();
            HUDMessageManager.showHeal("✨ +1 HP");
        }
    }

    public void startBattle(Monster monster) {

        // --- подготовка экрана ---
        GameWindow.getPanel().clearPendingMonster();
        GameWindow.setBattleActive(true);
        GameWindow.showBattleScreen();

        // --- выбор суммона (обязательный) ---
        SummonSelectionWindow window =
                new SummonSelectionWindow(GameState.get().summons().getAll());

        Optional<SummonCard> selected = window.showAndWait();

        if (selected.isEmpty()) {
            // если игрок закрыл окно — отменяем бой
            GameWindow.hideBattleScreen();
            GameWindow.setBattleActive(false);
            return;
        }

        // --- создаём БОЕВОГО суммона ---
        BattleSummon battleSummon = new BattleSummon(selected.get());

        // --- создаём контекст боя ---
        BattleContext context = new BattleContext(player, monster);
        context.setSummon(battleSummon);

        // --- создаём окно боя ---
        BattleWindow bw = new BattleWindow(
                context,
                player,
                monster,
                battleSummon
        );
        bw.setVisible(true);

        // --- выход из боя ---
        GameWindow.hideBattleScreen();
        GameWindow.setBattleActive(false);

        // --- обработка результата ---
        BattleOutcome outcome = bw.getOutcome();

        if (outcome == BattleOutcome.PLAYER_LOSE) {
            JOptionPane.showMessageDialog(null, "Вы погибли...");
            System.exit(0);
        }

        BattleResult result = bw.getResult();
        if (result != null && result.getReward() != null) {

            BattleReward reward = result.getReward();

            player.gainExperience(reward.experience());
            HUDMessageManager.showInfo("✨ Получено опыта: +" + reward.experience());

            for (Item item : reward.items()) {
                player.getInventory().addItem(item);
                HUDMessageManager.showInfo("🎁 Найден предмет: " + item.getName());
            }
        }
    }


    // Геттеры
    public Player getPlayer() {
        return player;
    }

    public boolean[][] getVisited() {
        return visited;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public int[][] getCurrentMaze() {
        return currentMaze;
    }

    public boolean isSecondMazeLoaded() {
        return secondMazeLoaded;
    }

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