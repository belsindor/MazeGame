package MazeGame;

import MazeGame.cards.CardCollection;
import MazeGame.cards.DeckCollectionWindow;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;

public class GameKeyListener extends KeyAdapter {

    private final VisualMazeGame game;
    private final GamePanel panel;
    private final GameWindow window;

    // Добавляем ссылки на открытые окна
    private static InventoryWindow inventoryWindow = null;
    private static DeckCollectionWindow deckWindow = null;
    private static MapWindow mapWindow = null;

    public GameKeyListener(VisualMazeGame game, GamePanel panel, GameWindow window) {
        this.game = game;
        this.panel = panel;
        this.window = window;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (GameWindow.isBattleActive()) {
            // Блокировка всех клавиш во время боя (кроме ESC, если нужно)
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
            case KeyEvent.VK_M -> toggleMapWindow();
            case KeyEvent.VK_ESCAPE -> window.exitGame();
            case KeyEvent.VK_I -> toggleInventoryWindow();
            case KeyEvent.VK_C -> toggleDeckWindow();
        }

        panel.setImage(game.getCurrentImageName());
    }

    // Методы для переключения окон
    private void toggleInventoryWindow() {
        if (GameWindow.isBattleActive()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        if (inventoryWindow != null && inventoryWindow.isVisible()) {
            inventoryWindow.dispose();
            inventoryWindow = null;
        } else {
            inventoryWindow = new InventoryWindow(game.getPlayer());
            // Добавляем слушатель для закрытия окна
            inventoryWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    inventoryWindow = null;
                }
            });
        }
    }

    private void toggleDeckWindow() {
        if (GameWindow.isBattleActive()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        CardCollection collection = game.getPlayer().getCardCollection();
        if (collection == null || collection.isEmpty()) {
            HUDMessageManager.show("Коллекция карт пуста", new Color(255, 180, 100), 22);
            return;
        }

        if (deckWindow != null && deckWindow.isVisible()) {
            deckWindow.dispose();
            deckWindow = null;
        } else {
            deckWindow = new DeckCollectionWindow(collection);
            // Добавляем слушатель для закрытия окна
            deckWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    deckWindow = null;
                }
            });
        }
    }

    private void toggleMapWindow() {
        if (GameWindow.isBattleActive()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        if (mapWindow != null && mapWindow.isVisible()) {
            mapWindow.dispose();
            mapWindow = null;
        } else {
            mapWindow = new MapWindow(game);
            // Добавляем слушатель для закрытия окна
            mapWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    mapWindow = null;
                }
            });
        }
    }
}