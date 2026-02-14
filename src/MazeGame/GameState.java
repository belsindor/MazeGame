package MazeGame;

import MazeGame.cards.CardCollection;
import MazeGame.cards.CombatDeck;
import MazeGame.cards.SummonDeck;

import java.io.Serializable;

public class GameState implements Serializable {

    private static final GameState INSTANCE = new GameState();

    private final CardCollection cardCollection = new CardCollection();
    private final SummonDeck summonDeck = new SummonDeck();
    private final CombatDeck combatDeck = new CombatDeck();

    private GameState() {
        // стартовый суммон

    }

    public static GameState get() {
        return INSTANCE;
    }

    public CardCollection cards() {
        return cardCollection;
    }

    public SummonDeck summons() {
        return summonDeck;
    }

    public CombatDeck combat() {
        return combatDeck;
    }
}
