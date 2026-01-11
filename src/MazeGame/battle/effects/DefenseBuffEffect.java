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
    public String getName() {
        return "Бафф защиты +" + bonus;
    }

    @Override
    public int modifyDefense(BattleUnit unit, int baseDefense) {
        return baseDefense + bonus;
    }

    @Override
    public void onExpire(BattleContext context) {
        // Ничего не делаем — бафф просто исчезает
    }
}
