package MazeGame.cards;

import MazeGame.Player;
import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleResult;
import MazeGame.battle.effects.BattleEffect;

import java.util.function.Function;


public class BuffCard extends Card {

    private final Function<Player, BattleEffect> effectFactory;

    public BuffCard(Function<Player, BattleEffect> effectFactory, CardRarity rarity) {
        super(rarity);
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


