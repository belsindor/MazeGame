package MazeGame.battle;


import MazeGame.item.Item;

import java.util.List;

public class BattleReward {

    private final int experience;
    private final List<Item> items;

    public BattleReward(int experience, List<Item> items) {
        this.experience = experience;
        this.items = items;
    }

    public int getExperience() {
        return experience;
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return experience == 0 && items.isEmpty();
    }
}
