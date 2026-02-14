package MazeGame.cards;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CardCollection implements Serializable {

    private final Map<Card, Integer> regularCards = new HashMap<>();

    /**
     * Добавление карты (сразу запускает проверку на конвертацию)
     */
    public void addCard(Card card) {
        if (card == null) return;

        regularCards.merge(card, 1, Integer::sum);
        checkAndUpgrade(card.getId());  // ← передаём id, чтобы работать с цепочкой
    }

    /**
     * Добавление дропа — удобно для BattleEngine / processDroppedCards
     */
    public void addDrop(CardDropService.DropEntry entry) {
        if (entry == null) return;

        if (entry.getSummonCard() != null) {
            addCard(entry.getSummonCard());
        } else if (entry.getCard() != null) {
            addCard(entry.getCard());
        }
        // Item обрабатывается отдельно (в инвентаре)
    }

    /**
     * Проверка и апгрейд для конкретного id (рекурсивно)
     */
    private void checkAndUpgrade(int baseId) {
        Card current = CardLibrary.getCardById(baseId);
        if (current == null) return;

        int count = regularCards.getOrDefault(current, 0);
        if (count < 10) return;

        Card next = CardLibrary.getCardById(baseId + 1);
        if (next == null) {
            // Нет следующей карты — оставляем как есть
            return;
        }

        // Конвертируем: -10 текущих → +1 следующая
        regularCards.put(current, count - 10);
        if (regularCards.get(current) <= 0) {
            regularCards.remove(current);
        }

        regularCards.merge(next, 1, Integer::sum);

        // Сообщение игроку (опционально)
        System.out.println("10× " + current.getName() + " → 1× " + next.getName());

        // Рекурсия: вдруг теперь у следующей тоже ≥10
        checkAndUpgrade(next.getId());
    }

    // ── Удаление при смерти суммона или других случаях

    public void removeCard(Card card) {
        if (card == null) return;
        Integer cnt = regularCards.get(card);
        if (cnt == null) return;

        if (cnt > 1) {
            regularCards.put(card, cnt - 1);
        } else {
            regularCards.remove(card);
        }
    }

    public void removeCardById(int id) {
        Card card = CardLibrary.getCardById(id);
        if (card != null) {
            removeCard(card);
        }
    }

    // ── Геттеры ─────────────────────────────────────────────────────────────

    public Map<Card, Integer> getAllCards() {
        return new HashMap<>(regularCards);
    }

    public Map<SummonCard, Integer> getSummons() {
        Map<SummonCard, Integer> summons = new HashMap<>();
        regularCards.forEach((card, cnt) -> {
            if (card instanceof SummonCard s) {
                summons.put(s, cnt);
            }
        });
        return summons;
    }

    public int getCount(Card card) {
        return regularCards.getOrDefault(card, 0);
    }

    public int getCountById(int id) {
        Card card = CardLibrary.getCardById(id);
        return card != null ? getCount(card) : 0;
    }

    public boolean hasCard(Card card) {
        return regularCards.containsKey(card) && regularCards.get(card) > 0;
    }

    public void clear() {
        regularCards.clear();
    }

    public void restoreCard(Card card, int amount) {
        if (card == null || amount <= 0) return;
        regularCards.put(card, amount);
    }

    public Map<Integer, Integer> getAllCardIds() {
        Map<Integer, Integer> result = new HashMap<>();
        regularCards.forEach((card, count) -> {
            result.put(card.getId(), count);
        });
        return result;
    }

}