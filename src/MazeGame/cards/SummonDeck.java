package MazeGame.cards;

import MazeGame.MonsterFactory;

import java.util.List;
import java.util.Random;


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
                MonsterFactory.createStarterSummonCard()
        ));
    }

    public SummonCard draw() {
        if (cards.isEmpty()) return null;
        return cards.get(new Random().nextInt(cards.size()));
    }
}
