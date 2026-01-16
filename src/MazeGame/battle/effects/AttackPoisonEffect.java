package MazeGame.battle.effects;

import MazeGame.battle.BattleContext;
//+
public class AttackPoisonEffect extends TimedEffect {

    private final int damagePerTurn;

    public AttackPoisonEffect(int damage, int duration) {
        super(duration);
        this.damagePerTurn = damage;
    }

    @Override
    public void onTurnStart(BattleContext context) {
        getTarget().takeDamage(damagePerTurn);
    }

    @Override
    public void onExpire(BattleContext context) {
            }

    @Override
    public String getName() {
        return "Отравление " + damagePerTurn;
    }
}

