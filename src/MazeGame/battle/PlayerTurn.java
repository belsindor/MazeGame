package MazeGame.battle;

import MazeGame.cards.Card;
import MazeGame.cards.CardTarget;

public class PlayerTurn implements BattleTurn {

    private final Card card;
    private final CardTarget target;

    public PlayerTurn(Card card, CardTarget target) {
        this.card = card;
        this.target = target;
    }

    @Override
    public void apply(BattleContext context, BattleResult result) {
        if (card == null) return;

        switch (target) {
            case PLAYER -> card.playOnPlayer(context, result);
            case SUMMON -> card.playOnSummon(context, result);
            case ENEMY  -> card.playOnEnemy(context, result);
        }
    }
}