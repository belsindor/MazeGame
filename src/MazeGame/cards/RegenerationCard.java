package MazeGame.cards;

import MazeGame.battle.effects.BattleEffect;

import java.util.function.Function;
import java.util.function.Supplier;

public class RegenerationCard extends BuffCard {
    public RegenerationCard(int id,
            Function effectFactory,
            CardRarity rarity,
            String imagePath
    ) {
        super(id, effectFactory, rarity, imagePath);
    }
}
