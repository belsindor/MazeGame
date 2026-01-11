package MazeGame.cards;

import MazeGame.Monster;
import MazeGame.MonsterTemplate;
import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleResult;

public class MonsterCard extends UnitCard {

    public MonsterCard(MonsterTemplate template) {
        super(template);
    }

    @Override
    public CardType getType() {
        return CardType.MONSTER;
    }

    @Override
    public Monster summon() {
        return new Monster(template).createWeakened(0.5);
    }

    @Override
    public void play(BattleContext context, BattleResult result) {
        throw new UnsupportedOperationException(
                "MonsterCard нельзя разыгрывать — она используется только для визуализации"
        );
    }
}
