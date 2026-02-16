package MazeGame;

import javax.swing.*;
import java.awt.*;

public class StartScreen extends JFrame {

    public StartScreen() {
        setTitle("Maze Game");
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // на весь экран
        setLocationRelativeTo(null);

        // Фоновая панель с картинкой
        BackgroundPanel background = new BackgroundPanel("/start.jpg");
        background.setLayout(new GridBagLayout());
        setContentPane(background);

        // Панель кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton newGame = createButton("Новая игра");
        JButton loadGame = createButton("Загрузить игру");
        JButton about = createButton("Об игре");
        JButton help = createButton("Помощь");
        JButton exit = createButton("Выход");

        buttonPanel.add(newGame);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(loadGame);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(about);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(help);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(exit);

        background.add(buttonPanel);

        // === ЛОГИКА КНОПОК ===

        newGame.addActionListener(e -> {

            startNewGame();
            dispose();
        });

        loadGame.addActionListener(e -> {
            loadGame();
            dispose();
        });

        about.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Maze Game\nВерсия 0.4\n\nRPG by Dorinem",
                        "Об игре",
                        JOptionPane.INFORMATION_MESSAGE)
        );

        help.addActionListener(e -> {
            showHelp();

        });
        exit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 50));
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        return button;
    }

    private void startNewGame() {
        String name = JOptionPane.showInputDialog(
                null,
                "Введите имя игрока:",
                "Новая игра",
                JOptionPane.QUESTION_MESSAGE
        );

        if (name == null || name.trim().isEmpty()) {
            name = "Герой";
        }

        Player player = new Player(name);
        VisualMazeGame game = new VisualMazeGame(player);
        new GameWindow(game);
        game.showHelp();
    }

    private void loadGame() {
        if (!GameSaveManager.hasSave()) {
            JOptionPane.showMessageDialog(this,
                    "Сохранение не найдено!",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        GameSaveData data = GameSaveManager.load();

        Player player = new Player(data.playerName);
        player.loadFromSave(data);

        VisualMazeGame game = new VisualMazeGame(player);
        game.loadFromSave(data);

        new GameWindow(game);
        game.showHelp();
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
}
