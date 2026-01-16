package MazeGame;

import MazeGame.item.EquippedItemsData;
import MazeGame.item.Item;

import java.io.Serializable;
import java.util.List;

public class GameSaveData implements Serializable {
    private static final long serialVersionUID = 1L;
    // ===== ИГРОК =====
    public String playerName;
    public int level;
    public int experience;
    public int health;
    public int maxHealth;

    // ===== ИНВЕНТАРЬ =====
    public List<Item> inventoryItems;
    public EquippedItemsData equippedItems;

    // ===== КАРТА =====
    public int playerX;
    public int playerY;
    public boolean secondMazeLoaded;
    public boolean[][] visited;
    public boolean[][] fogOfWar;
    public int currentMazeId;
    // добавить сохранение колоды
}
