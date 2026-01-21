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
        this.playerSide = new BattleSide(player);
        this.enemySide = new BattleSide(monster);
        this.context = new BattleContext(player, monster);
    }

    public BattleResult resolveTurn(PlayerTurn turn) {
        BattleResult result = new BattleResult();

        // Начало хода
        playerSide.onTurnStart(context);
        enemySide.onTurnStart(context);

        Monster summon = context.getSummon();
        BattleUnit activeAttacker = null;

        // Определяем, кто сейчас дерётся
        if (summon != null && summon.isAlive()) {
            summon.onTurnStart(context);
            activeAttacker = summon; // суммон дерётся
        } else {
            activeAttacker = playerSide.getUnit(); // суммон мёртв — дерётся игрок
        }

        // Ход игрока (карта применяется всегда, даже если дерётся суммон)
        if (playerSide.isAlive()) {
            turn.apply(context, result);
        }

        // Атака активного юнита (суммон или игрок)
        if (activeAttacker.isAlive() && enemySide.isAlive()) {
            int dmg = DamageCalculator.calculate(activeAttacker, enemySide.getUnit());
            enemySide.takeDamage(dmg);
            result.addMessage("⚔ " + activeAttacker.getName() + " наносит " + dmg);
        }

        // Атака врага — только на активного юнита
        if (enemySide.isAlive()) {
            BattleUnit target = (summon != null && summon.isAlive()) ? summon : playerSide.getUnit();
            int dmg = DamageCalculator.calculate(enemySide.getUnit(), target);
            target.takeDamage(dmg);
            result.addMessage("🐲 " + enemySide.getName() + " наносит " + dmg + " → " + target.getName());
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

            Monster enemyMonster = (Monster) enemySide.getUnit();
            List<CardDropService.DropEntry> drops = new CardDropService().generateDrop(enemyMonster);
            processDroppedCards((Player) playerSide.getUnit(), drops, result);
        } else if (!playerSide.isAlive() && (summon == null || !summon.isAlive())) {
            // Поражение только если и игрок, и суммон мертвы
            result.setPlayerLose();
            battleEnded = true;
        }

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

    private void processDroppedCards(Player player, List<CardDropService.DropEntry> drops, BattleResult result) {
        // ... (без изменений, оставляем как было)
    }

    private BattleReward createReward(int monsterLevel) {
        int exp = monsterLevel * 20 + (monsterLevel * 10);
        return new BattleReward(exp, List.of());
    }

    public BattleContext getContext() {
        return context;
    }
}