package MazeGame.cards;

import MazeGame.MonsterFactory;
import MazeGame.UnitType;

import java.util.*;

public class CombatDeck {

    private final List<Card> cards = new ArrayList<>();

    public void add(Card card) {
        cards.add(card);
    }

    public void clear() {
        cards.clear();
    }

    public List<Card> draw(int count) {
        return cards.subList(0, Math.min(count, cards.size()));
    }

    private final Map<UnitType, SummonFactory> activeCards = new EnumMap<>(UnitType.class);

    public void SummonFactoryDeck() {
        // стартовая карта
        SummonFactory starter = MonsterFactory.createStarterSummonFactory();
        activeCards.put(starter.getUnitType(), starter);
    }

    public boolean tryAddOrUpgrade(SummonFactory newCard) {
        if (newCard == null) {
            return false;
        }

        UnitType type = newCard.getUnitType();
        SummonFactory current = activeCards.get(type);

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

    public SummonFactory getByType(UnitType type) {
        return activeCards.get(type);
    }


    public Collection<SummonFactory> getAllActive() {
        return activeCards.values();
    }


    public int size() {
        return activeCards.size();
    }

    public SummonFactory getSelectedForBattle() {
        if (activeCards.isEmpty()) {
            return null;
        }
        // Вариант 1 — случайный
        // return new ArrayList<>(activeCards.values()).get(new Random().nextInt(size()));

        // Вариант 2 — всегда первый по порядку enum (стабильнее для тестов)
        return activeCards.values().iterator().next();
    }

    public Map<UnitType, SummonFactory> debugGetMap() {
        return new EnumMap<>(activeCards);
    }


}
