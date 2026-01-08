package MazeGame.battle;

import MazeGame.UnitType;

public interface BattleUnit {

    int getAttack();
    int getDefense();

    int getTotalAttack();

    int getTotalDefense();

    void takeDamage(int damage);
    boolean isAlive();

    String getName();
    UnitType getUnitType();
}
