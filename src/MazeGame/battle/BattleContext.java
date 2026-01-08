package MazeGame.battle;

import MazeGame.Monster;
import MazeGame.Player;

public class BattleContext {

    private final Player player;
    private final Monster enemy;
    private Monster summon;

    public BattleContext(Player player, Monster enemy) {
        this.player = player;
        this.enemy = enemy;
    }


    public Player getPlayer() { return player; }
    public Monster getEnemy() { return enemy; }
    public Monster getSummon() { return summon; }

    public void setSummon(Monster summon) {
        this.summon = summon;
    }
}
