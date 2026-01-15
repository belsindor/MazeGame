package MazeGame.cards;

import MazeGame.MonsterTemplate;
import MazeGame.UnitType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Центральное место регистрации всех возможных суммонов в игре.
 * Каждый суммон имеет уникальный id.
 */
public final class SummonLibrary {

    private static final Map<Integer, SummonEntry> SUMMONS = new HashMap<>();

    static {
        // Уровень 1 ── Common / Uncommon
        register(10, "Паук-метатель",      1, 12, 2, 0, UnitType.ARCHER,   "/images/monsters/spider.jpg",      CardRarity.GRAY);
        register(11, "Паук-метатель",      1, 12, 2, 0, UnitType.ARCHER,   "/images/monsters/spider.jpg",      CardRarity.GREEN);

        register(12, "Летучая мышь",       1, 10, 3, 0, UnitType.FLYING,   "/images/monsters/bat.jpg",         CardRarity.GRAY);
        register(13, "Летучая мышь",       1, 10, 3, 0, UnitType.FLYING,   "/images/monsters/bat.jpg",         CardRarity.GREEN);

        register(14, "Дикий волк",         1, 15, 1, 1, UnitType.INFANTRY, "/images/monsters/wolf.jpg",        CardRarity.GRAY);
        register(15, "Дикий волк",         1, 15, 1, 1, UnitType.INFANTRY, "/images/monsters/wolf.jpg",        CardRarity.GREEN);

        // Уровень 2
        register(16, "Гоблин-воин",        2, 20, 4, 1, UnitType.INFANTRY, "/images/monsters/goblin_warrior.jpg", CardRarity.GREEN);
        register(17, "Гоблин-воин",        2, 20, 4, 1, UnitType.INFANTRY, "/images/monsters/goblin_warrior.jpg", CardRarity.BLUE);

        register(18, "Скелет-лучник",      2, 18, 5, 2, UnitType.ARCHER,   "/images/monsters/skeleton_archer.jpg",CardRarity.GREEN);
        register(19, "Скелет-лучник",      2, 18, 5, 2, UnitType.ARCHER,   "/images/monsters/skeleton_archer.jpg",CardRarity.BLUE);

        register(20, "Имп",                2, 22, 4, 1, UnitType.FLYING,   "/images/monsters/imp.jpg",         CardRarity.GREEN);
        register(21, "Имп",                2, 22, 4, 1, UnitType.FLYING,   "/images/monsters/imp.jpg",         CardRarity.BLUE);

        // Уровень 3
        register(22, "Орк",                3, 35, 7, 2, UnitType.INFANTRY, "/images/monsters/orc.jpg",         CardRarity.BLUE);
        register(23, "Орк",                3, 35, 7, 2, UnitType.INFANTRY, "/images/monsters/orc.jpg",         CardRarity.VIOLETTE);

        register(24, "Голова зомби",       3, 40, 5, 3, UnitType.FLYING,   "/images/monsters/zombie_head.jpg", CardRarity.BLUE);
        register(25, "Голова зомби",       3, 40, 5, 3, UnitType.FLYING,   "/images/monsters/zombie_head.jpg", CardRarity.VIOLETTE);

        register(26, "Оборотень",          3, 38, 8, 2, UnitType.ARCHER,   "/images/monsters/werewolf.jpg",    CardRarity.BLUE);
        register(27, "Оборотень",          3, 38, 8, 2, UnitType.ARCHER,   "/images/monsters/werewolf.jpg",    CardRarity.VIOLETTE);

        // Уровень 4
        register(28, "Вампир",             4, 55, 9, 3, UnitType.FLYING,   "/images/monsters/vampire.jpg",     CardRarity.VIOLETTE);
        register(29, "Вампир",             4, 55, 9, 3, UnitType.FLYING,   "/images/monsters/vampire.jpg",     CardRarity.RED);

        register(30, "Циклоп",             4, 60, 8, 5, UnitType.ARCHER,   "/images/monsters/cyclops.jpg",     CardRarity.VIOLETTE);
        register(31, "Циклоп",             4, 60, 8, 5, UnitType.ARCHER,   "/images/monsters/cyclops.jpg",     CardRarity.RED);

        register(32, "Рыцарь смерти",      4, 65, 7, 6, UnitType.INFANTRY, "/images/monsters/death_knight.jpg",CardRarity.VIOLETTE);
        register(33, "Рыцарь смерти",      4, 65, 7, 6, UnitType.INFANTRY, "/images/monsters/death_knight.jpg",CardRarity.RED);

        // Уровень 5 ── Epic / Legendary
        register(34, "Дракончик",          5, 80, 12, 5, UnitType.FLYING,  "/images/monsters/dragonling.jpg",  CardRarity.RED);
        register(35, "Дракончик",          5, 80, 12, 5, UnitType.FLYING,  "/images/monsters/dragonling.jpg",  CardRarity.GOLD);

        register(36, "Владыка демонов",    5, 75, 13, 4, UnitType.ARCHER,  "/images/monsters/demon_lord.jpg",  CardRarity.RED);
        register(37, "Владыка демонов",    5, 75, 13, 4, UnitType.ARCHER,  "/images/monsters/demon_lord.jpg",  CardRarity.GOLD);

        register(38, "Король минотавров",  5, 85, 10, 7, UnitType.INFANTRY,"/images/monsters/minotaur_king.jpg",CardRarity.RED);
        register(39, "Король минотавров",  5, 85, 10, 7, UnitType.INFANTRY,"/images/monsters/minotaur_king.jpg",CardRarity.GOLD);
    }

