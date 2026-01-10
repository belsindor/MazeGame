package MazeGame.item;

import java.util.Random;

public class ItemFactory {

    private static final Random random = new Random();

    // Создание оружия
     public static Weapon createStick() {
        return new Weapon("Палка", 1, 0.2, 5);
    }

    public static Weapon createOldDagger() {
        return new Weapon("Старый кинжал", 2, 0.15, 8);
    }

    public static Weapon createRustySword() {
        return new Weapon("Ржавый меч", 3, 0.1, 10);
    }

    // Создание брони
    public static Armor createPatchedShorts() {
        return new Armor("Шорты с заплатками рудокопа", "низ", 1, 0.1, 10);
    }

    public static Armor createMinerPants() {
        return new Armor("Штаны рудокопа", "низ", 2, 0.08, 15);
    }

    public static Armor createWornShirt() {
        return new Armor("Дранянная рубаха рудокопа", "верх", 1, 0.1, 10);
    }

    public static Armor createFurVest() {
        return new Armor("Меховая жилетка рудокопа", "верх", 2, 0.08, 15);
    }

    // Создание шитов
    public static Armor createShieldSkin() { return new Armor("Кожаный щит", "shield", 1, 0.2, 10); }

    public static Armor createShieldWood() {
        return new Armor("Деревянный щит", "shield", 2, 0.15, 15);
    }

    public static Armor createShieldIron() {
        return new Armor("Дранянная рубаха рудокопа", "shield", 3, 0.1, 10);
    }





    // Генерация предмета в зависимости от уровня монстра
    public static Item generateLoot(int monsterLevel) {
        double roll = random.nextDouble();

        switch (monsterLevel) {

            case 1 -> {
                if (roll < 0.20) return createStick();
            }

            case 2 -> {
                if (roll < 0.15) return createOldDagger();
                if (roll < 0.25) return createPatchedShorts();
                if (roll < 0.35) return createWornShirt();
                if (roll < 0.45) return createShieldSkin();
            }

            case 3 -> {
                if (roll < 0.10) return createRustySword();
                if (roll < 0.20) return createMinerPants();
                if (roll < 0.30) return createFurVest();
                if (roll < 0.40) return createShieldWood();
            }

            case 4 -> {
                if (roll < 0.50) return createShieldIron();
            }

            case 5 -> {
                if (roll < 0.15) return createRustySword();
                if (roll < 0.25) return createMinerPants();
                if (roll < 0.35) return createFurVest();
            }
        }

        return null; // ничего не выпало
    }
}