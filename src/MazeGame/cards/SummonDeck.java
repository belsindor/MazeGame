package MazeGame.cards;

import MazeGame.UnitType;

import java.util.EnumMap;
import java.util.Map;
//+
public class SummonDeck {

    // Активные суммоны по типам (один на тип, лучший по раритету)
    private final Map<UnitType, SummonCard> active = new EnumMap<>(UnitType.class);
    private SummonCard selectedSummon = null;


    public void selectSummon(SummonCard summon) {
        if (summon == null) {
            selectedSummon = null;
            return;
        }

        // Проверяем, что этот суммон действительно есть среди активных
        if (active.containsValue(summon)) {
            selectedSummon = summon;
        }
    }

    /**
     * Получить выбранный игроком суммон (или null, если ничего не выбрано)
     */
    public SummonCard getSelectedSummon() {
        return selectedSummon;
    }

    /**
     * Сбросить выбор после боя (опционально)
     */
    public void resetSelection() {
        selectedSummon = null;
    }

    /**
     * Конструктор — можно инициализировать пустым или с начальными суммонами
     */
    public SummonDeck() {


        // Если нужно добавить дефолтные суммоны — добавьте здесь
    }

    /**
     * Добавление нового суммона из regularCards
     * Проверяет тип, сравнивает раритет и заменяет, если новая лучше
     *
     * @param newSummon новая карта суммона
     */
    public void addSummon(SummonCard newSummon) {
        if (newSummon == null) return;

        UnitType type = newSummon.getMonsterTemplate().unitType();

        SummonCard current = active.get(type);

        if (current == null) {
            // Если слот пустой — просто добавляем
            active.put(type, newSummon);
            return;
        }

        // Сравниваем раритет (предполагаем, что CardRarity — enum в порядке GRAY < GREEN < ... < GOLD)
        if (newSummon.getRarity().ordinal() > current.getRarity().ordinal()) {
            // Заменяем на лучшую
            active.put(type, newSummon);
        }
        // Если раритет равен или ниже — ничего не меняем
    }

    /**
     * Инициализация/обновление деки из всей коллекции regularCards
     * (если нужно полностью пересобрать active на основе всех доступных суммонов)
     *
     * @param collection коллекция карт игрока
     */
    public void updateFromCollection(CardCollection collection) {
        active.clear();

        Map<UnitType, SummonCard> bestByType = new EnumMap<>(UnitType.class);

        for (Map.Entry<Card, Integer> entry : collection.getAllCards().entrySet()) {
            Card card = entry.getKey();
            if (card instanceof SummonCard summon) {
                UnitType type = summon.getUnitType();
                SummonCard currentBest = bestByType.get(type);

                if (currentBest == null ||
                        summon.getRarity().ordinal() > currentBest.getRarity().ordinal()) {
                    bestByType.put(type, summon);
                }
            }
        }

        active.putAll(bestByType);
    }

    // ---------------------- Геттеры и утилиты ----------------------

    /**
     * Получить активного суммона по типу
     */
    public SummonCard getActiveByType(UnitType type) {
        return active.get(type);
    }

    /**
     * Получить все активные суммоны
     */
    public Map<UnitType, SummonCard> getActiveSummons() {
        return new EnumMap<>(active);
    }

    /**
     * Проверить, есть ли суммон для типа
     */
    public boolean hasSummonForType(UnitType type) {
        return active.containsKey(type);
    }

    /**
     * Удалить суммон по типу
     */
    public void removeSummon(UnitType type) {
        active.remove(type);
    }

    public void clear() {
        active.clear();
    }
}