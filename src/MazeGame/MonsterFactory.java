package MazeGame;

import MazeGame.cards.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MonsterFactory {

    private static final Random RANDOM = new Random();

    private MonsterFactory() {
    }

    // ──────────────────────────────────────────────────────────────
    // Уровень 1
    // ──────────────────────────────────────────────────────────────

    public static MonsterTemplate SPIDER_THROWER() {
        return new MonsterTemplate(100, "Паук-метатель", 1, 12, 2, 0,
                UnitType.ARCHER, "/images/monsters/spider.jpg");
    }

    public static MonsterTemplate BAT() {
        return new MonsterTemplate(120, "Летучая мышь", 1, 10, 3, 0,
                UnitType.FLYING, "/images/monsters/bat.jpg");
    }

    public static MonsterTemplate WILD_WOLF() {
        return new MonsterTemplate(140, "Дикий волк", 1, 15, 1, 1,
                UnitType.INFANTRY, "/images/monsters/wolf.jpg");
    }

    // ──────────────────────────────────────────────────────────────
    // Уровень 2
    // ──────────────────────────────────────────────────────────────

    public static MonsterTemplate GOBLIN_WARRIOR() {
        return new MonsterTemplate(160, "Гоблин-воин", 2, 20, 4, 1,
                UnitType.INFANTRY, "/images/monsters/goblin_warrior.jpg");
    }

    public static MonsterTemplate SKELETON_ARCHER() {
        return new MonsterTemplate(180, "Скелет-лучник", 2, 18, 5, 2,
                UnitType.ARCHER, "/images/monsters/skeleton_archer.jpg");
    }

    public static MonsterTemplate IMP() {
        return new MonsterTemplate(200, "Имп", 2, 22, 4, 1,
                UnitType.FLYING, "/images/monsters/imp.jpg");
    }

    // ──────────────────────────────────────────────────────────────
    // Уровень 3
    // ──────────────────────────────────────────────────────────────

    public static MonsterTemplate ORC() {
        return new MonsterTemplate(220, "Орк", 3, 35, 7, 2,
                UnitType.INFANTRY, "/images/monsters/orc.jpg");
    }

    public static MonsterTemplate ZOMBIE_HEAD() {
        return new MonsterTemplate(240, "Голова зомби", 3, 40, 5, 3,
                UnitType.FLYING, "/images/monsters/zombie_head.jpg");
    }

    public static MonsterTemplate WEREWOLF() {
        return new MonsterTemplate(260, "Оборотень", 3, 38, 8, 2,
                UnitType.ARCHER, "/images/monsters/werewolf.jpg");
    }

    // ──────────────────────────────────────────────────────────────
    // Уровень 4
    // ──────────────────────────────────────────────────────────────

    public static MonsterTemplate VAMPIRE() {
        return new MonsterTemplate(280, "Вампир", 4, 55, 9, 3,
                UnitType.FLYING, "/images/monsters/vampire.jpg");
    }

    public static MonsterTemplate CYCLOPS() {
        return new MonsterTemplate(300, "Циклоп", 4, 60, 8, 5,
                UnitType.ARCHER, "/images/monsters/cyclops.jpg");
    }

    public static MonsterTemplate DEATH_KNIGHT() {
        return new MonsterTemplate(320, "Рыцарь смерти", 4, 65, 7, 6,
                UnitType.INFANTRY, "/images/monsters/death_knight.jpg");
    }

    // ──────────────────────────────────────────────────────────────
    // Уровень 5
    // ──────────────────────────────────────────────────────────────

    public static MonsterTemplate DRAGONLING() {
        return new MonsterTemplate(340, "Дракончик", 5, 80, 12, 5,
                UnitType.FLYING, "/images/monsters/dragonling.jpg");
    }

    public static MonsterTemplate DEMON_LORD() {
        return new MonsterTemplate(360, "Владыка демонов", 5, 75, 13, 4,
                UnitType.ARCHER, "/images/monsters/demon_lord.jpg");
    }

    public static MonsterTemplate MINOTAUR_KING() {
        return new MonsterTemplate(380, "Король минотавров", 5, 85, 10, 7,
                UnitType.INFANTRY, "/images/monsters/minotaur_king.jpg");
    }

    // ──────────────────────────────────────────────────────────────
    // Список всех базовых шаблонов
    // ──────────────────────────────────────────────────────────────

    public static final List<MonsterTemplate> ALL_TEMPLATES = List.of(
            SPIDER_THROWER(),
            BAT(),
            WILD_WOLF(),
            GOBLIN_WARRIOR(),
            SKELETON_ARCHER(),
            IMP(),
            ORC(),
            ZOMBIE_HEAD(),
            WEREWOLF(),
            VAMPIRE(),
            CYCLOPS(),
            DEATH_KNIGHT(),
            DRAGONLING(),
            DEMON_LORD(),
            MINOTAUR_KING()
    );

    // ──────────────────────────────────────────────────────────────
    // Основные публичные методы
    // ──────────────────────────────────────────────────────────────

    /**
     * Создаёт врага подходящего уровня для игрока
     */
    public static Monster createEnemyForPlayer(int playerLevel) {
        MonsterTemplate template = pickTemplate(playerLevel);
        return new Monster(template);
    }

    /**
     * Выбор случайного подходящего шаблона
     */
    private static MonsterTemplate pickTemplate(int playerLevel) {
        List<MonsterTemplate> candidates = ALL_TEMPLATES.stream()
                .filter(t -> t.level() >= playerLevel - 1 && t.level() <= playerLevel + 1)
                .collect(Collectors.toList());

        if (candidates.isEmpty()) {
            // запасной вариант — самый слабый монстр
            return ALL_TEMPLATES.get(0);
        }

        return candidates.get(RANDOM.nextInt(candidates.size()));
    }

    /**
     * Получить все шаблоны определённого уровня
     */
    public static List<MonsterTemplate> getByLevel(int level) {
        return ALL_TEMPLATES.stream()
                .filter(t -> t.level() == level)
                .collect(Collectors.toList());
    }

    /**
     * Получить шаблон по id
     */
    public static MonsterTemplate getById(int id) {
        return ALL_TEMPLATES.stream()
                .filter(t -> t.id() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Не найден MonsterTemplate с id: " + id));
    }
}