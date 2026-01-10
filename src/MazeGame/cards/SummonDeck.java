package MazeGame.cards;

import java.util.List;
import MazeGame.MonsterFactory;

public class SummonDeck {

    private final List<SummonCard> cards;

    public SummonDeck(List<SummonCard> cards) {
        this.cards = cards;
    }

    public List<SummonCard> getCards() {
        return cards;
    }

    public static SummonDeck createStarterDeck() {
        return new SummonDeck(List.of(
                new SummonCard(MonsterFactory.createWolf()),
                new SummonCard(MonsterFactory.createSkeleton())
        ));
    }
}
