package MazeGame.battle;

import MazeGame.GameState;
import MazeGame.Monster;
import MazeGame.Player;
import MazeGame.UnitType;
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

    public BattleResult resolveTurn() {
        BattleResult result = new BattleResult();

        // 1. Начало хода — эффекты
        playerSide.onTurnStart(context);
        if (summonSide != null) summonSide.onTurnStart(context);
        enemySide.onTurnStart(context);

        // 2. Атака союзника → враг
        BattleSide activeAllySide = getActiveAllySide();
        if (activeAllySide != null && activeAllySide.isAlive() && enemySide.isAlive()) {
            int damage = DamageCalculator.calculate(activeAllySide, enemySide);
            enemySide.takeDamage(damage);
//            result.addMessage("⚔ " + activeAllySide.getName()
//                    + " наносит " + damage + " урона врагу");
        }

        // 3. Атака врага → союзник
        if (enemySide.isAlive()) {
            BattleSide target = getActiveAllySide();
            if (target != null && target.isAlive()) {
                int damage = DamageCalculator.calculate(enemySide, target);
                target.takeDamage(damage);
//                result.addMessage("🐲 " + enemySide.getName()
//                        + " наносит " + damage + " → " + target.getName());
            }
        }

        // 4. Конец хода — эффекты
        playerSide.onTurnEnd(context);
        if (summonSide != null) summonSide.onTurnEnd(context);
        enemySide.onTurnEnd(context);

        // 5. Проверка смерти суммона
        handleSummonDeath(result);

        // 6. Проверка конца боя
        handleBattleEnd(result);

        return result;
    }

    private void handleSummonDeath(BattleResult result) {
        if (summonSide == null || summonSide.isAlive()) return;

        Monster deadSummon = (Monster) summonSide.getUnit();
        result.addMessage("☠ " + deadSummon.getName() + " погиб!");

        Player player = (Player) playerSide.getUnit();
        SummonDeck summonDeck = player.getSummonDeck();
        CardCollection collection = player.getCardCollection();

        UnitType type = deadSummon.getUnitType();
        SummonCard lost = summonDeck.getActiveByType(type);
        summonDeck.removeSummon(type);

        if (lost != null) {
            collection.removeCard(lost);
            result.addMessage("Карта суммона потеряна навсегда: " + lost.getName());
        }

        summonSide = null;
        context.setSummon(null);
        context.setSummonSide(null);
    }

    private void handleBattleEnd(BattleResult result) {
        if (!enemySide.isAlive()) {
            result.setPlayerWin();
            BattleReward reward = createReward(enemySide.getUnit().getLevel());
            result.setReward(reward);

            Player player = (Player) playerSide.getUnit();
            player.gainExperience(reward.experience());

            result.addMessage("⭐ Получено опыта: " + reward.experience());

            Monster enemy = (Monster) enemySide.getUnit();
            var drops = new CardDropService().generateDrop(enemy);
            processDroppedCards((Player) playerSide.getUnit(), drops, result);
        }
        else if (!playerSide.isAlive()) {
            result.setPlayerLose();
        }

        if (result.isBattleOver()) {
            playerSide.getUnit().clearTemporaryEffects();
            if (summonSide != null) summonSide.getUnit().clearTemporaryEffects();

            Player player = (Player) playerSide.getUnit();
            player.getSummonDeck().resetSelection();
            player.getCombatDeck().resetBattleUsage();

            GameState.get().combat().clear();
        }
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

    public void playCard(Card card, CardTarget target, BattleResult result) {
        if (!playerSide.isAlive()) return;

        switch (target) {
            case PLAYER -> card.playOnPlayer(context, result);
            case SUMMON -> card.playOnSummon(context, result);
            case ENEMY  -> card.playOnEnemy(context, result);
        }

        // помечаем карту неактивной
        Player player = (Player) playerSide.getUnit();
        player.getCombatDeck().markUsed(card.getEffect());
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

}