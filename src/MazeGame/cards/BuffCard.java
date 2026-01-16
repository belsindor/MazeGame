package MazeGame.cards;

import MazeGame.Player;
import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleResult;
import MazeGame.battle.effects.BattleEffect;

import java.util.function.Function;

//+
public class BuffCard extends Card {

    private final Function<Player, BattleEffect> effectFactory;

    public BuffCard(int id, Function effectFactory, CardRarity rarity, String imagePath) {
        super(id, CardType.BUFF, rarity, imagePath);
        this.effectFactory = effectFactory;
    }

    @Override
    public CardType getType() {
        return CardType.BUFF;
    }

    @Override
    public void play(BattleContext context, BattleResult result) {
        BattleEffect effect = effectFactory.apply(context.getPlayer());
        context.getPlayerSide().addEffect(effect, context);
        result.addMessage("✨ Бафф применён");
    }
}


