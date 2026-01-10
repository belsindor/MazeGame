package MazeGame.cards;

import MazeGame.battle.effects.BattleEffect;

import java.util.function.Supplier;

public class PoisonCard extends CurseCard {
    public PoisonCard(
            Supplier<BattleEffect> effectFactory,
            CardRarity rarity,
            String imagePath
    ) {
        super(effectFactory, rarity, imagePath);
    }
}
