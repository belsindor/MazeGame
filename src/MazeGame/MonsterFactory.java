package MazeGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonsterFactory {

    private static final Random random = new Random();

    /* ================= СПИСОК ВСЕХ МОНСТРОВ ================= */

    private static final List<Monster> ALL_MONSTERS = new ArrayList<>();

    static {
        // Уровень 1
        ALL_MONSTERS.add(new Monster("Крыса-мутант", 1, 12, 2, 0));
        ALL_MONSTERS.add(new Monster("Ядовитый паук", 1, 10, 3, 0));
        ALL_MONSTERS.add(new Monster("Старушка", 1, 8, 4, 0));
        ALL_MONSTERS.add(new Monster("Грибник", 1, 15, 1, 1));

        // Уровень 2
        ALL_MONSTERS.add(new Monster("Гоблин-воин", 2, 20, 4, 1));
        ALL_MONSTERS.add(new Monster("Скелет-воин", 2, 18, 5, 2));
        ALL_MONSTERS.add(new Monster("Дикий волк", 2, 22, 4, 1));
        ALL_MONSTERS.add(new Monster("Бандит", 2, 25, 3, 2));

        // Уровень 3
        ALL_MONSTERS.add(new Monster("Орк-берсерк", 3, 35, 7, 2));
        ALL_MONSTERS.add(new Monster("Зомби-охранник", 3, 40, 5, 3));
        ALL_MONSTERS.add(new Monster("Тролль", 3, 45, 6, 3));
        ALL_MONSTERS.add(new Monster("Оборотень", 3, 38, 8, 2));

        // Уровень 4
        ALL_MONSTERS.add(new Monster("Младший вампир", 4, 55, 9, 3));
        ALL_MONSTERS.add(new Monster("Огненный элементаль", 4, 50, 10, 4));
        ALL_MONSTERS.add(new Monster("Химера", 4, 60, 8, 5));
        ALL_MONSTERS.add(new Monster("Рыцарь-скелет", 4, 65, 7, 6));

        // Уровень 5
        ALL_MONSTERS.add(new Monster("Дракончик", 5, 80, 12, 5));
        ALL_MONSTERS.add(new Monster("Младший демон", 5, 75, 13, 4));
        ALL_MONSTERS.add(new Monster("Лич-ученик", 5, 70, 11, 6));
        ALL_MONSTERS.add(new Monster("Минотавр", 5, 85, 10, 7));
    }

    /* ================= СОЗДАНИЕ МОНСТРА ================= */

    public static Monster createMonsterForPlayer(int playerLevel) {

        List<Monster> allowed = new ArrayList<>();

        for (Monster monster : ALL_MONSTERS) {
            if (monster.getLevel() <= playerLevel) {
                allowed.add(monster);
            }
        }

        if (allowed.isEmpty()) {
            // защита от ошибок — хотя по логике такого быть не должно
            return new Monster("Крыса-мутант", 1, 12, 2, 0);
        }

        Monster template = allowed.get(random.nextInt(allowed.size()));

        // создаём НОВЫЙ объект, чтобы не портить шаблон
        return new Monster(
                template.getName(),
                template.getLevel(),
                template.getMaxHealth(),
                template.getAttack(),
                template.getDefense()
        );
    }
}
