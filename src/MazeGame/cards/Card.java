package MazeGame.cards;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleResult;

import javax.swing.*;

public abstract class Card {

    protected final CardType type;
    protected final CardRarity rarity;
    protected final String imagePath;
    protected int copies = 1;

    protected Card(CardType type, CardRarity rarity, String imagePath) {
        this.type = type;
        this.rarity = rarity;
        this.imagePath = imagePath;
    }

    protected Card(CardRarity rarity) {
        this.type = null; // или нужен дефолтный тип
        this.rarity = rarity;
        this.imagePath = "";
    }

    public CardRarity getRarity() {
        return rarity;
    }

    public int getCopies() {
        return copies;
    }
    public CardType getType() {
        return type;
    }
    public String getImagePath() {
        return imagePath;
    }

    public abstract void play(BattleContext context, BattleResult result);

    public ImageIcon getImageIcon() {
        return new ImageIcon(getClass().getResource(imagePath));
    }

    protected Card(CardType type, CardRarity rarity) {
        this(type, rarity, "");
    }


}
