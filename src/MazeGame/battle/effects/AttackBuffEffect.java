package MazeGame.battle.effects;

import MazeGame.battle.BattleContext;
//+
public class AttackBuffEffect extends AbstractBattleEffect {

    private final int bonus;

    public AttackBuffEffect(int bonus, int duration) {
        super(duration);
        this.bonus = bonus;
    }

    @Override
    public void onApply(BattleContext context) {
        target.addTemporaryAttack(bonus);
    }

    @Override
    public void onExpire(BattleContext context) {
        target.addTemporaryAttack(-bonus);
    }

    @Override
    public String getName() {
        return "Атака +" + bonus + " (" + getRemainingTurns() + ")";
    }
}
