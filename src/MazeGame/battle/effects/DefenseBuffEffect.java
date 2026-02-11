package MazeGame.battle.effects;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleUnit;

public class DefenseBuffEffect extends AbstractBattleEffect {

    private final int bonus;

    public DefenseBuffEffect(int bonus, int duration) {
        super(duration);
        this.bonus = bonus;
    }

    @Override
    public int modifyDefense(BattleUnit unit, int baseDefense) {
        return baseDefense + bonus;
    }

    @Override
    public void onApply(BattleContext context) {
        context.addMessage("🛡 Защита +" + bonus + " у " + target.getName());
    }

    @Override
    public void onExpire(BattleContext context) {}

    @Override
    public String getName() {
        return "Бафф защиты +" + bonus;
    }
}

