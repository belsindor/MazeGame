package MazeGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

public class Inventory implements Serializable{
    private List<Item> items;
    private Map<String, Item> equippedItems;
    private static final long serialVersionUID = 1L;

    public Inventory() {
        this.items = new ArrayList<>();
        this.equippedItems = new HashMap<>();
        equippedItems.put("weapon", null);
        equippedItems.put("верх", null);
        equippedItems.put("низ", null);
    }

    public EquippedItemsData getEquippedItemsData() {
        EquippedItemsData data = new EquippedItemsData();
        data.weapon = equippedItems.get("weapon");
        data.top = equippedItems.get("верх");
        data.bottom = equippedItems.get("низ");
        return data;
    }



    public void setEquippedItems(EquippedItemsData data) {
        equippedItems.put("weapon", data.weapon);
        equippedItems.put("верх", data.top);
        equippedItems.put("низ", data.bottom);
    }



    public void addItem(Item newItem) {
        for (Item item : items) {
            if (item.getName().equals(newItem.getName()) &&
                    item.getCurrentStrength() < item.getStrength()) {
                item.repair();
                return;
            }
        }

        items.add(newItem);
    }

    public void removeItem(Item itemToRemove) {
        items.remove(itemToRemove);
    }

    public boolean equipItem(int index) {
        if (index < 0 || index >= items.size()) {
            return false;
        }

        Item item = items.get(index);
        String type = item.getType();

        if (item instanceof Weapon) {
            Weapon currentWeapon = (Weapon) equippedItems.get("weapon");
            if (currentWeapon != null) {
                addItem(currentWeapon);
            }
            equippedItems.put("weapon", item);
            removeItem(item);
            return true;
        } else if (item instanceof Armor) {
            Armor armor = (Armor) item;
            String armorType = armor.getArmorType();
            Item currentArmor = equippedItems.get(armorType);

            if (currentArmor != null) {
                addItem(currentArmor);
            }
            equippedItems.put(armorType, item);
            removeItem(item);
            return true;
        }

        return false;
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
        if (weapon instanceof Weapon) {
            return ((Weapon) weapon).getAttack();
        }
        return 0;
    }

    public int getTotalDefenseBonus() {
        int defense = 0;

        Item top = equippedItems.get("верх");
        Item bottom = equippedItems.get("низ");

        if (top != null) {
            defense += top.getProtection();
        }
        if (bottom != null) {
            defense += bottom.getProtection();
        }

        return defense;
    }

    public void reduceEquipmentStrength() {
        for (Map.Entry<String, Item> entry : equippedItems.entrySet()) {
            Item item = entry.getValue();
            if (item != null) {
                item.reduceStrength();

                if (item.isBroken()) {
                    System.out.println("Ваш " + item.getName() + " сломался!");
                    equippedItems.put(entry.getKey(), null);
                }
            }
        }
    }

    public List<String> getInventoryDisplay() {
        List<String> display = new ArrayList<>();
        int index = 1;

        for (Item item : items) {
            String displayString;
            if (item instanceof Weapon) {
                displayString = String.format("%d | %s | Атака %d | Прочность %d/%d",
                        index, item.getName(), ((Weapon) item).getAttack(),
                        item.getCurrentStrength(), item.getStrength());
            } else if (item instanceof Armor) {
                Armor armor = (Armor) item;
                displayString = String.format("%d | %s | Защита %d | Прочность %d/%d",
                        index, armor.getName(), armor.getProtection(),
                        armor.getCurrentStrength(), armor.getStrength());
            } else {
                displayString = String.format("%d | %s | Прочность %d/%d",
                        index, item.getName(), item.getCurrentStrength(), item.getStrength());
            }
            display.add(displayString);
            index++;
        }

        return display;
    }

    public List<String> getEquippedItemsInfo() {
        List<String> info = new ArrayList<>();

        Item weapon = equippedItems.get("weapon");
        Item top = equippedItems.get("верх");
        Item bottom = equippedItems.get("низ");

        if (weapon != null) {
            info.add("Оружие: " + weapon.getName() + " (+" + ((Weapon) weapon).getAttack() + " атаки)");
        }
        if (top != null) {
            info.add("Верх: " + top.getName() + " (+" + top.getProtection() + " защиты)");
        }
        if (bottom != null) {
            info.add("Низ: " + bottom.getName() + " (+" + bottom.getProtection() + " защиты)");
        }

        return info;
    }

    // Геттеры
    public List<Item> getItems() { return items; }
    public int getSize() { return items.size(); }
    public boolean isEmpty() { return items.isEmpty(); }

    public Weapon getEquippedWeapon() {
        Item weapon = equippedItems.get("weapon");
        return (weapon instanceof Weapon) ? (Weapon) weapon : null;
    }

    public Armor getEquippedTop() {
        Item top = equippedItems.get("верх");
        return (top instanceof Armor) ? (Armor) top : null;
    }

    public Armor getEquippedBottom() {
        Item bottom = equippedItems.get("низ");
        return (bottom instanceof Armor) ? (Armor) bottom : null;
    }
}