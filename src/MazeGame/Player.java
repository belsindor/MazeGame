package MazeGame;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleUnit;
import MazeGame.battle.BattleResult;
import MazeGame.battle.BattleReward;
import MazeGame.cards.*;
import MazeGame.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Player implements BattleUnit {

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
    private boolean hasUsedStartingSummon = false;
    private SummonCard startingSummon;

    // Текущая боевая колода (выбранные карты для боя, без суммонов)
    private CombatDeck combatDeck;

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
        this.combatDeck = new CombatDeck();
    }

    // ===== Реализация интерфейса BattleUnit =====

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UnitType getUnitType() {
        return unitType;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setUnitType(UnitType type) {
        throw new UnsupportedOperationException("Игрок не может менять свой тип юнита");
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
    public int getTotalAttack() {
        return baseAttack + temporaryAttack + inventory.getTotalAttackBonus();
    }

    @Override
    public int getTotalDefense() {
        return baseDefense + temporaryDefense + inventory.getTotalDefenseBonus();
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void takeDamage(int damage) {
        int actual = Math.max(1, damage - getTotalDefense());
        health -= actual;
        if (health < 0) health = 0;
    }

    @Override
    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    @Override
    public void addTemporaryAttack(int value) {
        temporaryAttack += value;
    }

    @Override
    public void addTemporaryDefense(int value) {
        temporaryDefense += value;
    }

    @Override
    public void clearTemporaryEffects() {
        temporaryAttack = 0;
        temporaryDefense = 0;
    }

    @Override
    public void onTurnStart(BattleContext ctx) {
        BattleUnit.super.onTurnStart(ctx);
    }

    @Override
    public void onTurnEnd(BattleContext ctx) {
        BattleUnit.super.onTurnEnd(ctx);
    }

    @Override
    public void onBattleEnd(BattleContext ctx) {
        BattleUnit.super.onBattleEnd(ctx);
    }

    @Override
    public int getLevel() {
        return level;
    }

    public boolean hasUsedStartingSummon() {
        return hasUsedStartingSummon;
    }

    public void markStartingSummonUsed() {
        this.hasUsedStartingSummon = true;
    }

    // ===== Остальные методы (без изменений) =====

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

    public void processDrop(List<CardDropService.DropEntry> drops) {
        if (drops == null || drops.isEmpty()) return;

        CardCollection cardCollection = getCardCollection();
        SummonDeck summonDeck = getSummonDeck();
        CombatDeck combatDeck = getCombatDeck();  // если он есть в Player

        for (CardDropService.DropEntry entry : drops) {
            if (entry.getSummonCard() != null) {
                SummonCard summon = entry.getSummonCard();
                cardCollection.addCard(summon);           // → regularCards
                summonDeck.addSummon(summon);             // → active по типу
                System.out.println("Дроп суммона: " + summon.getName());
            }
            else if (entry.getCard() != null) {
                Card card = entry.getCard();
                cardCollection.addCard(card);             // → regularCards
                combatDeck.addCard(card);                 // → combat по эффекту (если это боевая карта)
                System.out.println("Дроп карты: " + card.getId());
            }
            else if (entry.getItem() != null) {
                inventory.addItem(entry.getItem());       // → ВНИМАНИЕ: inventory.addItem!
                System.out.println("Дроп предмета: " + entry.getItem().getName());
            }
        }

        // После добавления всех карт обновляем колоды
        summonDeck.updateFromCollection(cardCollection);
        combatDeck.updateFromCollection(cardCollection);  // если метод есть
        System.out.println("После дропа в инвентаре предметов: " + getInventory().getSize());
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

    public void setCombatDeck(CombatDeck newDeck) {
        this.combatDeck = newDeck;
    }

    public SummonDeck getSummonDeck() {
        return summonDeck;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getExperience() {
        return experience;
    }

    public int getExperienceToNextLevel() {
        return experienceToNextLevel;
    }

    public void loadFromSave(GameSaveData data) {
        this.health = data.health;
        this.maxHealth = data.maxHealth;
        this.level = data.level;
        this.experience = data.experience;
        this.experienceToNextLevel = calculateExpToNextLevel(level);

        this.inventory.loadFromData(data.equippedItems, data.inventoryItems);
    }

    public void processBattleReward(BattleResult result) {
        if (!result.isPlayerWin()) {
            return;
        }

        BattleReward reward = result.getReward();
        gainExperience(reward.experience());
        for (Item item : reward.items()) {
            inventory.addItem(item);
        }
    }
}