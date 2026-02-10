package MazeGame.battle.effects;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleUnit;
//+
public abstract class AbstractBattleEffect implements BattleEffect {

    protected BattleUnit target;
    protected int remainingTurns;


    public void setTarget(BattleUnit target) {
        this.target = target;
    }
    protected BattleUnit getTarget() {
        return target;
    }
    @Override
    public void onApply(BattleContext context) {}

    @Override
    public void onTurnStart(BattleContext context) {}

    @Override
    public int modifyAttack(BattleUnit unit, int baseAttack) {
        return baseAttack;
    }

    public abstract String getName();

    @Override
    public int modifyDefense(BattleUnit unit, int baseDefense) {
        return baseDefense;
    }

    public abstract void onExpire(BattleContext context);

    protected AbstractBattleEffect(int duration) {
        this.remainingTurns = Math.max(1, duration); // минимум 1 ход
    }

    @Override
    public boolean isExpired() {
        return remainingTurns <= 0;
    }

    @Override
    public int getRemainingTurns() {
        return Math.max(0, remainingTurns);
    }

    protected void decreaseDuration() {
        if (remainingTurns > 0) {
            remainingTurns--;
        }
    }

    @Override
    public void onTurnEnd(BattleContext context) {
        decreaseDuration();

    }
    protected void tickDuration() {
        if (remainingTurns > 0) {
            remainingTurns--;
        }
    }

}
