package MazeGame.cards;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleResult;
import MazeGame.battle.BattleSide;
import MazeGame.battle.BattleUnit;
import MazeGame.battle.effects.AbstractBattleEffect;
import MazeGame.battle.effects.BattleEffect;

import java.util.function.Function;

//++
public class BuffCard extends Card {

    private final Function<BattleUnit, BattleEffect> effectFactory;

    public BuffCard(String title, String name, int id,
                    Function<BattleUnit, BattleEffect> effectFactory,
                    CardRarity rarity,
                    TypeEffect effect,
                    String imagePath) {
        super(id, CardType.BUFF, rarity, effect, imagePath);
        this.title = title;
        this.name = name;
        this.effectFactory = effectFactory;
    }

    @Override
    public CardType getType() {
        return CardType.BUFF;
    }

    @Override
    public void play(BattleContext context, BattleResult result) {
        BattleSide target = context.getCurrentTarget();

        if (target == null) {
            result.addMessage("❌ Нет цели для применения карты");
            return;
        }

        BattleUnit unit = target.getUnit();

        BattleEffect effect = effectFactory.apply(unit);
        target.addEffect(effect, context);
        if (effect instanceof AbstractBattleEffect abe) {
            abe.setIconPath(this.getImagePath());
        }

    }
}



