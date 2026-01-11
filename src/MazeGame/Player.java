package MazeGame;

import MazeGame.battle.BattleUnit;

import MazeGame.cards.*;

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
    private int temporaryAttack = 0;
    private int temporaryDefense = 0;

    // 🃏 КОЛОДЫ
    private final CombatDeck combatDeck;
    private final SummonDeck summonDeck;

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
        // ⚔ стартовые колоды
        this.combatDeck = CombatDeck.createStarterDeck();
        this.summonDeck = SummonDeck.createStarterDeck();
    }

    // ===== BattleUnit =====

    @Override
    public void addTemporaryAttack(int value) {
        temporaryAttack += value;
    }

    @Override
    public void addTemporaryDefense(int value) {
        temporaryDefense += value;
    }

    @Override
    public int getTotalAttack() {
        return baseAttack + temporaryAttack + inventory.getTotalAttackBonus();
    }

    @Override
    public int getTotalDefense() {
        return baseDefense + temporaryDefense + inventory.getTotalDefenseBonus();
    }

    @Override
    public int getAttack() {
        return baseAttack;
    }

    @Override
    public int getDefense() {
        return baseDefense;
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

    @Override
    public void setUnitType(UnitType type) {
        // Игрок всегда пехотинец, можно игнорировать
    }
    // ===== GAME LOGIC =====

    public void loadFromSave(GameSaveData data) {
        this.health = data.health;
        this.maxHealth = data.maxHealth;
        this.level = data.level;
        this.experience = data.experience;
        this.experienceToNextLevel =
                calculateExpToNextLevel(level);

        this.inventory.loadFromData(data.equippedItems,
                data.inventoryItems);
    }


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
        maxHealth += 5;
        health = maxHealth;
        baseAttack++;
        baseDefense++;
        experienceToNextLevel = calculateExpToNextLevel(level);
    }

    private int calculateExpToNextLevel(int level) {
        int exp = 100;
        for (int i = 1; i < level; i++) {
            exp = exp * 2 + 100;
        }
        return exp;
    }

    // ===== КОЛОДЫ =====

    public CombatDeck getCombatDeck() {
        return combatDeck;
    }

    public SummonDeck getSummonDeck() {
        return summonDeck;
    }

    public SummonCard chooseSummonCard() {
        return summonDeck.draw(); // или getRandom(), если так называется
    }

    // ===== GETTERS =====

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }

    @Override
    public int getLevel() { return level; }
    public Inventory getInventory() { return inventory; }
    public int getExperience() {
        return experience;
    }

    public int getExperienceToNextLevel() {
        return experienceToNextLevel;
    }
}
