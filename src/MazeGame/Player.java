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

    // Коллекция всех карт игрока (всё, что собрано)
    private final CardCollection cardCollection = new CardCollection();

    // Текущая боевая колода (выбранные карты для боя, без суммонов)
    private CombatDeck combatDeck;

    // Колода суммонов (только карты-призывы)
    private SummonDeck summonDeck;

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

        // Стартовые колоды
        this.combatDeck = CombatDeck.createStarterDeck();
        this.summonDeck = SummonDeck.createStarterDeck();

        // Добавляем стартовые карты в коллекцию
        for (Card card : CardLibrary.starterCombatDeck()) {
            cardCollection.add(card);
        }
        cardCollection.add(MonsterFactory.createStarterSummonCard());
    }

    // BattleUnit методы
    @Override public int getTotalAttack() {
        return baseAttack + temporaryAttack + inventory.getTotalAttackBonus();
    }

    @Override public int getTotalDefense() {
        return baseDefense + temporaryDefense + inventory.getTotalDefenseBonus();
    }

    @Override public int getAttack() { return baseAttack; }
    @Override public int getDefense() { return baseDefense; }
    @Override public void addTemporaryAttack(int value) { temporaryAttack += value; }
    @Override public void addTemporaryDefense(int value) { temporaryDefense += value; }
    @Override public void takeDamage(int damage) {
        int actual = Math.max(1, damage - getTotalDefense());
        health -= actual;
        if (health < 0) health = 0;
    }
    @Override public boolean isAlive() { return health > 0; }
    @Override public String getName() { return name; }
    @Override public UnitType getUnitType() { return unitType; }
    @Override public void setUnitType(UnitType type) { /* игнорируем */ }

    // Логика игры
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

    public SummonDeck getSummonDeck() {
        return summonDeck;
    }

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

    // Геттеры
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getLevel() { return level; }
    public Inventory getInventory() { return inventory; }
    public int getExperience() { return experience; }
    public int getExperienceToNextLevel() { return experienceToNextLevel; }
}