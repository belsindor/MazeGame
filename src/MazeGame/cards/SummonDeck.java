package MazeGame.cards;

import java.util.List;

class SummonDeck {

    private final List<SummonCard> cards;

    public SummonDeck(List<SummonCard> cards) {
        this.cards = cards;
    }

    public List<SummonCard> getCards() {
        return cards;
    }
}

