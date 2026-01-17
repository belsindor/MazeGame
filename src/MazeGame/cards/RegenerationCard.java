package MazeGame.cards;

import MazeGame.Monster;
import MazeGame.Player;
import MazeGame.battle.effects.BattleEffect;

import java.util.function.Function;
//+
public class RegenerationCard extends BuffCard {


    public RegenerationCard(int id,
            Function effectFactory,
            CardRarity rarity,
            TypeEffect effect,
            String imagePath
    ) {
        super(id, effectFactory, rarity, effect, imagePath);
    }
}
