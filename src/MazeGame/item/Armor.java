package MazeGame.item;

import MazeGame.cards.CardRarity;
//+
public class Armor extends Item {
    private final String armorType;
    private static final long serialVersionUID = 1L;

    public Armor(String name, String armorType, int protection, double dropChance, int strength, CardRarity rarity) {
        super(name, strength, protection, dropChance, rarity);
        this.armorType = armorType;
    }
    @Override
    public String getIconPath() {
        return switch (name) {
            // низ брони
            case "Шорты с заплатками рудокопа" -> "/images/items/pants0.png";
            case "Штаны рудокопа" -> "/images/items/pants1.png";
            // верх брони
            case "Дрянная рубаха рудокопа" -> "/images/items/shirt0.png";
            case "Меховая жилетка рудокопа" -> "/images/items/shirt1.png";
            // шиты
            case "Кожаный щит" -> "/images/items/shield_skin.png";
            case "Деревянный щит" -> "/images/items/shield_wood.png";
            case "Железный щит" -> "/images/items/shield_iron.png";
            default -> "/images/items/default.png";
        };
    }


    @Override
    public String getType() { return armorType; }

    @Override
    public int getProtection() { return getValue(); }

    @Override
    public int getAttack() { return 0; }

    public String getArmorType() { return armorType; }
}