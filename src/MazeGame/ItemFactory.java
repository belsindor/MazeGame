package MazeGame;

public class ItemFactory {
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

    // Генерация предмета в зависимости от уровня монстра
    public static Item generateLoot(int monsterLevel) {
        double random = Math.random();

        switch (monsterLevel) {
            case 1:
                if (random < 0.2) return createStick();
                break;
            case 2:
                if (random < 0.15) return createOldDagger();
                else if (random < 0.25) return createPatchedShorts();
                else if (random < 0.35) return createWornShirt();
                break;
            case 3:
                if (random < 0.1) return createRustySword();
                else if (random < 0.18) return createMinerPants();
                else if (random < 0.26) return createFurVest();
                break;
            case 4:
            case 5:
                if (random < 0.15) return createRustySword();
                else if (random < 0.25) return createMinerPants();
                else if (random < 0.35) return createFurVest();
                break;
        }

        return null;
    }
}