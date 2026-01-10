package MazeGame.battle.effects;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleUnit;

public interface BattleEffect {

    default int modifyAttack(int base, BattleContext ctx) {
        return base;
    }

    default int modifyDefense(int base, BattleContext ctx) {
        return base;
    }

    default void onApply(BattleContext ctx) {}

    default void onTurnStart(BattleContext ctx) {}

    default void onTurnEnd(BattleContext ctx) {}

    boolean isExpired();
    int getRemainingTurns();

    int modifyAttack(BattleUnit unit, int baseAttack);

    int modifyDefense(BattleUnit unit, int baseDefense);
}
