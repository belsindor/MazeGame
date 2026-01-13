package MazeGame.cards;

import java.util.*;

public class CardCollection {

    private final Map<Card, Integer> cards = new HashMap<>();

    public void add(Card card) {
        cards.merge(card, 1, Integer::sum);
    }

    public Map<Card, Integer> getAll() {
        return Collections.unmodifiableMap(cards);
    }
}

