package MazeGame.cards;

import MazeGame.Monster;
import MazeGame.MonsterTemplate;

public class SummonCard extends UnitCard {

    public SummonCard(MonsterTemplate template) {
        super(template);
    }

    @Override
    public CardType getType() {
        return CardType.SUMMON;
    }

    @Override
    public Monster summon() {
        return new Monster(template).createSummon();
    }
}
