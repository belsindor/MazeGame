package MazeGame.cards;

//+
import java.util.*;

public class CombatDeck {

    // Активные карты по эффектам (одна на эффект — лучшая по раритету)
    private final Map<TypeEffect, Card> active = new EnumMap<>(TypeEffect.class);

    public CombatDeck() {
        // Можно добавить начальные карты, если нужно
    }

    /**
     * Добавление одной новой карты (например, при дропе или апгрейде)
     */
    public void addCard(Card newCard) {
        if (newCard == null || newCard instanceof SummonCard) return;  // Игнорируем суммоны

        TypeEffect effect = getTypeEffectById(newCard.getId());
        if (effect == null) return;  // Неизвестный эффект — игнорируем

        Card current = active.get(effect);

        if (current == null ||
                newCard.getRarity().ordinal() > current.getRarity().ordinal()) {
            active.put(effect, newCard);
        }
    }

    /**
     * Полное обновление деки из коллекции regularCards
     */
    public void updateFromCollection(CardCollection collection) {
        active.clear();

        Map<TypeEffect, Card> bestByEffect = new EnumMap<>(TypeEffect.class);

        for (Map.Entry<Card, Integer> entry : collection.getAllCards().entrySet()) {
            Card card = entry.getKey();
            if (card instanceof SummonCard) continue;  // Пропускаем суммоны

            TypeEffect effect = getTypeEffectById(card.getId());
            if (effect == null) continue;

            Card currentBest = bestByEffect.get(effect);

            if (currentBest == null ||
                    card.getRarity().ordinal() > currentBest.getRarity().ordinal()) {
                bestByEffect.put(effect, card);
            }
        }

        active.putAll(bestByEffect);
    }
    // Вспомогательный метод: определяет эффект по id карты
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
            default -> null;                      // Неизвестный
        };
    }
    public Card getActiveByEffect(TypeEffect effect) {
        return active.get(effect);
    }

    public Map<TypeEffect, Card> getActiveCards() {
        return new EnumMap<>(active);
    }

    public boolean hasCardForEffect(TypeEffect effect) {
        return active.containsKey(effect);
    }

    public void removeCard(TypeEffect effect) {
        active.remove(effect);
    }

    public void clear() {
        active.clear();
    }
}