package MazeGame;

import MazeGame.cards.CardRarity;
import MazeGame.cards.CardType;
import MazeGame.cards.MonsterCard;
import MazeGame.cards.SummonCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class MonsterFactory {

    private static final Random RANDOM = new Random();

    /* ================= ВСЕ ШАБЛОНЫ МОНСТРОВ ================= */

    private static final List<MonsterTemplate> ALL_MONSTERS = new ArrayList<>();



    static {
        // ===== УРОВЕНЬ 1 =====
        ALL_MONSTERS.add(new MonsterTemplate(
                "Паук-метатель",
                1, 12, 2, 0,
                UnitType.ARCHER,
                "/images/monsters/spider.jpg"
        ));
        ALL_MONSTERS.add(new MonsterTemplate(
                "Летучая мышь",
                1, 10, 3, 0,
                UnitType.FLYING,
                "/images/monsters/bat.jpg"
        ));
        ALL_MONSTERS.add(new MonsterTemplate(
                "Дикий волк",
                1, 15, 1, 1,
                UnitType.INFANTRY,
                "/images/monsters/wolf.jpg"
        ));

        // ===== УРОВЕНЬ 2 =====
        ALL_MONSTERS.add(new MonsterTemplate(
                "Гоблин-воин",
                2, 20, 4, 1,
                UnitType.INFANTRY,
                "/images/monsters/goblin_warrior.jpg"
        ));
        ALL_MONSTERS.add(new MonsterTemplate(
                "Скелет-лучник",
                2, 18, 5, 2,
                UnitType.ARCHER,
                "/images/monsters/skeleton_archer.jpg"
        ));
        ALL_MONSTERS.add(new MonsterTemplate(
                "Имп",
                2, 22, 4, 1,
                UnitType.FLYING,
                "/images/monsters/imp.jpg"
        ));

        // ===== УРОВЕНЬ 3 =====
        ALL_MONSTERS.add(new MonsterTemplate(
                "Орк",
                3, 35, 7, 2,
                UnitType.INFANTRY,
                "/images/monsters/orc.jpg"
        ));
        ALL_MONSTERS.add(new MonsterTemplate(
                "Голова зомби",
                3, 40, 5, 3,
                UnitType.FLYING,
                "/images/monsters/zombie_head.jpg"
        ));
        ALL_MONSTERS.add(new MonsterTemplate(
                "Оборотень",
                3, 38, 8, 2,
                UnitType.ARCHER,
                "/images/monsters/werewolf.jpg"
        ));

        // ===== УРОВЕНЬ 4 =====
        ALL_MONSTERS.add(new MonsterTemplate(
                "Вампир",
                4, 55, 9, 3,
                UnitType.FLYING,
                "/images/monsters/vampire.jpg"
        ));
        ALL_MONSTERS.add(new MonsterTemplate(
                "Циклоп",
                4, 60, 8, 5,
                UnitType.ARCHER,
                "/images/monsters/cyclops.jpg"
        ));
        ALL_MONSTERS.add(new MonsterTemplate(
                "Рыцарь смерти",
                4, 65, 7, 6,
                UnitType.INFANTRY,
                "/images/monsters/death_knight.jpg"
        ));

        // ===== УРОВЕНЬ 5 =====
        ALL_MONSTERS.add(new MonsterTemplate(
                "Дракончик",
                5, 80, 12, 5,
                UnitType.FLYING,
                "/images/monsters/dragonling.jpg"
        ));
        ALL_MONSTERS.add(new MonsterTemplate(
                "Владыка демонов",
                5, 75, 13, 4,
                UnitType.ARCHER,
                "/images/monsters/demon_lord.jpg"
        ));
        ALL_MONSTERS.add(new MonsterTemplate(
                "Король минотавров",
                5, 85, 10, 7,
                UnitType.INFANTRY,
                "/images/monsters/minotaur_king.jpg"
        ));
    }

    /* ================= СОЗДАНИЕ ВРАГА ================= */

    public static Monster createEnemyForPlayer(int playerLevel) {
        MonsterTemplate template = pickTemplate(playerLevel);
        return new Monster(template);
    }

    /* ================= КАРТЫ ================= */

    public static MonsterCard createMonsterCard(int playerLevel) {
        return new MonsterCard(pickTemplate(playerLevel));
    }
    public static SummonCard createSummonCardByRarity(CardRarity rarity) {
        List<MonsterTemplate> pool = ALL_MONSTERS.stream()
                .filter(t -> CardRarity.fromLevel(t.level()) == rarity)
                .toList();

        MonsterTemplate t =
                pool.get(RANDOM.nextInt(pool.size()));

        return new SummonCard(t);
    }

    public static SummonCard createSummonCard(int playerLevel) {
        return new SummonCard(pickTemplate(playerLevel));
    }

    /* ================= ВСПОМОГАТЕЛЬНОЕ ================= */

    private static MonsterTemplate pickTemplate(int playerLevel) {
        List<MonsterTemplate> allowed = new ArrayList<>();

        for (MonsterTemplate t : ALL_MONSTERS) {
            if (t.level() >= playerLevel - 1 && t.level() <= playerLevel) {
                allowed.add(t);
            }
        }

        if (allowed.isEmpty()) {
            return ALL_MONSTERS.get(0); // страховка
        }

        return allowed.get(RANDOM.nextInt(allowed.size()));
    }

    public static List<MonsterTemplate> getAllTemplates() {
        return new ArrayList<>(ALL_MONSTERS);
    }

    public static SummonCard createStarterSummonCard() {
        MonsterTemplate template = new MonsterTemplate(
                "Дух предка",
                1,
                6,
                1,
                0,
                UnitType.FLYING,
                "/cards/summon/ancestor_spirit.jpg"
        );

        return new SummonCard(template);
    }


}
