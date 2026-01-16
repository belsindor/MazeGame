package MazeGame.item;

import java.io.Serializable;

//+
public class EquippedItemsData implements Serializable {
    private static final long serialVersionUID = 1L;
    public Item weapon;
    public Item top;
    public Item bottom;
    public Item shield;

    public EquippedItemsData(Item weapon, Item top, Item bottom, Item sheld) {
        this.weapon = weapon;
        this.top = top;
        this.bottom = bottom;
        this.shield = sheld;
    }

    public EquippedItemsData() {

    }
}