    private static void register(int id, String name, int level, int hp, int damage, int armor,
                                 UnitType type, String imagePath, CardRarity rarity) {
        SummonEntry entry = new SummonEntry(id, name, level, hp, damage, armor, type, imagePath, rarity);
        if (SUMMONS.put(id, entry) != null) {
            throw new IllegalStateException("Дубликат summon id: " + id);
        }
    }

    // ── Основные публичные методы ───────────────────────────────────────

    public static Optional<SummonEntry> getById(int id) {
        return Optional.ofNullable(SUMMONS.get(id));
    }

    public static SummonEntry getByIdOrThrow(int id) {
        return getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный summon id: " + id));
    }

    public static SummonCard createSummonCard(int id) {
        SummonEntry e = getByIdOrThrow(id);
        return new SummonCard(e.toMonsterTemplate());
    }

    // Полезно для тестов, отладки, будущей генерации колод и т.д.
    public static Map<Integer, SummonEntry> getAllSummons() {
        return new HashMap<>(SUMMONS);
    }

    // ------------------------------------------------------------------------

    /**
     * Внутренний immutable контейнер со всей информацией о суммоне
     */
    public record SummonEntry(
            int id,
            String name,
            int level,
            int hp,
            int damage,
            int armor,
            UnitType type,
            String imagePath,
            CardRarity rarity
    ) {
        public MonsterTemplate toMonsterTemplate() {
            return new MonsterTemplate(name, level, hp, damage, armor, type, imagePath);
        }

        public SummonCard toSummonCard() {
            return new SummonCard(toMonsterTemplate());
        }
    }

    public static int getBaseSummonId(int summonId) {
        return (summonId / 2) * 10;  // или summonId - (summonId % 2)
    }

    public static boolean isUpgraded(int summonId) {
        return summonId % 2 == 1;
    }

    public static int getBaseId(int summonId) {
        return summonId - (summonId % 2);  // или summonId & ~1
    }

    public static int getUpgradedId(int baseId) {
        if (baseId % 2 != 0) {
            throw new IllegalArgumentException("Ожидался чётный baseId, получен: " + baseId);
        }
        return baseId + 1;
    }

}
