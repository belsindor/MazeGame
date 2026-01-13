package MazeGame.cards;

import MazeGame.MonsterFactory;
import MazeGame.UnitType;

import java.util.Collection;
import java.util.EnumMap;

import java.util.Map;

public class CombatDeck {

    private final Map<UnitType, SummonCard> activeCards = new EnumMap<>(UnitType.class);

    public void SummonDeck() {
        // стартовая карта
        SummonCard starter = MonsterFactory.createStarterSummonCard();
        activeCards.put(starter.getUnitType(), starter);
    }

    public boolean tryAddOrUpgrade(SummonCard newCard) {
        if (newCard == null) {
            return false;
        }

        UnitType type = newCard.getUnitType();
        SummonCard current = activeCards.get(type);

        // Новый тип — просто добавляем
        if (current == null) {
            activeCards.put(type, newCard);
            return true;
        }

        // Сравниваем редкость
        if (newCard.getRarity().ordinal() > current.getRarity().ordinal()) {
            activeCards.put(type, newCard);
            // можно логировать/уведомлять игрока: "Улучшен [имя] до [новая редкость]"
            return true;
        }

        // Такая же или хуже редкость — игнорируем
        // (или можно положить в "коллекцию трофеев", если она у тебя планируется)
        return false;
    }

    public SummonCard getByType(UnitType type) {
        return activeCards.get(type);
    }


    public Collection<SummonCard> getAllActive() {
        return activeCards.values();
    }


    public int size() {
        return activeCards.size();
    }

    public SummonCard getSelectedForBattle() {
        if (activeCards.isEmpty()) {
            return null;
        }
        // Вариант 1 — случайный
        // return new ArrayList<>(activeCards.values()).get(new Random().nextInt(size()));

        // Вариант 2 — всегда первый по порядку enum (стабильнее для тестов)
        return activeCards.values().iterator().next();
    }

    public Map<UnitType, SummonCard> debugGetMap() {
        return new EnumMap<>(activeCards);
    }


}
