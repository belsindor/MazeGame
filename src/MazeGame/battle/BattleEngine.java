package MazeGame.battle;

import MazeGame.Monster;
import MazeGame.cards.Card;
import MazeGame.cards.CardDropService;
import MazeGame.cards.SummonCard;
import MazeGame.item.Item;
import MazeGame.item.ItemFactory;
import MazeGame.Player;

import java.util.List;

public class BattleEngine {

    private final BattleSide playerSide;
    private final BattleSide enemySide;
    private final BattleContext context;

    public BattleEngine(Player player, Monster monster) {
        this.playerSide = new BattleSide(player);
        this.enemySide = new BattleSide(monster);
        this.context = new BattleContext(player, monster);

        // SummonCard → Monster (призыв)
        Monster summon = player.getSummonDeck().getSelectedSummon().summon();
        this.context.setSummon(summon);
    }

    public BattleResult resolveTurn(PlayerTurn turn) {
        BattleResult result = new BattleResult();

        // Начало хода
        playerSide.onTurnStart(context);
        enemySide.onTurnStart(context);

        Monster summon = context.getSummon();
        if (summon != null && summon.isAlive()) {
            summon.onTurnStart(context);
        }

        // Ход игрока (карта)
        if (playerSide.isAlive()) {
            turn.apply(context, result);
        }

        // Атака игрока
        if (playerSide.isAlive() && enemySide.isAlive()) {
            int dmg = DamageCalculator.calculate(playerSide.getUnit(), enemySide.getUnit());
            enemySide.takeDamage(dmg);
            result.addMessage("⚔ " + playerSide.getName() + " наносит " + dmg);
        }

        // Атака суммона
        if (summon != null && summon.isAlive() && enemySide.isAlive()) {
            int dmg = DamageCalculator.calculate(summon, enemySide.getUnit());
            enemySide.takeDamage(dmg);
            result.addMessage("🧙 " + summon.getName() + " наносит " + dmg);
        }

        // Ход врага
        if (enemySide.isAlive()) {
            int dmg = DamageCalculator.calculate(enemySide.getUnit(), playerSide.getUnit());
            playerSide.takeDamage(dmg);
            result.addMessage("🐲 " + enemySide.getName() + " наносит " + dmg);
        }

        // Конец хода → тикают эффекты
        playerSide.onTurnEnd(context);
        enemySide.onTurnEnd(context);
        if (summon != null && summon.isAlive()) {
            summon.onTurnEnd(context);
        }

        // Проверка окончания боя
        if (!enemySide.isAlive()) {
            result.setPlayerWin();

            BattleReward reward = createReward();
            result.setReward(reward);

            List<Card> dropped = CardDropService.generateDrop(enemySide.getUnit());
            result.setDroppedCards(dropped);

            // Передаём именно Player
            processDroppedCards((Player) playerSide.getUnit(), dropped);
        } else if (!playerSide.isAlive()) {
            result.setPlayerLose();
            result.setDroppedCards(List.of());
        }

        // Очистка временных баффов без длительности
        playerSide.getUnit().clearTemporaryEffects();
        if (summon != null) {
            summon.clearTemporaryEffects();
        }

        return result;
    }

    private void processDroppedCards(Player player, List<Card> dropped) {
        if (dropped == null || dropped.isEmpty()) {
            return;
        }

        for (Card card : dropped) {
            if (card instanceof SummonCard summonCard) {
                player.getSummonDeck().tryAddOrUpgrade(summonCard);
            } else {
                player.getCardCollection().add(card);
            }
        }
    }

    private BattleReward createReward() {
        int level = enemySide.getLevel();
        int exp = level * 20;

        Item loot = ItemFactory.generateLoot(level);

        return new BattleReward(exp, loot != null ? List.of(loot) : List.of());
    }
}