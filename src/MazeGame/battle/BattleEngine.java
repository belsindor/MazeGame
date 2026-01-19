package MazeGame.battle;

import MazeGame.GameState;
import MazeGame.Monster;
import MazeGame.Player;
import MazeGame.cards.*;

import java.util.List;

public class BattleEngine {

    private final BattleSide playerSide;
    private final BattleSide enemySide;
    private final BattleContext context;

    public BattleEngine(Player player, Monster monster) {
        this.playerSide = new BattleSide((BattleUnit) player);
        this.enemySide = new BattleSide(monster);
        this.context = new BattleContext(player, monster);
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

        // Ход игрока
        if (playerSide.isAlive()) {
            turn.apply(context, result);
        }

        // Атака игрока
        if (playerSide.isAlive() && enemySide.isAlive()) {
            int dmg = DamageCalculator.calculate(playerSide.getUnit(), enemySide.getUnit());
            enemySide.takeDamage(dmg);
            result.addMessage("⚔ " + playerSide.getName() + " наносит " + dmg);
        }

        // Атака суммона (если есть)
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

        // Конец хода — эффекты
        playerSide.onTurnEnd(context);
        enemySide.onTurnEnd(context);
        if (summon != null && summon.isAlive()) {
            summon.onTurnEnd(context);
        }

        // Проверка конца боя
        boolean battleEnded = false;

        if (!enemySide.isAlive()) {
            result.setPlayerWin();
            battleEnded = true;

            BattleReward reward = createReward(enemySide.getUnit().getLevel());
            result.setReward(reward);

            // Генерируем дроп
            Monster enemyMonster = (Monster) enemySide.getUnit();
            List<CardDropService.DropEntry> drops = new CardDropService().generateDrop(enemyMonster);

            // Обрабатываем дроп с защитой от дублирования суммона
            processDroppedCards((Player) playerSide.getUnit(), drops, result);
        } else if (!playerSide.isAlive()) {
            result.setPlayerLose();
            battleEnded = true;
        }

        // Финальная очистка только при завершении боя
        if (battleEnded) {
            playerSide.getUnit().clearTemporaryEffects();
            if (summon != null) {
                summon.clearTemporaryEffects();
            }

            playerSide.getUnit().getSummonDeck().resetSelection();
        }

        GameState.get().combat().clear();

        return result;
    }

    /**
     * Обработка дропа — с защитой от двойного добавления суммона
     */
    private void processDroppedCards(Player player, List<CardDropService.DropEntry> drops, BattleResult result) {
        if (drops == null || drops.isEmpty()) {
            System.out.println("Дропа нет");
            return;
        }

        CardCollection cardCollection = player.getCardCollection();
        SummonDeck summonDeck = player.getSummonDeck();
        CombatDeck combatDeck = player.getCombatDeck();

        boolean summonAdded = false;  // добавили ли уже суммон

        System.out.println("Обработка дропа: " + drops.size() + " элементов");

        for (CardDropService.DropEntry entry : drops) {
            if (entry.getSummonCard() != null) {
                if (!summonAdded) {  // Добавляем ТОЛЬКО ПЕРВЫЙ суммон
                    SummonCard summon = entry.getSummonCard();
                    cardCollection.addCard(summon);
                    summonDeck.addSummon(summon);
                    System.out.println("Дроп суммона: " + summon.getName());
                    summonAdded = true;  // Блокируем дальнейшее добавление
                } else {
                    System.out.println("Пропущен дублирующий суммон: " + entry.getSummonCard().getName());
                }
            }
            else if (entry.getCard() != null) {
                Card card = entry.getCard();
                cardCollection.addCard(card);
                combatDeck.addCard(card);
                System.out.println("Дроп карты: " + card.getId());
            }
            else if (entry.getItem() != null) {
                player.getInventory().addItem(entry.getItem());
                System.out.println("Дроп предмета: " + entry.getItem().getName());
            }
        }

        // Обновляем колоды после дропа
        summonDeck.updateFromCollection(cardCollection);
        combatDeck.updateFromCollection(cardCollection);

        System.out.println("После дропа в инвентаре предметов: " + player.getInventory().getSize());
    }

    private BattleReward createReward(int monsterLevel) {
        int exp = monsterLevel * 20 + (monsterLevel * 10);
        return new BattleReward(exp, List.of());
    }

    public BattleContext getContext() {
        return context;
    }
}