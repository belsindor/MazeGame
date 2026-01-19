package MazeGame;

import MazeGame.item.Armor;
import MazeGame.item.EquippedItemsData;
import MazeGame.item.Item;
import MazeGame.item.Weapon;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

public class Inventory implements Serializable {
    private final List<Item> items = new ArrayList<>();
    private final Map<String, Item> equippedItems = new HashMap<>();

    @Serial
    private static final long serialVersionUID = 1L;

    public Inventory() {
        equippedItems.put("weapon", null);
        equippedItems.put("верх", null);
        equippedItems.put("низ", null);
        equippedItems.put("shield", null);
    }

    /**
     * Добавляем предмет в инвентарь — всегда новый экземпляр
     */
    public void addItem(Item newItem) {
        if (newItem == null) return;

        items.add(newItem);
        System.out.println("Добавлен предмет в инвентарь: " + newItem.getName() + " (всего предметов: " + items.size() + ")");
    }

    public void removeItem(Item itemToRemove) {
        items.remove(itemToRemove);
    }

    /**
     * Ремонт всех предметов (вызывать после отдыха или вручную)
     */
    public void repairAllItems() {
        for (Item item : items) {
            if (item.getCurrentStrength() < item.getStrength()) {
                item.repair();
            }
        }
    }

    public void equipItem(int index) {
        if (index < 0 || index >= items.size()) return;

        Item item = items.get(index);
        String type = item.getType();

        if (item instanceof Weapon) {
            Weapon current = (Weapon) equippedItems.get("weapon");
            if (current != null) {
                addItem(current); // возвращаем старое в инвентарь
            }
            equippedItems.put("weapon", item);
            removeItem(item);
        } else if (item instanceof Armor armor) {
            String armorType = armor.getArmorType();
            Item current = equippedItems.get(armorType);
            if (current != null) {
                addItem(current);
            }
            equippedItems.put(armorType, item);
            removeItem(item);
        }
    }

    public void unequipItem(String type) {
        Item item = equippedItems.get(type);
        if (item != null) {
            addItem(item);
            equippedItems.put(type, null);
        }
    }

    public int getTotalAttackBonus() {
        Item weapon = equippedItems.get("weapon");
        return (weapon instanceof Weapon) ? ((Weapon) weapon).getAttack() : 0;
    }

    public int getTotalDefenseBonus() {
        int defense = 0;
        Item top = equippedItems.get("верх");
        Item bottom = equippedItems.get("низ");
        Item shield = equippedItems.get("shield");

        if (top instanceof Armor) defense += ((Armor) top).getProtection();
        if (bottom instanceof Armor) defense += ((Armor) bottom).getProtection();
        if (shield instanceof Armor) defense += ((Armor) shield).getProtection();
        return defense;
    }

    public void reduceEquipmentStrength() {
        for (Map.Entry<String, Item> entry : equippedItems.entrySet()) {
            Item item = entry.getValue();
            if (item != null) {
                item.reduceStrength();
                if (item.isBroken()) {
                    HUDMessageManager.showInfo("Ваш " + item.getName() + " сломался!");
                    equippedItems.put(entry.getKey(), null);
                }
            }
        }
    }

    // Геттеры
    public List<Item> getItems() {
        return new ArrayList<>(items); // возвращаем копию
    }

    public int getSize() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public Weapon getEquippedWeapon() {
        Item w = equippedItems.get("weapon");
        return (w instanceof Weapon) ? (Weapon) w : null;
    }

    public Armor getEquippedTop() {
        Item t = equippedItems.get("верх");
        return (t instanceof Armor) ? (Armor) t : null;
    }

    public Armor getEquippedBottom() {
        Item b = equippedItems.get("низ");
        return (b instanceof Armor) ? (Armor) b : null;
    }

    public Armor getEquippedShield() {
        Item s = equippedItems.get("shield");
        return (s instanceof Armor) ? (Armor) s : null;
    }

    public void loadFromData(EquippedItemsData equipped, List<Item> items) {
        this.items.clear();
        this.items.addAll(items);
        setEquippedItems(equipped);
    }

    public EquippedItemsData getEquippedItemsData() {
        EquippedItemsData data = new EquippedItemsData();
        data.weapon = equippedItems.get("weapon");
        data.top = equippedItems.get("верх");
        data.bottom = equippedItems.get("низ");
        data.shield = equippedItems.get("shield");
        return data;
    }

    public void setEquippedItems(EquippedItemsData data) {
        equippedItems.put("weapon", data.weapon);
        equippedItems.put("верх", data.top);
        equippedItems.put("низ", data.bottom);
        equippedItems.put("shield", data.shield);
    }
}