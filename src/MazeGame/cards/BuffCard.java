package MazeGame.cards;

import MazeGame.battle.BattleUnit;

public class BuffCard extends Card {

    private final BuffEffect effect;

    public BuffCard(BuffEffect effect, CardRarity rarity) {
        super(rarity);
        this.effect = effect;
    }

    @Override
    public CardType getType() {
        return CardType.BUFF;
    }

    public void apply(BattleUnit target) {
        effect.apply(target);
    }
}
