package MazeGame.cards;

import MazeGame.Monster;
import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleResult;
import MazeGame.battle.effects.BattleEffect;

import java.util.function.Function;


public class CurseCard extends Card {

    private final Function<Monster, BattleEffect> effectFactory;

    public CurseCard(Function<Monster, BattleEffect> effectFactory, CardRarity rarity, String imagePath) {
        super(CardType.CURSE, rarity, imagePath);
        this.effectFactory = effectFactory;
    }

    @Override
    public CardType getType() {
        return CardType.CURSE;
    }

    @Override
    public void play(BattleContext context, BattleResult result) {
        BattleEffect effect = effectFactory.apply(context.getEnemy());
        context.getEnemySide().addEffect(effect, context);
        result.addMessage("☠ Проклятие наложено");
    }
}


