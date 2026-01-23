package MazeGame.battle;

import MazeGame.GameState;
import MazeGame.Monster;
import MazeGame.Player;
import MazeGame.cards.*;
import MazeGame.item.Item;

import java.util.List;

public class BattleEngine {

    private final BattleSide playerSide;
    private final BattleSide enemySide;
    private BattleSide summonSide;          // null если суммон не выбран или погиб
    private final BattleContext context;

    public BattleEngine(Player player, Monster enemy) {
        this.playerSide = new BattleSide(player);
        this.enemySide  = new BattleSide(enemy);
        this.context    = new BattleContext(playerSide, enemySide);
    }

    public void setSummon(Monster summon) {
        if (summon != null) {
            this.summonSide = new BattleSide(summon);
            context.setSummon(summon);
            context.setSummonSide(summonSide);
        } else {
            this.summonSide = null;
            context.setSummon(null);
            context.setSummonSide(null);
        }
    }

    public BattleResult resolveTurn(PlayerTurn turn) {
        BattleResult result = new BattleResult();

        // 1. Начало хода — эффекты на всех сторонах
        playerSide.onTurnStart(context);
        if (summonSide != null) {
            summonSide.onTurnStart(context);
        }
        enemySide.onTurnStart(context);

        // 2. Применение карты (если выбрана) — всегда от лица игрока
        if (playerSide.isAlive()) {
            turn.apply(context, result);
        }

        // 3. Атака активного союзника (суммон или игрок) → враг
        BattleSide activeAllySide = getActiveAllySide();
        if (activeAllySide != null && activeAllySide.isAlive() && enemySide.isAlive()) {
            int damage = DamageCalculator.calculate(activeAllySide, enemySide);
            enemySide.takeDamage(damage);
            result.addMessage("⚔ " + activeAllySide.getName() + " наносит " + damage + " урона врагу");
        }

        // 4. Атака врага → текущий активный союзник
        if (enemySide.isAlive()) {
            BattleSide target = getActiveAllySide();
            if (target != null && target.isAlive()) {
                int damage = DamageCalculator.calculate(enemySide, target);
                target.takeDamage(damage);
                result.addMessage("🐲 " + enemySide.getName() + " наносит " + damage + " → " + target.getName());
            }
        }

        // 5. Конец хода — эффекты и удаление истёкших
        playerSide.onTurnEnd(context);
        if (summonSide != null) {
            summonSide.onTurnEnd(context);
        }
        enemySide.onTurnEnd(context);

        // 6. Проверка смерти суммона → удаление из колоды
        if (summonSide != null && !summonSide.isAlive()) {
            Monster deadSummon = (Monster) summonSide.getUnit();
            result.addMessage("☠ " + deadSummon.getName() + " погиб!");

            SummonDeck summonDeck = ((Player) playerSide.getUnit()).getSummonDeck();

            // Вариант А: удаляем по типу (самый простой, если один суммон на тип)
            summonDeck.removeSummon(deadSummon.getUnitType());

            // Вариант Б: если хочешь точнее — ищи по имени или другим полям (менее надёжно)
            // summonDeck.removeFromActive(deadSummon);

            summonSide = null;
            context.setSummon(null);
            context.setSummonSide(null);
        }

        // 7. Проверка конца боя
        if (!enemySide.isAlive()) {
            result.setPlayerWin();

            BattleReward reward = createReward(enemySide.getUnit().getLevel());
            result.setReward(reward);

            Monster enemyMonster = (Monster) enemySide.getUnit();
            List<CardDropService.DropEntry> drops = new CardDropService().generateDrop(enemyMonster);
            processDroppedCards((Player) playerSide.getUnit(), drops, result);
        }
        else if (!playerSide.isAlive()) {
            // Поражение только если сам игрок мёртв
            result.setPlayerLose();
        }

        // Финальная очистка только при окончании боя
        if (result.isBattleOver()) {
            playerSide.getUnit().clearTemporaryEffects();
            if (summonSide != null) {
                summonSide.getUnit().clearTemporaryEffects();
            }
            ((Player) playerSide.getUnit()).getSummonDeck().resetSelection();
            GameState.get().combat().clear();
        }

        return result;
    }

    private BattleSide getActiveAllySide() {
        if (summonSide != null && summonSide.isAlive()) {
            return summonSide;
        }
        return playerSide.isAlive() ? playerSide : null;
    }

    private BattleReward createReward(int monsterLevel) {
        int exp = monsterLevel * 20 + (monsterLevel * 10);
        return new BattleReward(exp, List.of());
    }

    private void processDroppedCards(Player player, List<CardDropService.DropEntry> drops, BattleResult result) {
        for (CardDropService.DropEntry drop : drops) {
            if (drop.getSummonCard() != null) {
                SummonCard sc = drop.getSummonCard();
                player.getCardCollection().addCard(sc);
                result.addMessage("Получена суммон-карта: " + sc.getName());
            } else if (drop.getCard() != null) {
                Card c = drop.getCard();
                player.getCardCollection().addCard(c);
                result.addMessage("Получена карта: " + c.getName());
            } else if (drop.getItem() != null) {
                Item it = drop.getItem();
                player.getInventory().addItem(it);
                result.addMessage("Получен предмет: " + it.getName());
            }
        }
        player.getSummonDeck().refreshActive(player.getCardCollection());
        player.getCombatDeck().refreshActive(player.getCardCollection());
    }

    public BattleContext getContext() {
        return context;
    }

    public BattleSide getPlayerSide() {
        return playerSide;
    }

    public BattleSide getEnemySide() {
        return enemySide;
    }

    public BattleSide getSummonSide() {
        return summonSide;
    }



    // Если хочешь, можно добавить и другие полезные геттеры



}