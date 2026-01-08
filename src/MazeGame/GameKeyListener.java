package MazeGame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;



public class GameKeyListener extends KeyAdapter {

    private final VisualMazeGame game;
    private final GamePanel panel;
    private final GameWindow window;

    public GameKeyListener(VisualMazeGame game, GamePanel panel, GameWindow window) {
        this.game = game;
        this.panel = panel;
        this.window = window;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> game.moveNorth();
            case KeyEvent.VK_S -> game.moveSouth();
            case KeyEvent.VK_A -> game.moveWest();
            case KeyEvent.VK_D -> game.moveEast();
            case KeyEvent.VK_H -> game.showHelp();
            case KeyEvent.VK_M -> {
                if (!GameWindow.isBattleActive()) {
                    new MapWindow(game);
                }
            }
            case KeyEvent.VK_ESCAPE -> window.exitGame();

            case KeyEvent.VK_I -> {
                if (GameWindow.isBattleActive()) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                new InventoryWindow(game.getPlayer());
            }
        }
// надо добавить кнопку колоды например C
        panel.setImage(game.getCurrentImageName());
    }
}