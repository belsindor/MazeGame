package MazeGame;

import java.io.BufferedReader;
import java.io.IOException;

public class Player {
    private String name;
    private int health;
    private int maxHealth;
    private int baseAttack;
    private int baseDefense;
    private int experience;
    private int level;
    private int experienceToNextLevel;
    private Inventory inventory;

    public Player(String name) {
        this.name = name;
        this.level = 1;
        this.maxHealth = 15;
        this.health = maxHealth;
        this.baseAttack = 1;
        this.baseDefense = 1;
        this.experience = 0;
        this.experienceToNextLevel = 100;
        this.inventory = new Inventory();
    }

    public int getTotalAttack() {
        return baseAttack + inventory.getTotalAttackBonus();
    }

    public int getTotalDefense() {
        return baseDefense + inventory.getTotalDefenseBonus();
    }

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public void healStep() {
        heal(1);
    }


    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - getTotalDefense());
        health -= actualDamage;
        if (health < 0) health = 0;
    }

    public void gainExperience(int amount) {
        experience += amount;
        while (experience >= experienceToNextLevel) {
            experience -= experienceToNextLevel;
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        maxHealth *= 2;
        health = maxHealth;
        baseAttack++;
        baseDefense++;

        experienceToNextLevel = (experienceToNextLevel * 2) + 100;

        System.out.println("\n══════════════════════════════════════════════");
        System.out.println("   ПОЗДРАВЛЯЕМ! Вы достигли " + level + " уровня!");
        System.out.println("   Здоровье увеличено до " + maxHealth);
        System.out.println("   Атака увеличена до " + baseAttack);
        System.out.println("   Защита увеличена до " + baseDefense);
        System.out.println("══════════════════════════════════════════════");
    }

    public String getStatus() {
        return String.format("Игрок: %s\nУровень: %d\nЗдоровье: %d/%d\nОпыт: %d/%d\nАтака: %d\nЗащита: %d",
                name, level, health, maxHealth, experience, experienceToNextLevel,
                getTotalAttack(), getTotalDefense());
    }

    public String getEquipmentStatus() {
        StringBuilder sb = new StringBuilder("=== ЭКИПИРОВКА ===\n");

        Weapon equippedWeapon = inventory.getEquippedWeapon();
        if (equippedWeapon != null) {
            sb.append("Оружие: ").append(equippedWeapon.getName())
                    .append(" (+").append(equippedWeapon.getAttack()).append(" атаки)\n");
        } else {
            sb.append("Оружие: Нет\n");
        }

        Armor equippedTop = inventory.getEquippedTop();
        if (equippedTop != null) {
            sb.append("Верх: ").append(equippedTop.getName())
                    .append(" (+").append(equippedTop.getProtection()).append(" защиты)\n");
        } else {
            sb.append("Верх: Нет\n");
        }

        Armor equippedBottom = inventory.getEquippedBottom();
        if (equippedBottom != null) {
            sb.append("Низ: ").append(equippedBottom.getName())
                    .append(" (+").append(equippedBottom.getProtection()).append(" защиты)\n");
        } else {
            sb.append("Низ: Нет\n");
        }

        return sb.toString();
    }

    public void loadFromSave(GameSaveData data) {
        this.level = data.level;
        this.experience = data.experience;
        this.health = data.health;
        this.maxHealth = data.maxHealth;

        inventory.getItems().clear();
        inventory.getItems().addAll(data.inventoryItems);
        inventory.setEquippedItems(data.equippedItems);
    }

    public void openInventory(BufferedReader reader) {
        System.out.println("\n=== ИНВЕНТАРЬ ===");

        System.out.println("Надетые предметы:");
        java.util.List<String> equippedInfo = inventory.getEquippedItemsInfo();
        if (equippedInfo.isEmpty()) {
            System.out.println("  (нет надетых предметов)");
        } else {
            for (String info : equippedInfo) {
                System.out.println("  " + info);
            }
        }

        System.out.println("\nПредметы в инвентаре:");
        java.util.List<String> inventoryDisplay = inventory.getInventoryDisplay();
        if (inventoryDisplay.isEmpty()) {
            System.out.println("  (пусто)");
        } else {
            for (String item : inventoryDisplay) {
                System.out.println("  " + item);
            }

            System.out.println("\nКоманды: номер - надеть предмет, Q - выход");
            System.out.print("Выберите предмет для экипировки: ");

            try {
                String input = reader.readLine();
                if (!input.equalsIgnoreCase("q")) {
                    try {
                        int index = Integer.parseInt(input) - 1;
                        if (inventory.equipItem(index)) {
                            System.out.println("✅ Предмет надет!");
                        } else {
                            System.out.println("❌ Не удалось надеть предмет!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Некорректный ввод!");
                    }
                }
            } catch (IOException e) {
                System.out.println("❌ Ошибка ввода: " + e.getMessage());
            }
        }
    }

    // Геттеры и сеттеры
    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getExperience() { return experience; }
    public int getLevel() { return level; }
    public int getExperienceToNextLevel() { return experienceToNextLevel; }
    public Inventory getInventory() { return inventory; }
    public void setHealth(int health) { this.health = health; }
    public boolean isAlive() { return health > 0; }
}