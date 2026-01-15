package MazeGame.cards;

import MazeGame.Monster;

public class BattleSummon extends Monster {

    private final SummonCard source;

    public BattleSummon(SummonCard card) {
        super(
                card.getName(),
                card.getAttack() / 2,
                card.getHealth() / 2,
                card.getUnitType(),
                card.getId()
        );
        this.source = card;
    }

    public SummonCard getSourceCard() {
        return source;
    }
}
