package MazeGame.battle.effects;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleUnit;

public abstract class AbstractBattleEffect implements BattleEffect {

    protected BattleUnit target;
    protected int turnsLeft;
    protected int duration;

    protected AbstractBattleEffect(int duration) {
        this.duration = duration;
    }


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
        duration--;
    }

    @Override
    public boolean isExpired() {
        return duration <= 0;
    }

    @Override
    public int modifyAttack(BattleUnit unit, int baseAttack) {
        return baseAttack;
    }

    public abstract String getName();

    @Override
    public int getRemainingTurns() {
        return turnsLeft;
    }

    @Override
    public int modifyDefense(BattleUnit unit, int baseDefense) {
        return baseDefense;
    }

    public abstract void onExpire(BattleContext context);
}
