package MazeGame.cards;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleResult;

import javax.swing.*;

public abstract class Card {

    protected final CardType type;
    protected final CardRarity rarity;
    protected final String imagePath;

    protected boolean upgraded = false;

    protected Card(CardType type, CardRarity rarity, String imagePath) {
        this.type = type;
        this.rarity = rarity;
        this.imagePath = imagePath;
    }

    public CardRarity getRarity() {
        return rarity;
    }

    public boolean isUpgraded() {
        return upgraded;
    }

    public void markUpgraded() {
        this.upgraded = true;
    }

    public CardType getType() {
        return type;
    }

    public abstract void play(BattleContext context, BattleResult result);

    public ImageIcon getImageIcon() {
        return new ImageIcon(getClass().getResource(imagePath));
    }
}
