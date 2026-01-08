package MazeGame.cards;

import MazeGame.Monster;

public class CurseCard extends Card {

    private final CurseEffect effect;

    public CurseCard(CurseEffect effect, CardRarity rarity) {
        super(rarity);
        this.effect = effect;
    }

    @Override
    public CardType getType() {
        return CardType.CURSE;
    }

    public void apply(Monster target) {
        effect.apply(target);
    }
}
