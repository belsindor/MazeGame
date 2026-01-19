package MazeGame.cards;

//+
import java.util.HashMap;
import java.util.Map;

public class CardCollection {

    private final Map<Card, Integer> regularCards = new HashMap<>();

    /**
     * Универсальный метод добавления любой карты
     */
    public void addCard(Card card) {
        if (card == null) return;

        regularCards.merge(card, 1, Integer::sum);
        checkAndConvert(card);   // улучшение 10→1 работает для всех, в т.ч. SummonCard
    }

    /**
     * Удобный метод для добавления дропа
     */
    public void addDrop(CardDropService.DropEntry entry) {
        if (entry == null) return;

        if (entry.getSummonCard() != null) {
            addCard(entry.getSummonCard());
        } else if (entry.isCard()) {
            addCard(entry.getCard());
        }
        // Item игнорируется
    }

    private void checkAndConvert(Card currentCard) {
        int count = regularCards.getOrDefault(currentCard, 0);
        if (count < 10) return;

        Card nextCard = findNextLevelCard(currentCard);

        if (nextCard != null) {
            regularCards.put(currentCard, count - 10);
            if (regularCards.get(currentCard) == 0) {
                regularCards.remove(currentCard);
            }

            addCard(nextCard);  // рекурсия, цепная конвертация возможна
        }
        // если следующей нет — остаётся 10+ штук
    }

    private Card findNextLevelCard(Card current) {
        int nextId = current.getId() + 1;
        return CardLibrary.getAllCards().stream()
                .filter(c -> c.getId() == nextId)
                .findFirst()
                .orElse(null);
    }

    // ── Геттеры ───────────────────────────────────────────────

    /**
     * Все карты игрока с количеством
     */
    public Map<Card, Integer> getAllCards() {
        return new HashMap<>(regularCards);
    }

    /**
     * Только карты-призывы с количеством
     */
    public Map<SummonCard, Integer> getSummons() {
        Map<SummonCard, Integer> result = new HashMap<>();
        for (Map.Entry<Card, Integer> e : regularCards.entrySet()) {
            if (e.getKey() instanceof SummonCard summon) {
                result.put(summon, e.getValue());
            }
        }
        return result;
    }

    public int getTotalCount() {
        return regularCards.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void clear() {
        regularCards.clear();
    }


    public boolean isEmpty() {
        return false;
    }

    public void removeCard(Card card) {
        if (card == null) return;

        Integer count = regularCards.get(card);
        if (count != null) {
            if (count > 1) {
                regularCards.put(card, count - 1);
            } else {
                regularCards.remove(card);
            }
            System.out.println("Удалена карта из коллекции: " + card.getId());
        }
    }
}