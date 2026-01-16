package MazeGame.battle.effects;

import MazeGame.battle.BattleContext;
//+
public class AttackDebuffEffect extends TimedEffect {

    private final int penalty;

    public AttackDebuffEffect(int penalty, int duration) {
        super(duration);
        this.penalty = penalty;
    }

    @Override
    public void onApply(BattleContext context) {
        getTarget().addTemporaryAttack(-penalty); // нет в BattleUnit!
    }

    @Override
    public void onExpire(BattleContext context) {
        getTarget().addTemporaryAttack(penalty); // нет в BattleUnit!
    }

    @Override
    public String getName() {
        return "Проклятие атаки -" + penalty;
    }
}
