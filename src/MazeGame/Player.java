package MazeGame;

import MazeGame.battle.BattleUnit;

public class Player implements BattleUnit {

    private String name;
    private int health;
    private int maxHealth;
    private int baseAttack;
    private int baseDefense;
    private int experience;
    private int level;
    private int experienceToNextLevel;
    private Inventory inventory;

    private final UnitType unitType = UnitType.INFANTRY;

    public Player(String name) {
        this.name = name;
        this.level = 1;
        this.maxHealth = 15;
        this.health = maxHealth;
        this.baseAttack = 1;
        this.baseDefense = 1;
        this.experience = 0;
        this.experienceToNextLevel = calculateExpToNextLevel(level);
        this.inventory = new Inventory();
    }

    // ===== BattleUnit =====

    @Override
    public int getAttack() {
        return baseAttack;
    }

    @Override
    public int getDefense() {
        return baseDefense;
    }

    @Override
    public int getTotalAttack() {
        return baseAttack + inventory.getTotalAttackBonus();
    }

    @Override
    public int getTotalDefense() {
        return baseDefense + inventory.getTotalDefenseBonus();
    }

    @Override
    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - getTotalDefense());
        health -= actualDamage;
        if (health < 0) health = 0;
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UnitType getUnitType() {
        return unitType;
    }

    // ===== Game logic =====

    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    public void healStep() {
        heal(1);
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

    private int calculateExpToNextLevel(int level) {
        int exp = 100;
        for (int i = 1; i < level; i++) {
            exp = exp * 2 + 100;
        }
        return exp;
    }

    public void loadFromSave(GameSaveData data) {
        this.level = data.level;
        this.experience = data.experience;
        this.health = data.health;
        this.maxHealth = data.maxHealth;

        this.experienceToNextLevel = calculateExpToNextLevel(level);

        this.inventory = new Inventory();
        this.inventory.getItems().addAll(data.inventoryItems);
        this.inventory.setEquippedItems(data.equippedItems);
    }


    // ===== Getters (НЕ из BattleUnit) =====

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getExperienceToNextLevel() { return experienceToNextLevel; }
    public Inventory getInventory() { return inventory; }
}
