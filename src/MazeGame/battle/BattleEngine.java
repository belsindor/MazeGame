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

    /**
     * Конструктор без SummonFactory — суммон уже выбран и передан в context извне
     */
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

            List<CardDropService.DropEntry> drops = new CardDropService().generateDrop(enemySide.getUnit().getLevel());
            processDroppedCards((Player) playerSide.getUnit(), drops);
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

            // Сбрасываем выбор суммона после завершения боя
            playerSide.getUnit().getSummonDeck().resetSelection();
        }

        GameState.get().combat().clear();

        return result;
    }

    private void processDroppedCards(Player player, List<CardDropService.DropEntry> drops) {
        if (drops == null || drops.isEmpty()) return;

        CardCollection cardCollection = player.getCardCollection();
        SummonDeck summonDeck = player.getSummonDeck();

        for (CardDropService.DropEntry entry : drops) {
            if (entry.getSummonCard() != null) {
                summonDeck.addSummon(entry.getSummonCard());
            } else if (entry.getCard() != null) {
                cardCollection.addCard(entry.getCard());
            } else if (entry.getItem() != null) {
                player.addItem(entry.getItem());
            }
        }
    }

    private BattleReward createReward(int monsterLevel) {
        int exp = monsterLevel * 20 + (monsterLevel * 10); // можно усложнить формулу
        return new BattleReward(exp, List.of()); // предметы пока не добавляем в базовую награду
    }

    // Полезный геттер для доступа к контексту (нужен в BattleWindow)
    public BattleContext getContext() {
        return context;
    }
}