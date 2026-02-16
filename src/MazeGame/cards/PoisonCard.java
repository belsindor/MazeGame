package MazeGame.cards;

import MazeGame.Monster;
import MazeGame.battle.effects.BattleEffect;

import java.util.function.Function;

//+
public class PoisonCard extends CurseCard {
    public PoisonCard(
            String title, String name, int id, Function<Monster, BattleEffect> effectFactory,
            CardRarity rarity,
            TypeEffect effect,
            String imagePath
    ) {
        super(title, name, id, effectFactory, rarity, effect, imagePath);
    }
}
