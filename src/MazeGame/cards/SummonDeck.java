package MazeGame.cards;

import MazeGame.Monster;
import MazeGame.UnitType;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
//+
public class SummonDeck implements Serializable {

    // Активные суммоны по типам (один на тип, лучший по раритету)
    private final Map<UnitType, SummonCard> active = new EnumMap<>(UnitType.class);
    private SummonCard selectedSummon = null;


    // И метод selectSummon может выглядеть так:
    public void selectSummon(SummonCard card) {
        if (card != null) {
            active.put(card.getUnitType(), card);
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

    // Удаление по конкретной карте
    public void removeSummon(UnitType type) {
        if (type != null) {
            SummonCard removed = active.remove(type);
            if (removed != null) {
                System.out.println("Удалён из active по типу " + type + ": " + removed.getName());
            }
        }
    }

    public void clearActive() {
        active.clear();
    }


    /**
     * Удалить суммон по экземпляру карты (вызывается при смерти в бою)
     */
    public void removeFromActive(SummonCard card) {
        if (card == null) return;
        UnitType type = card.getUnitType();
        SummonCard current = active.get(type);
        if (current != null && current.getId() == card.getId()) {
            active.remove(type);
            System.out.println("Удалён активный суммон: " + card.getName());
        }
    }

    /**
     * Удалить суммон по Monster (экземпляру из боя)
     */
    public void removeFromActive(Monster monster) {
        if (monster == null) return;

        // Предполагаем, что у SummonCard есть метод getMonsterTemplate() или id совпадает
        // Здесь лучше искать по id карты, которая соответствует этому монстру
        for (SummonCard sc : active.values()) {
            if (sc.getId() == monster.getId()) {  // или другой способ связи
                UnitType type = sc.getUnitType();
                active.remove(type);
                System.out.println("Удалён суммон по монстру: " + monster.getName());
                return;
            }
        }
    }
    /**
     * После апгрейда/добавления карт — пересобрать active суммоны
     * (вызывается из Player или BattleEngine после дропа/конвертации)
     */
    public void refreshActive(CardCollection collection) {
        active.clear();

        Map<UnitType, SummonCard> best = new EnumMap<>(UnitType.class);

        collection.getSummons().forEach((summon, count) -> {
            if (count <= 0) return;
            UnitType type = summon.getUnitType();
            SummonCard curr = best.get(type);
            if (curr == null || summon.getRarity().ordinal() > curr.getRarity().ordinal()) {
                best.put(type, summon);
            }
        });

        active.putAll(best);
    }


}