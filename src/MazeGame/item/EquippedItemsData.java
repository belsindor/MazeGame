package MazeGame.item;

import java.io.Serializable;


public class EquippedItemsData implements Serializable {
    private static final long serialVersionUID = 1L;
    public Item weapon;
    public Item top;
    public Item bottom;

    public EquippedItemsData(Item weapon, Item top, Item bottom) {
        this.weapon = weapon;
        this.top = top;
        this.bottom = bottom;
    }

    public EquippedItemsData() {

    }
}
