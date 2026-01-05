package MazeGame;

public class Monster {
    private String name;
    private int level;
    private int health;
    private int maxHealth;
    private int attack;
    private int defense;

    public Monster(String name, int level, int health, int attack, int defense) {
        this.name = name;
        this.level = level;
        this.maxHealth = health;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
    }

    public Monster(String name, int level) {
        this.name = name;
        this.level = level;
        this.maxHealth = 10 * level;
        this.health = maxHealth;
        this.attack = level;
        this.defense = level;
    }

    // Геттеры
    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }

    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - defense);
        health -= actualDamage;
        if (health < 0) health = 0;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getExperienceReward() {
        return level * 10;
    }

    public String getStatus() {
        return String.format("%s | Уровень: %d | Здоровье: %d/%d | Атака: %d | Защита: %d",
                name, level, health, maxHealth, attack, defense);
    }
}