package MazeGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class MonsterFactory {

    private static final Random random = new Random();

    /* ================= СПИСОК ВСЕХ МОНСТРОВ ================= */

    private static final List<MonsterTemplate> ALL_MONSTERS = new ArrayList<>();

    static {
        // Уровень 1
        ALL_MONSTERS.add(new MonsterTemplate("Паук-метатель", 1, 12, 2, 0, UnitType.ARCHER));
        ALL_MONSTERS.add(new MonsterTemplate("Летучая мышь", 1, 10, 3, 0,UnitType.FLYING));
        ALL_MONSTERS.add(new MonsterTemplate("Дикий волк", 1, 15, 1, 1, UnitType.INFANTRY));

        // Уровень 2
        ALL_MONSTERS.add(new MonsterTemplate("Гоблин-воин", 2, 20, 4, 1, UnitType.INFANTRY));
        ALL_MONSTERS.add(new MonsterTemplate("Скелет-лучник", 2, 18, 5, 2, UnitType.ARCHER));
        ALL_MONSTERS.add(new MonsterTemplate("Имп", 2, 22, 4, 1, UnitType.FLYING));


        // Уровень 3
        ALL_MONSTERS.add(new MonsterTemplate("Орк", 3, 35, 7, 2, UnitType.INFANTRY));
        ALL_MONSTERS.add(new MonsterTemplate("Голова зомби", 3, 40, 5, 3, UnitType.FLYING));
        ALL_MONSTERS.add(new MonsterTemplate("Оборотень", 3, 38, 8, 2, UnitType.ARCHER));

        // Уровень 4
        ALL_MONSTERS.add(new MonsterTemplate("Вампир", 4, 55, 9, 3, UnitType.FLYING));
        ALL_MONSTERS.add(new MonsterTemplate("Циклоп", 4, 60, 8, 5, UnitType.ARCHER));
        ALL_MONSTERS.add(new MonsterTemplate("Рыцарь смерти", 4, 65, 7, 6, UnitType.INFANTRY));

        // Уровень 5
        ALL_MONSTERS.add(new MonsterTemplate("Дракончик", 5, 80, 12, 5, UnitType.FLYING));
        ALL_MONSTERS.add(new MonsterTemplate("Владыка демонов", 5, 75, 13, 4, UnitType.ARCHER));
        ALL_MONSTERS.add(new MonsterTemplate("Король минотавров", 5, 85, 10, 7, UnitType.INFANTRY));
    }
    //возможно надо добавить в конструктор имя файла jpg для карты
    /* ================= СОЗДАНИЕ МОНСТРА ================= */

    public static Monster createMonsterForPlayer(int playerLevel) {

        List<MonsterTemplate> allowed = new ArrayList<>();

        for (MonsterTemplate monster : ALL_MONSTERS) {
            if (monster.level() >= playerLevel - 1 && monster.level() <= playerLevel) {
                allowed.add(monster);
            }
        }

        if (allowed.isEmpty()) {
            // защита от ошибок — хотя по логике такого быть не должно
            return new Monster("Орк", 3, 35, 7, 2, UnitType.INFANTRY);
        }

        MonsterTemplate t = allowed.get(random.nextInt(allowed.size()));
        return new Monster(t);

        // создаём НОВЫЙ объект, чтобы не портить шаблон


    }
}
