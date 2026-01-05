package MazeGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;



    public class GameWindow extends JFrame {

        private static GamePanel panel;
        private static VisualMazeGame game;
        private static JTextArea logArea;

        private static boolean battleActive = false;

        public GameWindow(VisualMazeGame gameInstance) {

            game = gameInstance;

            setTitle("Maze Game");
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setLayout(new BorderLayout());
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

            panel = new GamePanel(game.getPlayer());
            panel.setImage(game.getCurrentImageName());
            add(panel, BorderLayout.CENTER);

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    exitGame();
                }
            });


            logArea = new JTextArea();
            logArea.setEditable(false);
            logArea.setBackground(new Color(20, 20, 20));
            logArea.setForeground(Color.WHITE);
            add(new JScrollPane(logArea), BorderLayout.SOUTH);

            // ❗ ОДИН слушатель клавиатуры
            addKeyListener(new GameKeyListener(game, panel, this));

            setFocusable(true);
            setVisible(true);
        }


        public void exitGame() {

        if (battleActive) {
            JOptionPane.showMessageDialog(
                    this,
                    "Нельзя выйти во время боя!",
                    "Бой",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "Сохранить игру перед выходом?",
                "Выход из игры",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        if (result == JOptionPane.CANCEL_OPTION) return;

        if (result == JOptionPane.YES_OPTION) {
            GameSaveManager.save(game);
        }

        System.exit(0);
    }



    // ===== ЛОГ =====
    public static void log(String message) {
        if (logArea == null) return;

        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    // ===== БОЙ =====
    public static void setBattleActive(boolean value) {
        battleActive = value;
    }

    public static boolean isBattleActive() {
        return battleActive;
    }

    public static void showBattleScreen() {
        panel.setImage("battle.jpg");
    }

    public static void hideBattleScreen() {
        panel.setImage(game.getCurrentImageName());
    }
}
