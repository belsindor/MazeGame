package MazeGame.cards;

import MazeGame.UnitType;

public record SummonEntry(
        int baseId,           // 10, 12, 14, 16...
        int upgradeLevel,     // 0 = базовый, 1 = первый апгрейд, 2 = второй и т.д.
        String name,
        int level,
        int hp,
        int damage,
        int armor,
        UnitType type,
        String imagePath,
        CardRarity rarity
) {
    public int getEffectiveId() {
        return baseId + upgradeLevel;
    }
}