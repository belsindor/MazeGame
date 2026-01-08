package MazeGame.cards;


import MazeGame.Monster;
import MazeGame.UnitType;

public class SummonCard extends Card {

    private final Monster template;
    private final String imagePath;

    public SummonCard(Monster template) {
        super(CardRarity.fromLevel(template.getLevel()));
        this.template = template;
        this.imagePath =
                "/images/monsters/" +
                        template.getName().replace(" ", "_") +
                        ".jpg";
    }

    @Override
    public CardType getType() {
        return CardType.MONSTER; // или SUMMON (лучше отдельный тип)
    }

    public Monster summon() {
        return template.createWeakened(0.5);
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getName() {
        return template.getName();
    }

    public UnitType getUnitType() {
        return template.getUnitType();
    }
}
