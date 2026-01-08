package MazeGame.cards;

import java.awt.*;

public enum CardRarity {

    GRAY(Color.GRAY),
    GREEN(new Color(0, 180, 0)),
    BLUE(new Color(0, 120, 255)),
    YELLOW(new Color(220, 200, 0)),
    RED(Color.RED);

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
            case BLUE -> YELLOW;
            case YELLOW -> RED;
            case RED -> RED;
        };
    }

    public static CardRarity fromLevel(int level) {
        return switch (level) {
            case 1 -> GRAY;
            case 2 -> GREEN;
            case 3 -> BLUE;
            case 4 -> YELLOW;
            default -> RED;
        };
    }

}
