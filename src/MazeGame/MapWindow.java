package MazeGame;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//+
public class MapWindow extends JFrame {

    public MapWindow(VisualMazeGame game) {
        setTitle("Карта");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        MapPanel panel = new MapPanel(game);
        setContentPane(panel);

        pack(); // 🔥 КЛЮЧЕВО: окно = размер карты
        setLocationRelativeTo(null);
        setResizable(false);

        setVisible(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_M || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }
}

