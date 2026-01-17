package MazeGame.cards;

import MazeGame.Monster;
import MazeGame.battle.effects.BattleEffect;

import java.util.function.Function;

//+
public class PoisonCard extends CurseCard {
    public PoisonCard(
            int id, Function<Monster, BattleEffect> effectFactory,
            CardRarity rarity,
            TypeEffect effect,
            String imagePath
    ) {
        super(id, effectFactory, rarity, effect, imagePath);
    }
}
