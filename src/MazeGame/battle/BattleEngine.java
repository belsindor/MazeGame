package MazeGame.battle;

import MazeGame.Monster;
import MazeGame.cards.CardDropService;
import MazeGame.item.Item;
import MazeGame.item.ItemFactory;
import MazeGame.Player;


import java.util.List;

public class BattleEngine {

    private final BattleSide player;
    private final BattleSide enemy;
    private final BattleContext context;

    public BattleEngine(Player player, Monster monster, Monster summon) {
        this.player = new BattleSide(player);
        this.enemy = new BattleSide(monster);

        this.context = new BattleContext(player, monster);
        this.context.setSummon(summon);
    }

    public BattleResult resolveTurn(PlayerTurn turn) {

        BattleResult result = new BattleResult();
        player.onTurnStart(context);
        enemy.onTurnStart(context);


        // === ХОД ИГРОКА (карта) ===
        if (player.isAlive()) {
            turn.apply(context, result);
            turn.apply(context, result);
        }

        // === АТАКА ИГРОКА ===
        if (player.isAlive() && enemy.isAlive()) {
            int dmg = DamageCalculator.calculate(
                    player.getUnit(),
                    enemy.getUnit()
            );
            enemy.takeDamage(dmg);
            result.addMessage("⚔ " + player.getName() + " наносит " + dmg);
        }

        // === АТАКА СУММОНА ===
        Monster summon = context.getSummon();
        if (summon != null && summon.isAlive() && enemy.isAlive()) {
            int dmg = DamageCalculator.calculate(summon, enemy.getUnit());
            enemy.takeDamage(dmg);
            result.addMessage("🧙 Суммон наносит " + dmg);
        }

        // === ХОД ВРАГА ===
        if (enemy.isAlive()) {
            int dmg = DamageCalculator.calculate(
                    enemy.getUnit(),
                    player.getUnit()
            );
            player.takeDamage(dmg);
            result.addMessage("🐲 " + enemy.getName() + " наносит " + dmg);
        }

        // === КОНЕЦ БОЯ ===
        if (!enemy.isAlive()) {
            result.setPlayerWin();
            result.setReward(createReward());
            player.onTurnEnd(context);
            enemy.onTurnEnd(context);
        } else if (!player.isAlive()) {
            result.setPlayerLose();
        }
        result.setDroppedCards(
                CardDropService.generateDrop(player.getLevel())
        );


        return result;
    }



    private BattleReward createReward() {

        int level = context.getEnemy().getLevel();
        int exp = level * 20;

        Item loot = ItemFactory.generateLoot(level);

        return new BattleReward(
                exp,
                loot == null ? List.of() : List.of(loot)
        );
    }

}
