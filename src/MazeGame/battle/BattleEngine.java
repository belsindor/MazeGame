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

        Monster summon = player.getSummonDeck().getSelectedSummon();
        this.context.setSummon(summon);
    }

    public BattleResult resolveTurn(PlayerTurn turn) {
        BattleResult result = new BattleResult();

        // 1. Начало хода всех участников
        playerSide.onTurnStart(context);
        enemySide.onTurnStart(context);

        Monster summon = context.getSummon();
        if (summon != null && summon.isAlive()) {
            summon.onTurnStart(context);
        }

        // 2. Ход игрока (разыгрывание карты)
        if (playerSide.isAlive()) {
            turn.apply(context, result);
        }

        // 3. Атака игрока
        if (playerSide.isAlive() && enemySide.isAlive()) {
            int dmg = DamageCalculator.calculate(playerSide.getUnit(), enemySide.getUnit());
            enemySide.takeDamage(dmg);
            result.addMessage("⚔ " + playerSide.getName() + " наносит " + dmg);
        }

        // 4. Атака суммона (если есть и жив)
        if (summon != null && summon.isAlive() && enemySide.isAlive()) {
            int dmg = DamageCalculator.calculate(summon, enemySide.getUnit());
            enemySide.takeDamage(dmg);
            result.addMessage("🧙 " + summon.getName() + " наносит " + dmg);
        }

        // 5. Ход врага
        if (enemySide.isAlive()) {
            int dmg = DamageCalculator.calculate(enemySide.getUnit(), playerSide.getUnit());
            playerSide.takeDamage(dmg);
            result.addMessage("🐲 " + enemySide.getName() + " наносит " + dmg);

            // Опционально: враг может атаковать суммона (раскомментировать при необходимости)
            // if (summon != null && summon.isAlive()) {
            //     int summonDmg = DamageCalculator.calculate(enemySide.getUnit(), summon);
            //     summon.takeDamage(summonDmg);
            //     result.addMessage("Враг бьёт суммона на " + summonDmg);
            // }
        }

        // 6. Конец хода → тикают и удаляются эффекты
        playerSide.onTurnEnd(context);
        enemySide.onTurnEnd(context);
        if (summon != null && summon.isAlive()) {
            summon.onTurnEnd(context);
        }

        // 7. Проверка завершения боя
        if (!enemySide.isAlive()) {
            result.setPlayerWin();

            BattleReward reward = createReward();
            result.setReward(reward);

            List<Card> dropped = CardDropService.generateDrop(enemySide.getUnit());
            result.setDroppedCards(dropped);

            processDroppedCards(playerSide.getUnit(), dropped);

            // Дополнительные действия по окончании боя (если нужны)
            // playerSide.getUnit().onBattleEnd(context);
            // enemySide.getUnit().onBattleEnd(context);
        } else if (!playerSide.isAlive()) {
            result.setPlayerLose();
            result.setDroppedCards(List.of());
        }

        // Очистка временных баффов без длительности (если такие есть в игре)
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
                boolean changed = player.getSummonDeck().tryAddOrUpgrade(summonCard);

                // Здесь можно добавить уведомление для игрока
                if (changed) {
                    String msg = player.getSummonDeck().getAll().containsKey(summonCard.getUnitType())
                            ? "Улучшен суммон: " + summonCard.getUnitName()
                            : "Новый суммон: " + summonCard.getUnitName();
                    // HUDMessageManager.showInfo(msg + " (" + summonCard.getRarity() + ")");
                }
            } else {
                // Обычные боевые карты добавляем в коллекцию игрока
                player.getCardCollection().add(card);
                // Можно сразу предложить добавить в колоду, если логика такая есть
            }
        }
    }

    private BattleReward createReward() {
        int level = enemySide.getLevel();
        int exp = level * 20;

        Item loot = ItemFactory.generateLoot(level);

        return new BattleReward(
                exp,
                loot == null ? List.of() : List.of(loot)
        );
    }
}