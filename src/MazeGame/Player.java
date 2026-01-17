package MazeGame;

import MazeGame.battle.BattleResult;
import MazeGame.battle.BattleReward;
import MazeGame.cards.*;
import MazeGame.item.Item;

import java.util.ArrayList;
import java.util.List;

//+
public class Player {


    private final String name;
    private int health;
    private int maxHealth;
    private int baseAttack;
    private int baseDefense;
    private int experience;
    private int level;
    private int experienceToNextLevel;
    private final Inventory inventory;

    private int temporaryAttack = 0;
    private int temporaryDefense = 0;

    // Коллекция всех карт игрока (всё, что собрано)
    private final CardCollection cardCollection = new CardCollection();
    private final SummonDeck summonDeck = new SummonDeck();
    private final List<Item> items = new ArrayList<>();

    // Текущая боевая колода (выбранные карты для боя, без суммонов)
    private CombatDeck combatDeck;

    // Колода суммонов (только карты-призывы)
    private List<SummonCard> ALL_SUMMON_CARDS;

    private final UnitType unitType = UnitType.INFANTRY;

    public Player(String name) {
        this.name = name;
        this.level = 1;
        this.maxHealth = 20;
        this.health = maxHealth;
        this.baseAttack = 3;
        this.baseDefense = 1;
        this.experience = 0;
        this.experienceToNextLevel = calculateExpToNextLevel(level);
        this.inventory = new Inventory();



    }


public int getTotalAttack() {
        return baseAttack + temporaryAttack + inventory.getTotalAttackBonus();
    }

public int getTotalDefense() {
        return baseDefense + temporaryDefense + inventory.getTotalDefenseBonus();
    }

public int getAttack() { return baseAttack; }
public int getDefense() { return baseDefense; }
public void addTemporaryAttack(int value) { temporaryAttack += value; }
public void addTemporaryDefense(int value) { temporaryDefense += value; }
public void takeDamage(int damage) {
        int actual = Math.max(1, damage - getTotalDefense());
        health -= actual;
        if (health < 0) health = 0;
    }
public boolean isAlive() { return health > 0; }
public String getName() { return name; }
public UnitType getUnitType() { return unitType; }

    public void setUnitType(UnitType type) {
        throw new UnsupportedOperationException("Игрок не может менять свой тип юнита");
    }

    public void clearTemporaryEffects() {
        temporaryAttack = 0;
        temporaryDefense = 0;
    }

    // Логика игры
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
        maxHealth += 6;
        health = maxHealth;
        baseAttack += 1;
        baseDefense += 1;
        experienceToNextLevel = calculateExpToNextLevel(level);
        HUDMessageManager.showInfo("↑ Уровень повышен! Теперь " + level + " уровень");
    }

    private int calculateExpToNextLevel(int lvl) {
        return 80 + (lvl * lvl * 50);
    }

    // Коллекции и колоды
    public CardCollection getCardCollection() {
        return cardCollection;
    }

    public CombatDeck getCombatDeck() {
        return combatDeck;
    }

//    public SummonCard getSummonDeck() {
//        return List <SummonCard> ALL_SUMMON_CARDS;
//    }

    public void setCombatDeck(CombatDeck newDeck) {
        this.combatDeck = newDeck;
    }

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

    public void processBattleReward(BattleResult result) {
        if (!result.isPlayerWin()) {
            return;
        }

        // Опыт и предметы
        BattleReward reward = result.getReward();
        gainExperience(reward.experience());
        for (Item item : reward.items()) {
            inventory.addItem(item);
        }


    }

    public List<Item> getItems() {
        return new ArrayList<>(items); // защитная копия
    }

    public void addItem(Item item) {
        if (item != null) {
            items.add(item);
        }
    }
    // Геттеры


    public SummonDeck getSummonDeck() {
        return summonDeck;
    }

    public int getHealth() { return health; }

    public int getMaxHealth() { return maxHealth; }

    public int getLevel() { return level;}

    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }


    public Inventory getInventory() { return inventory; }
    public int getExperience() { return experience; }
    public int getExperienceToNextLevel() { return experienceToNextLevel; }
}