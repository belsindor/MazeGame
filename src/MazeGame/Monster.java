package MazeGame;

import MazeGame.battle.BattleUnit;

public class Monster implements BattleUnit {

    private final String name;
    private final int level;

    private int health;
    private final int maxHealth;

    private final int attack;
    private final int defense;

    private int temporaryAttack = 0;
    private int temporaryDefense = 0;

    private UnitType unitType;
    private final boolean immortal;

    // ===== КОНСТРУКТОРЫ =====

    public Monster(
            String name,
            int level,
            int health,
            int attack,
            int defense,
            UnitType unitType,
            boolean immortal
    ) {
        this.name = name;
        this.level = level;
        this.maxHealth = health;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.unitType = unitType;
        this.immortal = immortal;
    }

    public Monster(
            String name,
            int level,
            int health,
            int attack,
            int defense,
            UnitType unitType
    ) {
        this(name, level, health, attack, defense, unitType, false);
    }
    public Monster(MonsterTemplate template) {
        this(template.name(), template.level(), template.maxHealth(), template.attack(), template.defense(), template.unitType(), false);
    }

    // ===== ФАБРИКИ =====

    public Monster createWeakened(double factor) {
        return new Monster(
                name,
                level,
                (int) Math.max(1, maxHealth * factor),
                (int) Math.max(1, attack * factor),
                (int) Math.max(0, defense * factor),
                unitType,
                immortal
        );
    }

    public Monster createSummon() {
        return new Monster(
                name,
                level,
                (int) (maxHealth * 0.5),
                (int) (attack * 0.5),
                defense,
                unitType,
                true // 👻 бессмертен
        );
    }

    // ===== BattleUnit =====

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public int getDefense() {
        return defense;
    }

    @Override
    public int getTotalAttack() {
        return Math.max(0, attack + temporaryAttack);
    }

    @Override
    public int getTotalDefense() {
        return Math.max(0, defense + temporaryDefense);
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
    public void takeDamage(int damage) {
        if (immortal) return;
        health -= damage;
        if (health < 0) health = 0;
    }

    @Override
    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    @Override
    public boolean isAlive() {
        return immortal || health > 0;
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
        this.unitType = type;
    }

    public void revive() {
        if (isAlive()) return;

        this.health = Math.max(1, maxHealth / 2);
    }


    // ===== ДОП =====

    public int getLevel() {
        return level;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isImmortal() {
        return immortal;
    }
}
