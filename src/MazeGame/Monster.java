package MazeGame;

import MazeGame.battle.BattleUnit;

public class Monster implements BattleUnit {

    private final String name;
    private final int level;
    private int health;
    private final int maxHealth;
    private final int attack;
    private final int defense;
    private final UnitType unitType;
    private final boolean immortal;

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
    public Monster(MonsterTemplate t) {
        this(
                t.name(),
                t.level(),
                t.maxHealth(),
                t.attack(),
                t.defense(),
                t.unitType()
        );
    }

    // ===== ГЕТТЕРЫ =====

    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }

    @Override
    public int getTotalAttack() {
        return attack;
    }

    @Override
    public int getTotalDefense() {
        return defense;
    }

    public UnitType getUnitType() { return unitType; }
    public boolean isImmortal() { return immortal; }


    // ===== ЛОГИКА =====

    public void takeDamage(int damage) {
        if (immortal) return; // 👈 ключевая логика
        health -= damage;
        if (health < 0) health = 0;
    }

    public boolean isAlive() {
        return immortal || health > 0;
    }

    public String getStatus() {
        return name + " HP: " + health + "/" + maxHealth;
    }
}
