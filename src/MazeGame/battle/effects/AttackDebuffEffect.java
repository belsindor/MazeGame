package MazeGame.battle.effects;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleUnit;


public class AttackDebuffEffect extends AbstractBattleEffect {

    private final int penalty;

    public AttackDebuffEffect(int penalty, int duration) {
        super(duration);
        this.penalty = penalty;
    }

    @Override
    public int modifyAttack(BattleUnit unit, int baseAttack) {
        return baseAttack - penalty;
    }

    @Override
    public void onApply(BattleContext context) {
        context.addMessage("💀 Атака -" + penalty + " у " + target.getName());
    }

    @Override
    public void onExpire(BattleContext context) {}

    @Override
    public String getName() {
        return "Проклятие атаки -" + penalty;
    }
}


