package MazeGame.battle;

import MazeGame.Monster;
import MazeGame.Player;



public class BattleContext {

    private final Player player;
    private final Monster enemy;
    private Monster summon;
    private BattleResult result;
    private BattleSide playerSide;
    private BattleSide enemySide;

    public BattleContext(Player player, Monster enemy) {
        this.player = player;
        this.enemy = enemy;
        this.playerSide = new BattleSide(player);
        this.enemySide = new BattleSide(enemy);
    }
    public void addMessage(String msg) {
        result.addMessage(msg);
    }



    public Player getPlayer() { return player; }
    public Monster getEnemy() { return enemy; }
    public Monster getSummon() { return summon; }

    public BattleSide getPlayerSide() {
        return null;
    }

    public BattleSide getEnemySide() {
        return null;
    }

    public void setSummon(Monster summon) {
        this.summon = summon;
    }
}
