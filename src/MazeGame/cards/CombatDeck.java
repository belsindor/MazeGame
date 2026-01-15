package MazeGame.cards;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CombatDeck {

    private final List<Card> cards = new ArrayList<>();

    public void add(Card card) {
        cards.add(card);
    }

    public void remove(Card card) {
        cards.remove(card);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Collection<Card> getCards() {
        return cards;
    }

    public void playCard(Card card, BattleContext context, BattleResult result) {
        if (!cards.contains(card)) {
            return;
        }

        card.play(context, result);

        if (card instanceof ConsumableCard) {
            cards.remove(card);
        }
    }

    public void clear() {
        cards.clear();
    }
}
