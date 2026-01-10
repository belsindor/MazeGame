package MazeGame.cards;

import MazeGame.battle.effects.BattleEffect;

import java.util.function.Supplier;

public class RegenerationCard extends BuffCard {
    public RegenerationCard(
            Supplier<BattleEffect> effectFactory,
            CardRarity rarity,
            String imagePath
    ) {
        super(effectFactory, rarity, imagePath);
    }
}
