package MazeGame.battle;

import MazeGame.UnitType;

public interface BattleUnit {

    // ===== ИДЕНТИФИКАЦИЯ =====
    String getName();
    UnitType getUnitType();
    int getId();

    // ===== БАЗОВЫЕ СТАТЫ =====
    int getAttack();           // базовая атака без модификаторов
    int getDefense();          // базовая защита без модификаторов
    int getLevel();            // ← добавлен

    // ===== С УЧЁТОМ ВСЕХ ЭФФЕКТОВ =====
    int getTotalAttack();
    int getTotalDefense();

    // ===== ЗДОРОВЬЕ =====
    int getHealth();
    int getMaxHealth();
    boolean isAlive();

    // ===== ИЗМЕНЕНИЕ СОСТОЯНИЯ =====
    void takeDamage(int damage);
    void heal(int amount);                // ← добавлен

    // ===== ВРЕМЕННЫЕ БАФФЫ =====
    void addTemporaryAttack(int value);
    void addTemporaryDefense(int value);

    // Опционально: очистка временных эффектов в конце хода/боя
    default void clearTemporaryEffects() {
        // если хочешь — можно реализовать здесь или оставить абстрактным
    }
    default void onTurnStart(BattleContext ctx) { /* можно переопределить */ }
    default void onTurnEnd(BattleContext ctx) { /* можно переопределить */ }
    default void onBattleEnd(BattleContext ctx) { /* опционально */ }
    void setUnitType(UnitType type);

}
