package MazeGame.cards;

//+
import MazeGame.MonsterTemplate;
import MazeGame.UnitType;
import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleResult;

//++
public class SummonCard extends Card {   // предполагается наследование от Card

    private final MonsterTemplate template;


    public SummonCard(MonsterTemplate template, CardRarity rarity) {
        super(template.id(), CardType.SUMMON, rarity, TypeEffect.NONE, template.imagePath());
        this.template = template;
    }


    public MonsterTemplate getMonsterTemplate() {
        return template;
    }

    public int getLevel() {
        return template.level();
    }

    public String getName() {
        return template.name();
    }

    public UnitType getUnitType() {
        return template.unitType();
    }

    public int getMaxHealth() {
        return template.maxHealth();
    }

    public int getAttack() {
        return template.attack();
    }

    public int getDefense() {
        return template.defense();
    }

    public String getImagePath() {
        return template.imagePath();
    }

    @Override
    public void play(BattleContext context, BattleResult result) {
        //надо дописать
    }

}


