package MazeGame;

public record MonsterTemplate(
        String name,
        int level,
        int maxHealth,
        int attack,
        int defense,
        UnitType unitType,
        String imagePath
) {}
