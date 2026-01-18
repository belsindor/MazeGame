package MazeGame.cards;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleResult;

import javax.swing.*;
import java.util.Objects;
//++
public abstract class Card {

    protected final CardType type;
    protected final CardRarity rarity;
    protected final String imagePath;
    protected int copies = 1;
    protected int id;

    protected Card(int id, CardType type, CardRarity rarity, TypeEffect effect, String imagePath) {
        this.id = id;
        this.type = type;
        this.rarity = rarity;
        this.imagePath = imagePath;
    }

    protected Card(CardRarity rarity, TypeEffect effect) {
        this.type = null; // или нужен дефолтный тип
        this.rarity = rarity;
        this.imagePath = "";
    }

    public CardRarity getRarity() {
        return rarity;
    }

    public int getId() {
        return id;
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
        return new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
    }

    protected Card(int id, CardType type, CardRarity rarity, TypeEffect effect) {
        this(id, type, rarity, effect,"");
    }


}
