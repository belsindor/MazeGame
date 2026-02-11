package MazeGame.battle.effects;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleUnit;

public abstract class AbstractBattleEffect implements BattleEffect {

    protected BattleUnit target;
    protected int remainingTurns;

    protected AbstractBattleEffect(int duration) {
        this.remainingTurns = Math.max(1, duration);
    }

    @Override
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
    public void onTurnEnd(BattleContext context) {
        remainingTurns--;

        if (isExpired()) {
            onExpire(context);
            context.addMessage("Эффект закончился: " + getName());
        }
    }

    @Override
    public boolean isExpired() {
        return remainingTurns <= 0;
    }

    @Override
    public int getRemainingTurns() {
        return Math.max(0, remainingTurns);
    }

    @Override
    public int modifyAttack(BattleUnit unit, int baseAttack) {
        return baseAttack;
    }

    @Override
    public int modifyDefense(BattleUnit unit, int baseDefense) {
        return baseDefense;
    }

    public abstract void onExpire(BattleContext context);
    public abstract String getName();
}
