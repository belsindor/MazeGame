package MazeGame.cards;

import java.awt.*;

public enum CardRarity {

    GRAY(new Color(255, 255, 255)),
    GREEN(new Color(0, 180, 0)),
    BLUE(new Color(0, 120, 255)),
    VIOLETTE(new Color(148, 0, 211)),
    RED(new Color(148, 0, 0)),
    GOLD(new Color(255, 215, 0));

    private final Color frameColor;

    CardRarity(Color frameColor) {
        this.frameColor = frameColor;
    }

    public Color getFrameColor() {
        return frameColor;
    }

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
