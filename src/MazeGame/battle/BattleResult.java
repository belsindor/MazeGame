package MazeGame.battle;

import java.util.ArrayList;
import java.util.List;

public class BattleResult {

    // текстовые сообщения для HUD
    public final List<String> messages = new ArrayList<>();

    // урон за ход
    public int playerDamage = 0;
    public int monsterDamage = 0;

    // состояние боя
    private boolean battleOver = false;

    // ===== API =====

    public void addMessage(String msg) {
        messages.add(msg);
    }

    public void setBattleOver() {
        this.battleOver = true;
    }

    public boolean isBattleOver() {
        return battleOver;
    }

}
