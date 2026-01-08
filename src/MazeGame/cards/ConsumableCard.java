package MazeGame.cards;

import MazeGame.battle.BattleContext;

public class ConsumableCard extends Card {

    private final ConsumableEffect effect;

    public ConsumableCard(ConsumableEffect effect, CardRarity rarity) {
        super(rarity);
        this.effect = effect;
    }

    @Override
    public CardType getType() {
        return CardType.CONSUMABLE;
    }

    public void use(BattleContext ctx) {
        effect.apply(ctx);
        copies--;
    }

    public boolean isEmpty() {
        return copies <= 0;
    }
}
