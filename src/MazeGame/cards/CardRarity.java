package MazeGame.cards;

import java.awt.*;

public enum CardRarity {

    GRAY,
    GREEN,
    BLUE,
    VIOLETTE,
    RED,
    GOLD;

    public CardRarity next() {
        return switch (this) {
            case GRAY -> GREEN;
            case GREEN -> BLUE;
            case BLUE -> VIOLETTE;
            case VIOLETTE -> RED;
            case RED -> GOLD;
            case GOLD -> null;
        };
    }

    public boolean canUpgrade() {
        return this != GOLD;
    }

    public boolean isHigherThan(CardRarity other) {
        return this.ordinal() > other.ordinal();
    }

    public static CardRarity fromLevel(int level) {
        return switch (level) {
            case 1 -> GRAY;
            case 2 -> GREEN;
            case 3 -> BLUE;
            case 4 -> VIOLETTE;
            case 5 -> RED;
            default -> GOLD;
        };
    }
}
