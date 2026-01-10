package MazeGame.cards;

import java.util.List;

public class CombatDeck {

    private final List<Card> cards;

    public CombatDeck(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public static CombatDeck createStarterDeck() {
        return new CombatDeck(List.of(
                new AttackCard(),
                new AttackCard(),
                new DefendCard()
        ));
    }
}
