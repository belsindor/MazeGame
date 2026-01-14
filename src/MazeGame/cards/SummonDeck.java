package MazeGame.cards;

import MazeGame.UnitType;
import java.util.*;

public class SummonDeck {

    private final Map<UnitType, SummonCard> active =
            new EnumMap<>(UnitType.class);

    private SummonCard selected; // 👈 ВЫБРАННЫЙ СУММОН

    public boolean tryAddOrReplace(SummonCard card) {
        SummonCard current = active.get(card.getUnitType());

        if (current == null) {
            active.put(card.getUnitType(), card);
            return true;
        }

        if (card.getRarity().isHigherThan(current.getRarity())) {
            active.put(card.getUnitType(), card);

            // если улучшили выбранного — обновим ссылку
            if (current == selected) {
                selected = card;
            }
            return true;
        }

        return false;
    }

    public Collection<SummonCard> getAll() {
        return active.values();
    }

    public void addInitialSummon(SummonCard card) {
        active.put(card.getUnitType(), card);
        if (selected == null) {
            selected = card;
        }
    }

    // ===== НОВОЕ =====

    public void select(SummonCard card) {
        if (active.containsValue(card)) {
            selected = card;
        }
    }

    public SummonCard getSelectedSummon() {
        return selected;
    }
}
