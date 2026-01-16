package MazeGame.battle.effects;

import MazeGame.battle.BattleContext;
//+
public abstract class TimedEffect extends AbstractBattleEffect {

    protected int turnsLeft;


    protected TimedEffect(int duration) {
        super(duration);
    }

    @Override
    public int getRemainingTurns() {
        return turnsLeft;
    }

    @Override
    public void onTurnEnd(BattleContext context) {
        turnsLeft--;
    }

    @Override
    public boolean isExpired() {
        return turnsLeft <= 0;
    }

    public abstract void onExpire(BattleContext context);

    public abstract String getName();
}
