package MazeGame.battle;

import MazeGame.Monster;
import MazeGame.Player;
import MazeGame.cards.Card;
//+
public class PlayerTurn implements BattleTurn {

    private final Card card;

    public PlayerTurn(Card card) {
        this.card = card;
    }

    @Override
    public void apply(BattleContext context, BattleResult result) {

        Player player = context.getPlayer();
        Monster enemy = context.getEnemy();

        // === 1. КАРТА (если есть) ===
        if (card != null) {
            card.play(context, result);
        }

        // === 2. ОБЯЗАТЕЛЬНАЯ АТАКА ===
        int dmg = DamageCalculator.calculate((BattleUnit) player, enemy);
        enemy.takeDamage(dmg);
        result.addMessage("⚔ " + player.getName() + " наносит " + dmg);
    }
}
