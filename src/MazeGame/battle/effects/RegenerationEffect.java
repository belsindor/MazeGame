package MazeGame.battle.effects;

import MazeGame.battle.BattleContext;

public class RegenerationEffect extends AbstractBattleEffect {

    private final int healPerTurn;

    public RegenerationEffect(int healPerTurn, int duration) {
        super(duration);
        this.healPerTurn = healPerTurn;
    }

    @Override
    public String getName() {
        return "Регенерация (+" + healPerTurn + "/ход)";
    }

    @Override
    public void onTurnStart(BattleContext context) {
        target.heal(healPerTurn);
        context.addMessage("✨ " + target.getName() +
                " восстанавливает " + healPerTurn + " HP");
    }

    @Override
    public void onApply(BattleContext context) {
        context.addMessage("✨ На " + target.getName() + " наложена регенерация");
    }

    @Override
    public void onExpire(BattleContext context) {
        context.addMessage("✨ Регенерация закончилась");
    }
}
