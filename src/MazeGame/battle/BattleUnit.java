package MazeGame.battle;

import MazeGame.UnitType;

public interface BattleUnit {

    // ===== БАЗОВЫЕ СТАТЫ =====
    int getAttack();
    int getDefense();
    int getLevel();

    // ===== С УЧЁТОМ ЭФФЕКТОВ =====
    int getTotalAttack();
    int getTotalDefense();

    // ===== БОЙ =====
    void takeDamage(int damage);
    void heal(int amount);
    boolean isAlive();

    // ===== ВРЕМЕННЫЕ ЭФФЕКТЫ =====
    void addTemporaryAttack(int value);
    void addTemporaryDefense(int value);

    // ===== МЕТА =====
    String getName();
    UnitType getUnitType();
    void setUnitType(UnitType type);

}
