package MazeGame;

import javax.swing.*;

public class GameLauncher {

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