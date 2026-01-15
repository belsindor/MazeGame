package MazeGame.cards;

import MazeGame.UnitType;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public class SummonDeck {

    private final Map<UnitType, SummonCard> active = new EnumMap<>(UnitType.class);
    private SummonCard selected;

    public boolean tryAddOrUpgrade(SummonCard card) {
        SummonCard current = active.get(card.getUnitType());

        if (current == null) {
            active.put(card.getUnitType(), card);
            return true;
        }

        if (current.getRarity() == CardRarity.GOLD) {
            return false;
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

    public SummonCard getByType(UnitType type) {
        return active.get(type);
    }

    public void select(SummonCard card) {
        this.selected = card;
    }

    public SummonCard getSelectedSummon() {
        return selected;
    }

    public boolean tryAddOrReplace(SummonCard card) {
        return tryAddOrUpgrade(card);
    }

    public Collection<SummonCard> getCards() {
        return active.values();
    }


}
