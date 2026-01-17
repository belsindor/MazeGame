package MazeGame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;

public class GameKeyListener extends KeyAdapter {

    private final VisualMazeGame game;
    private final GamePanel panel;
    private final GameWindow window;

    // Статическая переменная — хранит текущее открытое окно карты
    private static MapWindow currentMapWindow = null;

    public GameKeyListener(VisualMazeGame game, GamePanel panel, GameWindow window) {
        this.game = game;
        this.panel = panel;
        this.window = window;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (GameWindow.isBattleActive()) {
            if (e.getKeyCode() != KeyEvent.VK_ESCAPE) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> game.moveNorth();
            case KeyEvent.VK_S -> game.moveSouth();
            case KeyEvent.VK_A -> game.moveWest();
            case KeyEvent.VK_D -> game.moveEast();
            case KeyEvent.VK_H -> game.showHelp();

            case KeyEvent.VK_M -> {
                if (!GameWindow.isBattleActive()) {
                    toggleMapWindow();
                }
            }

            case KeyEvent.VK_ESCAPE -> window.exitGame();
        }

        panel.setImage(game.getCurrentImageName());
    }

    /**
     * Переключает состояние окна карты:
     *  - если открыто → закрывает
     *  - если закрыто → открывает новое
     */


    private void toggleMapWindow() {
        if (currentMapWindow != null && currentMapWindow.isShowing()) {
            currentMapWindow.dispose();
            currentMapWindow = null;
            window.requestFocus(); // ← возвращаем фокус на игру
        } else {
            currentMapWindow = new MapWindow(game);
            currentMapWindow.setVisible(true);
        }
    }
}