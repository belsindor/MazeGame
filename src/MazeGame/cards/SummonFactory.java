package MazeGame.cards;

import MazeGame.Monster;

public class SummonFactory {

    public static SummonCard fromMonster(Monster monster) {

        int summonId = monster.getId() / 10;
        CardRarity rarity = CardRarity.fromLevel(monster.getLevel());

        return new SummonCard(
                summonId,
                monster.getName(),
                monster.getUnitType(),
                rarity,
                monster.getAttack(),
                monster.getHealth(),
                getSummonImage(monster, rarity)
        );
    }

    private static String getSummonImage(Monster monster, CardRarity rarity) {
        return "summons/" + monster.getId() + "_" + rarity.name().toLowerCase() + ".jpg";
    }
}
