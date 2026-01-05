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