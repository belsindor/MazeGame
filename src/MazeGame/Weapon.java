package MazeGame;

public class Weapon extends Item {
    private static final long serialVersionUID = 1L;
    public Weapon(String name, int attack, double dropChance, int strength) {
        super(name, strength, attack, dropChance);
    }
    @Override
    public String getIconPath() {
        return switch (name) {
            case "Палка" -> "images/items/stick.png";
            case "Старый кинжал" -> "images/items/dagger.png";
            case "Ржавый меч" -> "images/items/sword.png";
            default -> "images/items/default.png";
        };
    }


    @Override
    public String getType() { return "weapon"; }

    @Override
    public int getProtection() { return 0; }

    @Override
    public int getAttack() { return getValue(); }
}