package MazeGame.cards;

import MazeGame.MonsterFactory;
import MazeGame.UnitType;

import java.util.*;


public class SummonDeck {


    private final Map<UnitType, SummonCard> activeSummons = new EnumMap<>(UnitType.class);
    private UnitType selectedType;

    public SummonDeck() {
        SummonCard starter = MonsterFactory.createStarterSummonCard();
        activeSummons.put(starter.getUnitType(), starter);
        this.selectedType = starter.getUnitType(); // по умолчанию берём стартовый
    }

    public boolean tryAddOrUpgrade(SummonCard newCard) {
        UnitType type = newCard.getUnitType();
        SummonCard current = activeSummons.get(type);

        if (current == null) {
            activeSummons.put(type, newCard);
            // если это первый суммон — можно сразу выбрать его
            if (selectedType == null) {
                selectedType = type;
            }
            return true;
        }

        if (newCard.getRarity().ordinal() > current.getRarity().ordinal()) {
            activeSummons.put(type, newCard);
            return true;
        }

        return false;
    }

    public SummonCard getSelectedSummon() {
        if (selectedType == null) return null;
        return activeSummons.get(selectedType);
    }

    public void selectType(UnitType type) {
        if (activeSummons.containsKey(type)) {
            this.selectedType = type;
        }
    }

    public SummonCard getByType(UnitType type) {
        return activeSummons.get(type);
    }

    public Map<UnitType, SummonCard> getAll() {
        return Collections.unmodifiableMap(activeSummons);
    }

    public int size() {
        return activeSummons.size();
    }

}
