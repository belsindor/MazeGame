package MazeGame.cards;

import MazeGame.Monster;
import MazeGame.MonsterTemplate;
import MazeGame.UnitType;

public abstract class UnitCard extends Card {

    protected final MonsterTemplate template;

    protected UnitCard(MonsterTemplate template) {
        super(CardType.UNIT, rarity, imagePath);

        this.template = template;
    }

    public abstract Monster summon();

    public String getImagePath() {
        return template.imagePath();
    }

    public String getUnitName() {
        return template.name();
    }

    public UnitType getUnitType() {
        return template.unitType();
    }
}
