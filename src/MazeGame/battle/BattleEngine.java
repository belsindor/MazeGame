package MazeGame.battle;

import MazeGame.GameState;
import MazeGame.Monster;
import MazeGame.Player;
import MazeGame.cards.BattleSummon;
import MazeGame.cards.Card;
import MazeGame.cards.CardDropService;
import MazeGame.cards.SummonCard;

import java.util.List;

public class BattleEngine {

    private final BattleSide playerSide;
    private final BattleSide enemySide;
    private final BattleContext context;
    private final Player player;

    public BattleEngine(Player player, Monster monster) {
        this.player = player;
        this.playerSide = new BattleSide(player);
        this.enemySide = new BattleSide(monster);
        this.context = new BattleContext(player, monster);

        SummonCard selected = player.getSummonDeck().getSelectedSummon();
        if (selected != null) {
            this.context.setSummon(new BattleSummon(selected));
        }
    }

    public BattleResult resolveTurn(PlayerTurn turn) {
        BattleResult result = new BattleResult();

        playerSide.onTurnStart(context);
        enemySide.onTurnStart(context);

        BattleSummon summon = context.getSummon();
        if (summon != null && summon.isAlive()) {
            summon.onTurnStart(context);
        }

        if (playerSide.isAlive()) {
            turn.apply(context, result);
        }

        if (playerSide.isAlive() && enemySide.isAlive()) {
            int dmg = DamageCalculator.calculate(playerSide.getUnit(), enemySide.getUnit());
            enemySide.takeDamage(dmg);
            result.addMessage("⚔ Игрок наносит " + dmg);
        }

        if (summon != null && summon.isAlive() && enemySide.isAlive()) {
            int dmg = DamageCalculator.calculate(summon, enemySide.getUnit());
            enemySide.takeDamage(dmg);
            result.addMessage("🧙 " + summon.getName() + " наносит " + dmg);
        }

        if (enemySide.isAlive()) {
            int dmg = DamageCalculator.calculate(enemySide.getUnit(), playerSide.getUnit());
            playerSide.takeDamage(dmg);
            result.addMessage("🐲 Враг наносит " + dmg);
        }

        playerSide.onTurnEnd(context);
        enemySide.onTurnEnd(context);

        if (summon != null) {
            summon.onTurnEnd(context);
        }

        if (!enemySide.isAlive()) {
            result.setPlayerWin();
            result.setReward(createReward());

            List<Card> dropped = CardDropService.generateDrop(enemySide.getUnit());
            result.setDroppedCards(dropped);

        } else if (!playerSide.isAlive()) {
            result.setPlayerLose();
        }

        player.clearTemporaryEffects();
        if (summon != null) summon.clearTemporaryEffects();

        GameState.get().combat().clear();
        return result;
    }

    private BattleReward createReward() {
        int level = enemySide.getLevel();
        return new BattleReward(level * 20, List.of());
    }
}
