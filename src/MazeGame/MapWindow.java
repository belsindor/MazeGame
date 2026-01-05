package MazeGame;

import javax.swing.*;

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
    }
}

