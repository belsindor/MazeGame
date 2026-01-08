package MazeGame.cards;

import MazeGame.Monster;
import MazeGame.UnitType;

public class MonsterCard extends Card {

    private final Monster template;
    private final String imagePath;

    public MonsterCard(Monster template) {
        super(CardRarity.fromLevel(template.getLevel()));
        this.template = template;
        this.imagePath = "/images/monsters/" + template.getName().replace(" ", "_") + ".jpg";

    }

    @Override
    public CardType getType() {
        return CardType.MONSTER;
    }

    // ===== ПРИЗЫВ МОНСТРА (−50% статов) =====
    public Monster summon() {
        return template.createWeakened(0.5);
    }



    // ===== ГЕТТЕРЫ =====
    public String getImagePath() {
        return imagePath;
    }

    public String getMonsterName() {
        return template.getName();
    }

    public UnitType getUnitType() {
        return template.getUnitType();
    }

}
