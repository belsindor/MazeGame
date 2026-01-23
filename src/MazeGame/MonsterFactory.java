package MazeGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Фабрика для создания монстров по уровню игрока или заданному уровню
 */
public final class MonsterFactory {

    private static final Random RANDOM = new Random();

    private MonsterFactory() {} // приватный конструктор — фабрика статическая

    // ──────────────────────────────────────────────────────────────
    // Все базовые шаблоны монстров (уровни 1–5)
    // ──────────────────────────────────────────────────────────────

    public static final List<MonsterTemplate> ALL_TEMPLATES = List.of(
            new MonsterTemplate(100, "Паук-метатель",   1, 12, 2, 0, UnitType.ARCHER, "/images/monsters/spider.jpg"),
            new MonsterTemplate(120, "Летучая мышь",     1, 10, 3, 0, UnitType.FLYING, "/images/monsters/bat.jpg"),
            new MonsterTemplate(140, "Дикий волк",       1, 15, 2, 1, UnitType.INFANTRY, "/images/monsters/wolf.jpg"),

            new MonsterTemplate(160, "Гоблин-воин",      2, 20, 4, 1, UnitType.INFANTRY, "/images/monsters/goblin_warrior.jpg"),
            new MonsterTemplate(180, "Скелет-лучник",    2, 18, 5, 2, UnitType.ARCHER, "/images/monsters/skeleton_archer.jpg"),
            new MonsterTemplate(200, "Имп",              2, 22, 4, 1, UnitType.FLYING, "/images/monsters/imp.jpg"),

            new MonsterTemplate(220, "Орк",              3, 35, 7, 2, UnitType.INFANTRY, "/images/monsters/orc.jpg"),
            new MonsterTemplate(240, "Голова зомби",     3, 40, 5, 3, UnitType.FLYING, "/images/monsters/zombie_head.jpg"),
            new MonsterTemplate(260, "Оборотень",        3, 38, 8, 2, UnitType.ARCHER, "/images/monsters/werewolf.jpg"),

            new MonsterTemplate(280, "Вампир",           4, 55, 9, 3, UnitType.FLYING, "/images/monsters/vampire.jpg"),
            new MonsterTemplate(300, "Циклоп",           4, 60, 8, 5, UnitType.ARCHER, "/images/monsters/cyclops.jpg"),
            new MonsterTemplate(320, "Рыцарь смерти",    4, 65, 7, 6, UnitType.INFANTRY, "/images/monsters/death_knight.jpg"),

            new MonsterTemplate(340, "Дракончик",        5, 80, 12, 5, UnitType.FLYING, "/images/monsters/dragonling.jpg"),
            new MonsterTemplate(360, "Владыка демонов",  5, 75, 13, 4, UnitType.ARCHER, "/images/monsters/demon_lord.jpg"),
            new MonsterTemplate(380, "Король минотавров",5, 85, 10, 7, UnitType.INFANTRY, "/images/monsters/minotaur_king.jpg")
    );

    /**
     * Создаёт монстра уровня ≤ уровня игрока
     */
    public static Monster createEnemyForPlayer(int playerLevel) {
        // Уровень монстра: от 1 до playerLevel
        int enemyLevel = RANDOM.nextInt(playerLevel) + 1;

        return createEnemyForLevel(enemyLevel);
    }

    /**
     * Создаёт монстра строго указанного уровня
     * Если шаблонов на точный уровень нет — берёт ближайший ниже
     */
    public static Monster createEnemyForLevel(int level) {
        // Ищем все шаблоны на точный уровень
        List<MonsterTemplate> templates = ALL_TEMPLATES.stream()
                .filter(t -> t.level() == level)
                .collect(Collectors.toList());

        // Если нет — ищем ближайший уровень ниже (но не ниже 1)
        if (templates.isEmpty()) {
            int fallbackLevel = Math.max(1, level - 1);
            templates = ALL_TEMPLATES.stream()
                    .filter(t -> t.level() == fallbackLevel)
                    .collect(Collectors.toList());
        }

        // Если всё равно пусто — берём самого слабого (уровень 1)
        if (templates.isEmpty()) {
            templates = ALL_TEMPLATES.stream()
                    .filter(t -> t.level() == 1)
                    .collect(Collectors.toList());
        }

        if (templates.isEmpty()) {
            throw new IllegalStateException("Нет доступных шаблонов монстров!");
        }

        // Выбираем случайный из подходящих
        MonsterTemplate template = templates.get(RANDOM.nextInt(templates.size()));
        return new Monster(template);
    }

    /**
     * Получить все шаблоны на заданный уровень
     */
    public static List<MonsterTemplate> getTemplatesByLevel(int level) {
        return ALL_TEMPLATES.stream()
                .filter(t -> t.level() == level)
                .collect(Collectors.toList());
    }

    /**
     * Получить шаблон по ID
     */
    public static MonsterTemplate getById(int id) {
        return ALL_TEMPLATES.stream()
                .filter(t -> t.id() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Не найден MonsterTemplate с id: " + id));
    }
}