package MazeGame.battle;


import MazeGame.UnitType;

public class BattleSide {

    private final BattleUnit unit;

    public BattleSide(BattleUnit unit) {
        this.unit = unit;
    }

    public boolean isAlive() {
        return unit.isAlive();
    }

    public void takeDamage(int dmg) {
        unit.takeDamage(dmg);
    }

    public int getAttack() {
        return unit.getAttack();
    }

    public int getDefense() {
        return unit.getDefense();
    }

    public String getName() {
        return unit.getName();
    }

    public BattleUnit getUnit() {
        return unit;
    }
}

