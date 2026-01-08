package MazeGame.cards;

import java.util.ArrayList;
import java.util.List;

public class CombatDeck {

    private final List<Card> cards = new ArrayList<>();

    public List<Card> getCards() {
        return cards;
    }

    public void add(Card card) {
        cards.add(card);
    }

    public void remove(Card card) {
        cards.remove(card);
    }

    public void clear() {
        cards.clear();
    }
}
