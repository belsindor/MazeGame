package MazeGame.cards;

import MazeGame.Monster;

public class SummonFactory {

    public static SummonCard fromMonster(Monster monster) {

        CardRarity rarity = CardRarity.fromLevel(monster.getLevel());

        return new SummonCard(
                monster.getName(),
                monster.getUnitType(),
                rarity,
                monster.getAttack() / 2,
                monster.getHealth() / 2,
                getSummonImage(monster, rarity)
        );
    }

    private static String getSummonImage(Monster monster, CardRarity rarity) {
        return "summons/" + monster.getId() + "_" + rarity.name().toLowerCase() + ".jpg";
    }
}
