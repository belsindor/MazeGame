package MazeGame.cards;

public abstract class Card {

    protected CardRarity rarity;
    protected int copies = 1;

    protected Card(CardRarity rarity) {
        this.rarity = rarity;
    }

    public abstract CardType getType();

    public CardRarity getRarity() {
        return rarity;
    }

    public int getCopies() {
        return copies;
    }
}
