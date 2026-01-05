package MazeGame;

import java.io.Serializable;

public abstract class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String name;
    protected int strength;
    protected int currentStrength;
    protected double dropChance;
    protected int value;
    public abstract String getIconPath();

    public Item(String name, int strength, int value, double dropChance) {
        this.name = name;
        this.strength = strength;
        this.currentStrength = strength;
        this.value = value;
        this.dropChance = dropChance;
    }

    public abstract String getType();
    public abstract int getProtection();
    public abstract int getAttack();

    public void reduceStrength() {
        if (currentStrength > 0) currentStrength--;
    }

    public boolean isBroken() {
        return currentStrength <= 0;
    }

    public void repair() {
        currentStrength = strength;
    }

    // Геттеры
    public String getName() { return name; }
    public int getStrength() { return strength; }
    public int getCurrentStrength() { return currentStrength; }
    public double getDropChance() { return dropChance; }
    public int getValue() { return value; }

    @Override
    public String toString() {
        if (this instanceof Weapon) {
            return name + " | Атака " + getAttack() +
                    " | " + currentStrength + "/" + strength;
        }
        if (this instanceof Armor) {
            return name + " | Защита " + getProtection() +
                    " | " + currentStrength + "/" + strength;
        }
        return name;
    }


}