package MazeGame.cards;


import MazeGame.MonsterTemplate;
import MazeGame.UnitType;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public final class SummonFactory {

    private static final Map<Integer, SummonEntry> SUMMONS = new HashMap<>();

    private SummonFactory() {
    }
    // Уровень 0 ── Summon
    public static SummonCard ancestor_spirit() {
        return summonCard(10000,"Дух предка",1,6,1,0,
                UnitType.FLYING,"/cards/summon/ancestor_spirit.jpg", CardRarity.GRAY);
    }

    // Уровень 1 ── Summon
    public static SummonCard SPIDER_THROWER_GRAY() {
        return summonCard(10, "Паук-метатель", 1, 12, 2, 0,
                UnitType.ARCHER, "/images/monsters/spider.jpg", CardRarity.GRAY);
    }

    public static SummonCard SPIDER_THROWER_GREEN() {
        return summonCard(11, "Паук-метатель", 1, 12, 2, 0,
                UnitType.ARCHER, "/images/monsters/spider.jpg", CardRarity.GREEN);
    }

    public static SummonCard BAT_GRAY() {
        return summonCard(12, "Летучая мышь", 1, 10, 3, 0,
                UnitType.FLYING, "/images/monsters/bat.jpg", CardRarity.GRAY);
    }

    public static SummonCard BAT_GREEN() {
        return summonCard(13, "Летучая мышь", 1, 10, 3, 0,
                UnitType.FLYING, "/images/monsters/bat.jpg", CardRarity.GREEN);
    }

    public static SummonCard WILD_WOLF_GRAY() {
        return summonCard(14, "Дикий волк", 1, 15, 1, 1,
                UnitType.INFANTRY, "/images/monsters/wolf.jpg", CardRarity.GRAY);
    }

    public static SummonCard WILD_WOLF_GREEN() {
        return summonCard(15, "Дикий волк", 1, 15, 1, 1,
                UnitType.INFANTRY, "/images/monsters/wolf.jpg", CardRarity.GREEN);
    }
    // Уровень 2

    public static SummonCard GOBLIN_WARRIOR_GREEN() {
        return summonCard(16, "Гоблин-воин", 2, 20, 4, 1,
                UnitType.INFANTRY, "/images/monsters/goblin_warrior.jpg", CardRarity.GREEN);
    }

    public static SummonCard GOBLIN_WARRIOR_BLUE() {
        return summonCard(17, "Гоблин-воин", 2, 20, 4, 1,
                UnitType.INFANTRY, "/images/monsters/goblin_warrior.jpg", CardRarity.BLUE);
    }

    public static SummonCard SKELETON_ARCHER_GREEN() {
        return summonCard(18, "Скелет-лучник", 2, 18, 5, 2,
                UnitType.ARCHER, "/images/monsters/skeleton_archer.jpg", CardRarity.GREEN);
    }

    public static SummonCard SKELETON_ARCHER_BLUE() {
        return summonCard(19, "Скелет-лучник", 2, 18, 5, 2,
                UnitType.ARCHER, "/images/monsters/skeleton_archer.jpg", CardRarity.BLUE);
    }

    public static SummonCard IMP_GREEN() {
        return summonCard(20, "Имп", 2, 22, 4, 1,
                UnitType.FLYING, "/images/monsters/imp.jpg", CardRarity.GREEN);
    }

    public static SummonCard IMP_BLUE() {
        return summonCard(21, "Имп", 2, 22, 4, 1,
                UnitType.FLYING, "/images/monsters/imp.jpg", CardRarity.BLUE);
    }

    // Уровень 3

    public static SummonCard ORC_BLUE() {
        return summonCard(22, "Орк", 3, 35, 7, 2,
                UnitType.INFANTRY, "/images/monsters/orc.jpg", CardRarity.BLUE);
    }

    public static SummonCard ORC_VIOLETTE() {
        return summonCard(23, "Орк", 3, 35, 7, 2,
                UnitType.INFANTRY, "/images/monsters/orc.jpg", CardRarity.VIOLETTE);
    }

    public static SummonCard ZOMBIE_HEAD_BLUE() {
        return summonCard(24, "Голова зомби", 3, 40, 5, 3,
                UnitType.FLYING, "/images/monsters/zombie_head.jpg", CardRarity.BLUE);
    }

    public static SummonCard ZOMBIE_HEAD_VIOLETTE() {
        return summonCard(25, "Голова зомби", 3, 40, 5, 3,
                UnitType.FLYING, "/images/monsters/zombie_head.jpg", CardRarity.VIOLETTE);
    }

    public static SummonCard WEREWOLF_BLUE() {
        return summonCard(26, "Оборотень", 3, 38, 8, 2,
                UnitType.ARCHER, "/images/monsters/werewolf.jpg", CardRarity.BLUE);
    }

    public static SummonCard WEREWOLF_VIOLETTE() {
        return summonCard(27, "Оборотень", 3, 38, 8, 2,
                UnitType.ARCHER, "/images/monsters/werewolf.jpg", CardRarity.VIOLETTE);
    }

    // Уровень 4

    public static SummonCard VAMPIRE_VIOLETTE() {
        return summonCard(28, "Вампир", 4, 55, 9, 3,
                UnitType.FLYING, "/images/monsters/vampire.jpg", CardRarity.VIOLETTE);
    }

    public static SummonCard VAMPIRE_RED() {
        return summonCard(29, "Вампир", 4, 55, 9, 3,
                UnitType.FLYING, "/images/monsters/vampire.jpg", CardRarity.RED);
    }

    public static SummonCard CYCLOPS_VIOLETTE() {
        return summonCard(30, "Циклоп", 4, 60, 8, 5,
                UnitType.ARCHER, "/images/monsters/cyclops.jpg", CardRarity.VIOLETTE);
    }

    public static SummonCard CYCLOPS_RED() {
        return summonCard(31, "Циклоп", 4, 60, 8, 5,
                UnitType.ARCHER, "/images/monsters/cyclops.jpg", CardRarity.RED);
    }

    public static SummonCard DEATH_KNIGHT_VIOLETTE() {
        return summonCard(32, "Рыцарь смерти", 4, 65, 7, 6,
                UnitType.INFANTRY, "/images/monsters/death_knight.jpg", CardRarity.VIOLETTE);
    }

    public static SummonCard DEATH_KNIGHT_RED() {
        return summonCard(33, "Рыцарь смерти", 4, 65, 7, 6,
                UnitType.INFANTRY, "/images/monsters/death_knight.jpg", CardRarity.RED);
    }

    // Уровень 5

    public static SummonCard DRAGONLING_RED() {
        return summonCard(34, "Дракончик", 5, 80, 12, 5,
                UnitType.FLYING, "/images/monsters/dragonling.jpg", CardRarity.RED);
    }

    public static SummonCard DRAGONLING_GOLD() {
        return summonCard(35, "Дракончик", 5, 80, 12, 5,
                UnitType.FLYING, "/images/monsters/dragonling.jpg", CardRarity.GOLD);
    }

    public static SummonCard DEMON_LORD_RED() {
        return summonCard(36, "Владыка демонов", 5, 75, 13, 4,
                UnitType.ARCHER, "/images/monsters/demon_lord.jpg", CardRarity.RED);
    }

    public static SummonCard DEMON_LORD_GOLD() {
        return summonCard(37, "Владыка демонов", 5, 75, 13, 4,
                UnitType.ARCHER, "/images/monsters/demon_lord.jpg", CardRarity.GOLD);
    }

    public static SummonCard MINOTAUR_KING_RED() {
        return summonCard(38, "Король минотавров", 5, 85, 10, 7,
                UnitType.INFANTRY, "/images/monsters/minotaur_king.jpg", CardRarity.RED);
    }

    public static SummonCard MINOTAUR_KING_GOLD() {
        return summonCard(39, "Король минотавров", 5, 85, 10, 7,
                UnitType.INFANTRY, "/images/monsters/minotaur_king.jpg", CardRarity.GOLD);
    }



    private static SummonCard summonCard(int id, String name, int level, int hp, int damage, int armor,
                                         UnitType type, String imagePath, CardRarity rarity) {
        MonsterTemplate template = new MonsterTemplate(id, name, level, hp, damage, armor, type, imagePath);
        return new SummonCard(template);
    }



    // ── Основные публичные методы ───────────────────────────────────────

    public static Optional<SummonEntry> getById(int id) {
        return Optional.ofNullable(SUMMONS.get(id));
    }

    public static SummonEntry getByIdOrThrow(int id) {
        return getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный summon id: " + id));
    }



    // ------------------------------------------------------------------------


    public static int getSummonLevel(SummonCard card) {
        // Предполагаем, что у тебя есть доступ к template внутри фабрики
        // Но если template приватный — этот вариант не сработает
        return card.getMonsterTemplate().level();   // ← всё равно нужен геттер
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

    public static final List<SummonCard> ALL_SUMMON_CARDS = List.of(
            ancestor_spirit(),
            // Уровень 1
            SPIDER_THROWER_GRAY(),
            SPIDER_THROWER_GREEN(),
            BAT_GRAY(),
            BAT_GREEN(),
            WILD_WOLF_GRAY(),
            WILD_WOLF_GREEN(),

            // Уровень 2
            GOBLIN_WARRIOR_GREEN(),
            GOBLIN_WARRIOR_BLUE(),
            SKELETON_ARCHER_GREEN(),
            SKELETON_ARCHER_BLUE(),
            IMP_GREEN(),
            IMP_BLUE(),

            // Уровень 3
            ORC_BLUE(),
            ORC_VIOLETTE(),
            ZOMBIE_HEAD_BLUE(),
            ZOMBIE_HEAD_VIOLETTE(),
            WEREWOLF_BLUE(),
            WEREWOLF_VIOLETTE(),

            // Уровень 4
            VAMPIRE_VIOLETTE(),
            VAMPIRE_RED(),
            CYCLOPS_VIOLETTE(),
            CYCLOPS_RED(),
            DEATH_KNIGHT_VIOLETTE(),
            DEATH_KNIGHT_RED(),

            // Уровень 5
            DRAGONLING_RED(),
            DRAGONLING_GOLD(),
            DEMON_LORD_RED(),
            DEMON_LORD_GOLD(),
            MINOTAUR_KING_RED(),
            MINOTAUR_KING_GOLD()
    );


}