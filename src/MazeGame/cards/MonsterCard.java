package MazeGame.cards;

import MazeGame.Monster;
import MazeGame.MonsterTemplate;

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
}
