package MazeGame.battle;

import MazeGame.Monster;
import MazeGame.Player;


//+
public class BattleContext {

    private final Player player;
    private final Monster enemy;
    private Monster summon;
    private final BattleResult result;
    private final BattleSide playerSide;
    private final BattleSide enemySide;

    public BattleContext(Player player, Monster enemy) {
        this.player = player;
        this.enemy = enemy;
        this.playerSide = new BattleSide((BattleUnit) player);
        this.enemySide = new BattleSide(enemy);
        this.result = new BattleResult();
    }

    public void addMessage(String msg) {
        result.addMessage(msg);
    }

    public Player getPlayer() { return player; }
    public Monster getEnemy() { return enemy; }
    public Monster getSummon() { return summon; }

    public BattleSide getPlayerSide() {
        return playerSide;
    }

    public BattleSide getEnemySide() {
        return enemySide;
    }

    public void setSummon(Monster summon) {
        this.summon = summon;
    }
}
