package MazeGame.cards;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleResult;

import java.util.function.Consumer;

public class ConsumableCard extends Card {

    private final Consumer<BattleContext> action;

    public ConsumableCard(Consumer<BattleContext> action, CardRarity rarity, String imagePath) {
        super(CardType.CONSUMABLE, rarity, imagePath);
        this.action = action;
    }

    @Override
    public void play(BattleContext context, BattleResult result) {
        action.accept(context);
        result.addMessage("🧪 Карта использована");
    }
}
