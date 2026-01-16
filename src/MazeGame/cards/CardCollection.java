package MazeGame.cards;

import java.util.*;

public class CardCollection {

    private final Map<Card, Integer> regularCards = new HashMap<>();
    private final List<SummonCard> summons = new ArrayList<>();

    public void addRegularCard(Card card) {
        if (card == null) return;

        regularCards.merge(card, 1, Integer::sum);
        checkAndConvert(card);
    }

    public void addSummon(SummonCard summon) {
        if (summon != null) {
            summons.add(summon);
        }
    }

    public void addDrop(CardDropService.DropEntry entry) {
        if (entry == null) return;

        if (entry.getSummonCard() != null) {
            addSummon(entry.getSummonCard());
        } else if (entry.isCard()) {
            addRegularCard(entry.getCard());
        }
        // Item игнорируется — он идёт в инвентарь
    }

    private void checkAndConvert(Card currentCard) {
        int count = regularCards.getOrDefault(currentCard, 0);
        if (count < 10) {
            return;
        }

        Card nextCard = findNextLevelCard(currentCard);

        if (nextCard != null) {
            // Убираем 10 текущих карт
            regularCards.put(currentCard, count - 10);
            if (regularCards.get(currentCard) == 0) {
                regularCards.remove(currentCard);
            }

            // Добавляем одну карту следующего уровня
            // (рекурсия позволит обработать цепочку, если будет 100, 1000 и т.д.)
            addRegularCard(nextCard);

            // Можно здесь добавить событие/уведомление/лог/эффект
            // System.out.println("Улучшение: 10× " + currentCard + " → 1× " + nextCard);
        }
        // Если следующей карты нет → оставляем как есть (10+ штук)
    }

    /**
     * Находит карту следующего уровня по id текущей карты
     * id увеличивается на 1 в рамках одной линейки (buff attack, curse и т.д.)
     */
    private Card findNextLevelCard(Card current) {
        int currentId = current.getId();
        int nextId = currentId + 1;

        // Ищем карту с id = currentId + 1 среди всех карт
        return CardLibrary.getAllCards().stream()
                .filter(c -> c.getId() == nextId)
                .findFirst()
                .orElse(null);
    }

    // ── Геттеры и вспомогательные методы ─────────────────────────────────

    public Map<Card, Integer> getRegularCards() {
        return new HashMap<>(regularCards); // защитная копия
    }

    public List<SummonCard> getSummons() {
        return new ArrayList<>(summons);
    }

    public int getRegularCardCount() {
        return regularCards.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getSummonCount() {
        return summons.size();
    }

    public int getTotalCardCount() {
        return getRegularCardCount() + getSummonCount();
    }

    public void clear() {
        regularCards.clear();
        summons.clear();
    }
}

