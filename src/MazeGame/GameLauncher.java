package MazeGame;

import MazeGame.cards.SummonFactory;

import javax.swing.*;

public class GameLauncher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Player player;
            VisualMazeGame game;
            GameSaveData data = null;

            // Проверяем наличие сохранения
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
                // Загрузка из сохранения
                player = new Player(data.playerName);
                player.loadFromSave(data);
                game = new VisualMazeGame(player);
                game.loadFromSave(data);
            } else {
                // Новая игра
                String name = JOptionPane.showInputDialog(null,
                        "Введите имя игрока:",
                        "Новая игра",
                        JOptionPane.QUESTION_MESSAGE);

                if (name == null || name.trim().isEmpty()) {
                    name = "Герой";
                }

                player = new Player(name);
                game = new VisualMazeGame(player);
            }
            player.getCardCollection().addCard(SummonFactory.ancestor_spirit());
            // Создаём главное окно и передаём ему игру
            GameWindow window = new GameWindow(game);

            // Показываем помощь
            game.showHelp();
        });
    }
}