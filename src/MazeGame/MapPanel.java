package MazeGame;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {

    private final VisualMazeGame game;
    private static final int CELL_SIZE = 20;

    public MapPanel(VisualMazeGame game) {
        this.game = game;
        setBackground(Color.BLACK);
    }

    @Override
    public Dimension getPreferredSize() {
        int[][] maze = game.getCurrentMaze();
        return new Dimension(
                maze[0].length * CELL_SIZE,
                maze.length * CELL_SIZE
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int[][] maze = game.getCurrentMaze();
        boolean[][] visited = game.getVisited();

        int px = game.getPlayerX();
        int py = game.getPlayerY();

        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[0].length; x++) {

                int drawX = x * CELL_SIZE;
                int drawY = y * CELL_SIZE;

                // Стены
                if (maze[y][x] == 0) {
                    g.setColor(Color.BLACK);
                    g.fillRect(drawX, drawY, CELL_SIZE, CELL_SIZE);
                    continue;
                }

                // Туман войны
                if (!visited[y][x]) {
                    g.setColor(Color.BLACK);
                    g.fillRect(drawX, drawY, CELL_SIZE, CELL_SIZE);
                    continue;
                }

                // Открытая клетка
                g.setColor(new Color(70, 70, 70));
                g.fillRect(drawX, drawY, CELL_SIZE, CELL_SIZE);

                g.setColor(Color.BLACK);
                g.drawRect(drawX, drawY, CELL_SIZE, CELL_SIZE);
            }
        }

        // Игрок
        g.setColor(Color.GREEN);
        g.fillOval(
                px * CELL_SIZE + 4,
                py * CELL_SIZE + 4,
                CELL_SIZE - 8,
                CELL_SIZE - 8
        );
    }
}


