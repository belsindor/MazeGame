package MazeGame.cards;

import MazeGame.Monster;
import MazeGame.battle.effects.BattleEffect;

import java.util.function.Function;
import java.util.function.Supplier;

public class PoisonCard extends CurseCard {
    public PoisonCard(
            Function<Monster, BattleEffect> effectFactory,
            CardRarity rarity,
            String imagePath
    ) {
        super(effectFactory, rarity, imagePath);
    }
}
