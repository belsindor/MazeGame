package MazeGame;

import MazeGame.item.EquippedItemsData;

import java.io.*;

public class GameSaveManager {

    public EquippedItemsData equippedItems;
    private static final String SAVE_FILE = "save.dat";
    // ===== КАРТЫ =====
//    public List<CardSaveData> allCards;
//    public List<CardSaveData> combatDeck;
//    public CardSaveData selectedSummon;


    public static void save(VisualMazeGame game) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {

            GameSaveData data = new GameSaveData();

            Player p = game.getPlayer();

            // ===== игрок =====
            data.playerName = p.getName();
            data.level = p.getLevel();
            data.experience = p.getExperience();
            data.health = p.getHealth();
            data.maxHealth = p.getMaxHealth();

            // ===== инвентарь =====
            data.inventoryItems = p.getInventory().getItems();
            data.equippedItems = p.getInventory().getEquippedItemsData();


            // ===== карта =====
            data.playerX = game.getPlayerX();
            data.playerY = game.getPlayerY();
            data.secondMazeLoaded = game.isSecondMazeLoaded();

            // ===== туман войны =====
            data.visited = game.getVisited();

            out.writeObject(data);

            GameWindow.log("💾 Игра сохранена");

        } catch (IOException e) {
            e.printStackTrace();
            GameWindow.log("❌ Ошибка сохранения");
        }
    }

    public static boolean hasSave() {
        return new File("save.dat").exists();
    }


    public static GameSaveData load() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(SAVE_FILE))) {

            return (GameSaveData) in.readObject();

        } catch (Exception e) {
            return null;
        }
    }
}
