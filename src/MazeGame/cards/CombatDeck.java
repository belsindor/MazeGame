package MazeGame.cards;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;


/**
 * Боевая колода — содержит активные карты по эффектам (одна лучшая на каждый TypeEffect).
 * Обновляется автоматически из общей коллекции regularCards.
 */
public class CombatDeck {

    // Активные боевые карты по эффектам (одна на эффект — лучшая по раритету)
    private final Map<TypeEffect, Card> cards = new EnumMap<>(TypeEffect.class);
    private final Set<TypeEffect> usedThisBattle = EnumSet.noneOf(TypeEffect.class);

    public CombatDeck() {
//        cards.put(TypeEffect.CURSE, CURSE_1_2());
//        cards.put(TypeEffect.ATTACK_BUFF, ATTACK_BUFF_1_2());
//        cards.put(TypeEffect.DEFENSE_BUFF, DEFENSE_BUFF_1_2());
//        cards.put(TypeEffect.POISON, POISON_1_2());
//        cards.put(TypeEffect.REGENERATION, REGENERATION_1_2());
//        cards.put(TypeEffect.METEOR_SHOWER, METEOR_SHOWER());
//        cards.put(TypeEffect.RESURRECTION, RESURRECTION());

       // Можно добавить начальные карты, если нужно (по умолчанию пусто)
    }

    /**
     * Добавление одной новой карты (например, при дропе или апгрейде)
     * НЕ рекомендуется использовать напрямую при дропе — лучше добавлять в CardCollection и вызывать updateFromCollection
     */
    public void addCard(Card newCard) {
        if (newCard == null || newCard instanceof SummonCard) return;  // Игнорируем суммоны

        TypeEffect effect = getTypeEffectById(newCard.getId());
        if (effect == null) return;  // Неизвестный эффект — игнорируем

        Card current = cards.get(effect);

        if (current == null || newCard.getRarity().ordinal() > current.getRarity().ordinal()) {
            cards.put(effect, newCard);
        }
    }

    /**
     * Пересобрать активные боевые карты после изменений в коллекции
     */
    public void refreshActive(CardCollection collection) {
        cards.clear();

        Map<TypeEffect, Card> best = new EnumMap<>(TypeEffect.class);

        collection.getAllCards().forEach((card, count) -> {
            if (count <= 0 || card instanceof SummonCard) return;

            TypeEffect effect = getTypeEffectById(card.getId());
            if (effect == null) return;

            Card curr = best.get(effect);
            if (curr == null || card.getRarity().ordinal() > curr.getRarity().ordinal()) {
                best.put(effect, card);
            }
        });

        cards.putAll(best);
    }

    /**
     * Полное обновление боевой колоды на основе всей коллекции regularCards
     * Вызывается после каждого добавления карт в коллекцию (при дропе, покупке и т.д.)
     */
    public void updateFromCollection(CardCollection collection) {
        cards.clear();

        Map<TypeEffect, Card> bestByEffect = new EnumMap<>(TypeEffect.class);

        for (Map.Entry<Card, Integer> entry : collection.getAllCards().entrySet()) {
            Card card = entry.getKey();
            if (card instanceof SummonCard) continue;  // Пропускаем суммоны

            TypeEffect effect = getTypeEffectById(card.getId());
            if (effect == null) continue;

            Card currentBest = bestByEffect.get(effect);

            if (currentBest == null || card.getRarity().ordinal() > currentBest.getRarity().ordinal()) {
                bestByEffect.put(effect, card);
            }
        }

        cards.putAll(bestByEffect);
        System.out.println("CombatDeck обновлена: активных карт = " + cards.size());
    }

    // Вспомогательный метод: определяет эффект по id карты (можно улучшить по мере добавления новых карт)
    private TypeEffect getTypeEffectById(int id) {
        return switch (id / 100) {  // По диапазонам id
            case 10 -> TypeEffect.ATTACK_BUFF;    // 1001-1006
            case 11 -> TypeEffect.DEFENSE_BUFF;   // 1100-1105
            case 12 -> TypeEffect.CURSE;          // 1200-1205
            case 13 -> TypeEffect.POISON;         // 1300-1305
            case 14 -> TypeEffect.REGENERATION;   // 1400-1401
            case 15 -> TypeEffect.DESTRUCTION;    // 1500
            case 16 -> TypeEffect.METEOR_SHOWER;  // 1600
            case 17 -> TypeEffect.RESURRECTION;   // 1700
            default -> null;                      // Неизвестный эффект
        };
    }

    public boolean isUsed(TypeEffect effect) {
        return usedThisBattle.contains(effect);
    }

    public void markUsed(TypeEffect effect) {
        usedThisBattle.add(effect);
    }

    public void resetBattleUsage() {
        usedThisBattle.clear();
    }


    // Геттеры и утилиты
    public Card getActiveByEffect(TypeEffect effect) {
        return cards.get(effect);
    }

    public Map<TypeEffect, Card> getActiveCards() {
        return new EnumMap<>(cards);
    }

    public boolean hasCardForEffect(TypeEffect effect) {
        return cards.containsKey(effect);
    }

    public void removeCard(TypeEffect effect) {
        cards.remove(effect);
    }

    public void clear() {
        cards.clear();
    }

    public int size() {
        return cards.size();
    }
}