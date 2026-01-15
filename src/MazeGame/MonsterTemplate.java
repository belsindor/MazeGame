package MazeGame;

public record MonsterTemplate(
        int Id,
        String name,
        int level,
        int maxHealth,
        int attack,
        int defense,
        UnitType unitType,
        String imagePath
) {}
