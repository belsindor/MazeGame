package MazeGame.item;

import MazeGame.cards.CardRarity;

import java.util.List;
import java.util.Random;
//+
public final class ItemFactory {

    private static final Random RANDOM = new Random();

    private ItemFactory() {
    }

    // ──────────────────────────────────────────────────────────────
    // Оружие (Weapon)
    // ──────────────────────────────────────────────────────────────

    public static Weapon stick() {
        return new Weapon("Палка", 1, 0.20, 5, CardRarity.GRAY);
    }

    public static Weapon oldDagger() {
        return new Weapon("Старый кинжал", 2, 0.15, 8, CardRarity.GREEN);
    }

    public static Weapon rustySword() {
        return new Weapon("Ржавый меч", 3, 0.10, 10, CardRarity.BLUE);
    }

    // ──────────────────────────────────────────────────────────────
    // Броня (Armor) — верх/низ/щит
    // ──────────────────────────────────────────────────────────────

    // Низ
    public static Armor patchedShorts() {
        return new Armor("Шорты с заплатками рудокопа","низ",1,0.10,10, CardRarity.GRAY);
    }

    public static Armor minerPants() {
        return new Armor("Штаны рудокопа","низ",2,0.08,15, CardRarity.GREEN);
    }

    // Верх
    public static Armor wornShirt() {
        return new Armor("Дрянная рубаха рудокопа", "верх", 1, 0.10, 10, CardRarity.GRAY);
    }

    public static Armor furVest() {
        return new Armor("Меховая жилетка рудокопа", "верх", 2, 0.08, 15, CardRarity.GREEN);
    }

    // Щит
    public static Armor leatherShield() {
        return new Armor("Кожаный щит", "shield", 1, 0.20, 10, CardRarity.GRAY);
    }

    public static Armor woodenShield() {
        return new Armor("Деревянный щит", "shield", 2, 0.15, 15, CardRarity.GREEN);
    }

    public static Armor ironShield() {
        return new Armor("Железный щит", "shield", 3, 0.10, 20, CardRarity.BLUE);
    }

    // ──────────────────────────────────────────────────────────────
    // Все возможные предметы в виде списка
    // ──────────────────────────────────────────────────────────────

    public static final List<Item> ALL_ITEMS = List.of(
            stick(),
            oldDagger(),
            rustySword(),
            patchedShorts(),
            minerPants(),
            wornShirt(),
            furVest(),
            leatherShield(),
            woodenShield(),
            ironShield()
    );



    private record WeightedItem(Item item, int weight) {
    }

    private static WeightedItem weighted(Item item, int weight) {
        return new WeightedItem(item, weight);
    }

    private static Item weightedChoice(List<WeightedItem> options) {
        if (options.isEmpty()) {
            return null;
        }

        int totalWeight = options.stream().mapToInt(WeightedItem::weight).sum();
        int roll = RANDOM.nextInt(totalWeight);

        int current = 0;
        for (WeightedItem option : options) {
            current += option.weight;
            if (roll < current) {
                return option.item;
            }
        }

        // теоретически недостижимо, но на всякий случай
        return options.getLast().item;
    }

    /**
     * Получить все предметы определённого типа
     */
    public static List<Item> getAllWeapons() {
        return ALL_ITEMS.stream()
                .filter(item -> item instanceof Weapon)
                .toList();
    }

    public static List<Item> getAllArmor() {
        return ALL_ITEMS.stream()
                .filter(item -> item instanceof Armor)
                .toList();
    }
}