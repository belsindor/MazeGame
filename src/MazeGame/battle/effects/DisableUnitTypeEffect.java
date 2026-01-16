package MazeGame.battle.effects;

import MazeGame.UnitType;
import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleUnit;
//+
public class DisableUnitTypeEffect extends TimedEffect {

    private UnitType originalType;

    public DisableUnitTypeEffect(int duration) {
        super(duration);
    }

    @Override
    public void onApply(BattleContext context) {
        BattleUnit target = getTarget();
        originalType = target.getUnitType();
        target.setUnitType(UnitType.NONE);
    }

    @Override
    public void onExpire(BattleContext context) {
        getTarget().setUnitType(originalType);
    }

    @Override
    public String getName() {
        return "Подавление типа";
    }
}
