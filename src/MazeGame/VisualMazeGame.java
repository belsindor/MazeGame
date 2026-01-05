package MazeGame;

import javax.swing.*;
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


    private boolean secondMazeLoaded = false;

    public Player getPlayer() {
        return player;
    }


    // ================= КОНСТРУКТОР =================

    public VisualMazeGame(Player player) {
        this.player = player;


        loadMaze(MAZE_1, 4, 3, 27, 9);
    }

    // ================= ЗАГРУЗКА ЛАБИРИНТА =================

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


    // ================= ПОСТРОЕНИЕ КАРТЫ =================

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

    // ================= ТЕКУЩАЯ ЛОКАЦИЯ =================

    public VisualLocation getCurrentLocation() {
        return map[playerY][playerX];
    }

    public String getCurrentImageName() {
        return getCurrentLocation().getImageName();
    }

    // ================= ДВИЖЕНИЕ =================

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

    // ================= ПОСЛЕ ХОДА =================

    private void afterMove() {
        visited[playerY][playerX] = true;


        // === ПРОВЕРКА ВЫХОДА ===
        if (playerX == exitX && playerY == exitY) {

            if (!secondMazeLoaded) {
                HUDMessageManager.show("🚪 Второй лабиринт");
                loadMaze(MAZE_2, 1, 1, 28, 6);
                secondMazeLoaded = true;
            } else {
                HUDMessageManager.show("🏁 Вы нашли выход");
            }

            return;
        }

        checkMonsterAttack();
        checkHeal();
    }

    // ================= ГЕТТЕРЫ =================
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

    // ================= МОНСТРЫ =================

    private void checkMonsterAttack() {
        if (random.nextInt(100) < 10) {

            Monster monster =
                    MonsterFactory.createMonsterForPlayer(player.getLevel());

            HUDMessageManager.show("⚔️ На вас напал " + monster.getName());

            // ⏱️ Пауза перед боем
            new javax.swing.Timer(1000, e -> {

                GameWindow.setBattleActive(true);
                GameWindow.showBattleScreen();

                new BattleWindow(player, monster);

                GameWindow.hideBattleScreen();
                GameWindow.setBattleActive(false);

            }) {{
                setRepeats(false);
                start();
            }};
        }
    }




    // ================= ИНВЕНТАРЬ =================
    public void showInventory() {
        if (!GameWindow.isBattleActive()) {
            new InventoryWindow(player);
        }
    }


    // ================= СПРАВКА =================

    public void showHelp() {
        JOptionPane.showMessageDialog(
                null,
                """
                        Управление:
                        
                        W — север
                        S — юг
                        A — запад
                        D — восток
                        
                        Дополнительно:
                        H — помощь
                        I — инвентарь
                        M — карта
                        
                        Esc - ВЫХОД
                        
                        """,
                "Помощь",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ================= ЛЕЧЕНИЕ =================


    private void checkHeal() {
        if (player.getHealth() < player.getMaxHealth()) {
            player.healStep();
            HUDMessageManager.show("✨ +1 HP");
        }
    }



    // ================= ЛАБИРИНТЫ =================

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
                playerX, playerY,
                secondMazeLoaded ? 28 : 27,
                secondMazeLoaded ? 6 : 9
        );

        // ВАЖНО: восстановление тумана ПОСЛЕ loadMaze
        this.visited = data.visited;
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        Player player;
        VisualMazeGame game;

        GameSaveData data = null;

        if (GameSaveManager.hasSave()) {

            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Обнаружено сохранение.\nЧто вы хотите сделать?",
                    "Maze Game",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Загрузить", "Новая игра", "Выход"},
                    "Загрузить"
            );

            if (choice == 0) {
                data = GameSaveManager.load();
            } else if (choice == 1) {
                data = null;
            } else {
                System.exit(0);
            }
        }

        if (data != null) {
            player = new Player(data.playerName);
            player.loadFromSave(data);
            game = new VisualMazeGame(player);
            game.loadFromSave(data);
        } else {
            String name = JOptionPane.showInputDialog("Введите имя игрока:");
            if (name == null || name.isBlank()) name = "Герой";
            player = new Player(name);
            game = new VisualMazeGame(player);
        }

        new GameWindow(game);
        game.showHelp();
    }
}
