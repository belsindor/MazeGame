package MazeGame.battle;

import MazeGame.Player;
import MazeGame.Monster;

public class BattleEngine {

    private final BattleSide player;
    private final BattleSide monster;

    public BattleEngine(Player player, Monster monster) {
        this.player = new BattleSide(player);
        this.monster = new BattleSide(monster);
    }

    public BattleResult resolveTurn(PlayerTurn turn) {

        BattleResult result = new BattleResult();

        // === ХОД ИГРОКА ===
        if (player.isAlive()) {
            int dmg = DamageCalculator.calculate(
                    player.getUnit(),
                    monster.getUnit()
            );

            monster.takeDamage(dmg);
            result.addMessage("⚔ " + player.getName() + " наносит " + dmg);
        }

        // === ХОД МОНСТРА ===
        if (monster.isAlive()) {
            int dmg = DamageCalculator.calculate(
                    monster.getUnit(),
                    player.getUnit()
            );

            player.takeDamage(dmg);
            result.addMessage("🐲 " + monster.getName() + " наносит " + dmg);
        }

        // === КОНЕЦ БОЯ ===
        if (!player.isAlive() || !monster.isAlive()) {
            result.setBattleOver();
        }

        return result;
    }
}
