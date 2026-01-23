package MazeGame.battle;

import MazeGame.cards.Card;

public class PlayerTurn implements BattleTurn {

    private final Card card;

    public PlayerTurn(Card card) {
        this.card = card;
    }

    @Override
    public void apply(BattleContext context, BattleResult result) {
        // Только карта (опционально)
        if (card != null) {
            card.play(context, result);
        }
    }
}