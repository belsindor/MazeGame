package MazeGame.battle.effects;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleUnit;

//+
public class AttackBuffEffect extends AbstractBattleEffect {

    private final int bonus;

    public AttackBuffEffect(int bonus, int duration) {
        super(duration);
        this.bonus = bonus;
    }

    @Override
    public int modifyAttack(BattleUnit unit, int baseAttack) {
        return baseAttack + bonus;
    }

    @Override
    public void onApply(BattleContext context) {
        context.addMessage("🗡 Атака +" + bonus);
    }

    @Override
    public void onExpire(BattleContext context) {
        context.addMessage("🗡 Бафф атаки закончился");
    }

    @Override
    public String getName() {
        return "Атака +" + bonus;
    }
}
