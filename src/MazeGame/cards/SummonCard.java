package MazeGame.cards;

import MazeGame.Monster;
import MazeGame.MonsterTemplate;
import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleResult;

public class SummonCard extends UnitCard {

    public SummonCard(MonsterTemplate template) {
        super(template);
    }

    public BattleSummon toBattleSummon() {
        return new BattleSummon(this);
    }

    @Override
    public CardType getType() {
        return CardType.SUMMON;
    }

    @Override
    public Monster summon() {
        return new Monster(template).createSummon();
    }

    @Override
    public void play(BattleContext context, BattleResult result) {
        Monster summon = summon();
        context.setSummon(summon);

        result.addMessage("🔮 Призван суммон: " + summon.getName());
    }
}
