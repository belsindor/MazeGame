package MazeGame.cards;

import MazeGame.MonsterFactory;
import MazeGame.UnitType;

import java.util.*;


public class SummonDeck {

    private final Map<UnitType, SummonCard> active = new EnumMap<>(UnitType.class);

    public boolean tryAddOrReplace(SummonCard card) {

        SummonCard current = active.get(card.getUnitType());

        if (current == null) {
            active.put(card.getUnitType(), card);
            return true;
        }

        if (card.getRarity().isHigherThan(current.getRarity())) {
            active.put(card.getUnitType(), card);
            return true;
        }

        return false;
    }

    public Collection<SummonCard> getAll() {
        return active.values();
    }

    public void addInitialSummon(SummonCard card) {
        active.put(card.getUnitType(), card);
    }
}
