package MazeGame;

public record MonsterTemplate(
        int id,
        String name,
        int level,
        int maxHealth,
        int attack,
        int defense,
        UnitType unitType,
        String imagePath
) {}
