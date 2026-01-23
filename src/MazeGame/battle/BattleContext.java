package MazeGame.battle;

import MazeGame.Monster;
import MazeGame.Player;

public class BattleContext {

    private final BattleSide playerSide;
    private final BattleSide enemySide;

    private Monster summon;
    private BattleSide summonSide;

    // Убрали поле result — он создаётся в каждом resolveTurn


    private BattleResult currentResult;  // не final, будем менять каждый ход

    public BattleContext(BattleSide playerSide, BattleSide enemySide) {
        this.playerSide = playerSide;
        this.enemySide  = enemySide;
        this.currentResult = new BattleResult();  // или null, см. ниже
    }

    public void setCurrentResult(BattleResult result) {
        this.currentResult = result;
    }

    public void addMessage(String msg) {
        if (currentResult != null) {
            currentResult.addMessage(msg);
        }
    }

    // Геттеры
    public BattleSide getPlayerSide() { return playerSide; }
    public BattleSide getEnemySide()  { return enemySide;  }
    public BattleSide getSummonSide() { return summonSide; }

    public Player  getPlayer() { return (Player)  playerSide.getUnit(); }
    public Monster getEnemy()  { return (Monster) enemySide.getUnit();  }
    public Monster getSummon() { return summon; }

    // Active ally — самый важный метод для карт
    public BattleSide getActiveAllySide() {
        if (summonSide != null && summonSide.isAlive()) {
            return summonSide;
        }
        return playerSide.isAlive() ? playerSide : null;
    }

    public BattleUnit getActiveAlly() {
        BattleSide side = getActiveAllySide();
        return side != null ? side.getUnit() : null;
    }

    // Сеттеры для суммона
    public void setSummon(Monster summon) {
        this.summon = summon;
    }

    public void setSummonSide(BattleSide side) {
        this.summonSide = side;
    }
}