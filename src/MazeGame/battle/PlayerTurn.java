package MazeGame.battle;

import MazeGame.cards.Card;

public class PlayerTurn implements BattleTurn {

    private final Card card;

    public PlayerTurn(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
