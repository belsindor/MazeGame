package MazeGame.cards;

import MazeGame.MonsterFactory;
import MazeGame.UnitType;
import MazeGame.MonsterTemplate;

import java.util.*;


public class SummonDeck {

    private final Map<UnitType, SummonFactory> active = new EnumMap<>(UnitType.class);

    public boolean tryAddOrReplace(SummonFactory card) {

        SummonFactory current = active.get(card.getUnitType());

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

    public Collection<SummonFactory> getAll() {
        return active.values();
    }

    public void addInitialSummon(SummonFactory card) {
        active.put(card.getUnitType(), card);
    }
}
