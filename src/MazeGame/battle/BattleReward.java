package MazeGame.battle;


import MazeGame.item.Item;

import java.util.List;
//+
public record BattleReward(int experience, List<Item> items) {

    public boolean isEmpty() {
        return experience == 0 && items.isEmpty();
    }
}
