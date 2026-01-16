package MazeGame.battle.effects;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleUnit;
//+
public interface BattleEffect {

    void setTarget(BattleUnit target);
    void onApply(BattleContext context);
    void onTurnStart(BattleContext context);
    void onTurnEnd(BattleContext context);

    default void onExpire(BattleContext context) {}

    default int modifyAttack(int base, BattleContext ctx) {
        return base;
    }

    default int modifyDefense(int base, BattleContext ctx) {
        return base;
    }



    boolean isExpired();
    int getRemainingTurns();

    int modifyAttack(BattleUnit unit, int baseAttack);

    int modifyDefense(BattleUnit unit, int baseDefense);
}
