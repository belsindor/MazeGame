package MazeGame.cards;

import MazeGame.battle.BattleContext;

public abstract class AdditionalCard extends Card {

    protected AdditionalCard(CardType type, CardRarity rarity) {
        super(type, rarity);
    }

    public abstract void apply(BattleContext ctx);
}


