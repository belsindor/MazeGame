package MazeGame.item;

public class Armor extends Item {
    private String armorType;
    private static final long serialVersionUID = 1L;

    public Armor(String name, String armorType, int protection, double dropChance, int strength) {
        super(name, strength, protection, dropChance);
        this.armorType = armorType;
    }
    @Override
    public String getIconPath() {
        return switch (name) {
            case "Шорты с заплатками рудокопа" -> "images/items/pants0.png";
            case "Штаны рудокопа" -> "images/items/pants1.png";
            case "Дранянная рубаха рудокопа" -> "images/items/shirt0.png";
            case "Меховая жилетка рудокопа" -> "images/items/shirt1.png";
            default -> "images/items/default.png";
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