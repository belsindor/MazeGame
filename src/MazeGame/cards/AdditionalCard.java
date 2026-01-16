package MazeGame.cards;

import MazeGame.battle.BattleContext;

public abstract class AdditionalCard extends Card {

    protected AdditionalCard(int id, CardType type, CardRarity rarity) {
        super(id, type, rarity);
    }

    public abstract void apply(BattleContext ctx);
}


