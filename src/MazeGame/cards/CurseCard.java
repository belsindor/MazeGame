package MazeGame.cards;

import MazeGame.Monster;
import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleResult;
import MazeGame.battle.BattleSide;
import MazeGame.battle.effects.BattleEffect;

import java.util.function.Function;

//+
public class CurseCard extends Card {

    private final Function<Monster, BattleEffect> effectFactory;

    public CurseCard(int id, Function<Monster, BattleEffect> effectFactory, CardRarity rarity, TypeEffect effect, String imagePath) {
        super(id, CardType.CURSE, rarity, effect, imagePath);
        this.effectFactory = effectFactory;
    }

    @Override
    public CardType getType() {
        return CardType.CURSE;
    }

    @Override
    public void play(BattleContext context, BattleResult result) {
        BattleSide target = context.getCurrentTarget();

        if (target == null) {
            result.addMessage("❌ Нет цели для проклятия");
            return;
        }

        Monster monster = (Monster) target.getUnit();
        BattleEffect effect = effectFactory.apply(monster);
        target.addEffect(effect, context);

        result.addMessage("☠ Проклятие наложено на " + target.getName());
    }

}


