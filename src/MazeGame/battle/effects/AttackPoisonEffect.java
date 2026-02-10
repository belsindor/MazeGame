package MazeGame.battle.effects;

import MazeGame.battle.BattleContext;

public class AttackPoisonEffect extends AbstractBattleEffect {

    private final int damagePerTurn;

    public AttackPoisonEffect(int damagePerTurn, int duration) {
        super(duration);
        this.damagePerTurn = damagePerTurn;
    }

    @Override
    public void onTurnStart(BattleContext context) {
        getTarget().takeDamage(damagePerTurn);
        context.addMessage("☠ " + getTarget().getName()
                + " получает " + damagePerTurn + " урона от яда");
    }

    @Override
    public void onExpire(BattleContext context) {
        context.addMessage("☠ Яд на " + getTarget().getName() + " рассеялся");
    }

    @Override
    public String getName() {
        return "Отравление (" + damagePerTurn + ")";
    }
}
