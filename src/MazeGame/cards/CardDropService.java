package MazeGame.cards;

import MazeGame.item.Item;
import MazeGame.item.ItemFactory;
import MazeGame.Monster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardDropService implements Serializable {

    private static final Random RANDOM = new Random();

    // Всегда 100% — суммон-карта (по лору душелов)
    private static final double SUMMON_DROP_CHANCE = 1.00;

    // Шанс на дополнительную награду (карта ИЛИ предмет) — 65%
    private static final double EXTRA_DROP_CHANCE = 0.65;

    // Из доп. награды: 60% шанс на карту, 40% на предмет
    private static final double EXTRA_IS_CARD_CHANCE = 0.60;

    // Кумулятивные шансы по раритету (чем выше — тем реже)
    // Сумма = 0.76 → ~24% шанс ничего не получить даже при выпадении доп. слота
    private static final double[] RARITY_CUMULATIVE_CHANCES = {
            0.25,   // GRAY     25%
            0.45,   // GREEN    +20%
            0.60,   // BLUE     +15%
            0.70,   // VIOLETTE +10%
            0.75,   // RED      +5%
            0.76    // GOLD     +1%
    };

    public List<DropEntry> generateDrop(Monster monster) {
        List<DropEntry> drops = new ArrayList<>();

        int monsterLevel = monster.getLevel();
        CardRarity maxRarity = getMaxRarityByMonsterLevel(monsterLevel);

        // 1. Суммон-карта — всегда (душелов)
        if (RANDOM.nextDouble() < SUMMON_DROP_CHANCE) {
            SummonCard summon = pickSummonByMonsterId(monster.getId());
            if (summon != null) {
                drops.add(new DropEntry(summon));
            }
        }

        // 2. Дополнительная награда — максимум одна
        if (RANDOM.nextDouble() < EXTRA_DROP_CHANCE) {
            if (RANDOM.nextDouble() < EXTRA_IS_CARD_CHANCE) {
                Card card = pickRandomNonSummonCard(maxRarity);
                if (card != null) {
                    drops.add(new DropEntry(card));
                }
            } else {
                Item item = pickRandomItem(maxRarity);
                if (item != null) {
                    drops.add(new DropEntry(item));
                }
            }
        }

        return drops;
    }

    // -------------------------------------------------------------------------

    private CardRarity getMaxRarityByMonsterLevel(int level) {
        return switch (level) {
            case 1 -> CardRarity.GRAY;
            case 2 -> CardRarity.GREEN;
            case 3 -> CardRarity.BLUE;
            case 4 -> CardRarity.VIOLETTE;
            case 5 -> CardRarity.RED;
            default -> CardRarity.GRAY;  // или можно RED/GOLD для очень высоких уровней
        };
    }

    private SummonCard pickSummonByMonsterId(int monsterId) {
        int baseId = monsterId / 10;

        for (SummonCard card : SummonFactory.ALL_SUMMON_CARDS) {
            if (card.getId() == baseId) {
                return card;
            }
        }

        // fallback — случайный суммон (можно сделать по уровню монстра)
        if (!SummonFactory.ALL_SUMMON_CARDS.isEmpty()) {
            return SummonFactory.ALL_SUMMON_CARDS.get(
                    RANDOM.nextInt(SummonFactory.ALL_SUMMON_CARDS.size())
            );
        }
        return null;
    }

    /**
     * Выбор случайной НЕ-суммон карты с учётом максимального раритета и шансов
     */
    private Card pickRandomNonSummonCard(CardRarity maxRarity) {
        List<Card> candidates = CardLibrary.getAllCards().stream()
                .filter(c -> !(c instanceof SummonCard))  // только обычные карты
                .filter(c -> c.getRarity().compareTo(maxRarity) <= 0)
                .filter(c -> !(c.getRarity() == CardRarity.GOLD && !(c instanceof ConsumableCard)))
                .toList();

        if (candidates.isEmpty()) {
            return null;
        }

        // Выбираем раритет по шансам
        CardRarity chosenRarity = chooseRarity(maxRarity);

        // Фильтруем только карты нужного раритета
        List<Card> filtered = candidates.stream()
                .filter(c -> c.getRarity() == chosenRarity)
                .toList();

        if (filtered.isEmpty()) {
            // если такого раритета нет — берём любую из доступных
            return candidates.get(RANDOM.nextInt(candidates.size()));
        }

        return filtered.get(RANDOM.nextInt(filtered.size()));
    }

    private CardRarity chooseRarity(CardRarity maxAllowed) {
        double roll = RANDOM.nextDouble();
        int maxIndex = getMaxRarityIndex(maxAllowed);

        for (int i = 0; i <= maxIndex; i++) {
            if (roll <= RARITY_CUMULATIVE_CHANCES[i]) {
                return CardRarity.values()[i];
            }
        }
        return CardRarity.GRAY; // fallback
    }

    private int getMaxRarityIndex(CardRarity maxRarity) {
        return switch (maxRarity) {
            case GRAY     -> 0;
            case GREEN    -> 1;
            case BLUE     -> 2;
            case VIOLETTE -> 3;
            case RED      -> 4;
            case GOLD     -> 5;
        };
    }

    private Item pickRandomItem(CardRarity maxRarity) {
        List<Item> candidates = ItemFactory.ALL_ITEMS.stream()
                .filter(item -> item.getRarity().compareTo(maxRarity) <= 0)
                .toList();

        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.get(RANDOM.nextInt(candidates.size()));
    }

    // -------------------------------------------------------------------------
    // DropEntry — оставляем почти как было
    // -------------------------------------------------------------------------
    public static class DropEntry {
        private final Card card;
        private final SummonCard summonCard;
        private final Item item;

        public DropEntry(Card card) {
            this.card = card instanceof SummonCard ? null : card;
            this.summonCard = card instanceof SummonCard ? (SummonCard) card : null;
            this.item = null;
        }

        public DropEntry(SummonCard summon) {
            this.card = null;
            this.summonCard = summon;
            this.item = null;
        }

        public DropEntry(Item item) {
            this.card = null;
            this.summonCard = null;
            this.item = item;
        }

        public SummonCard getSummonCard() { return summonCard; }
        public Card getCard() { return card; }
        public Item getItem() { return item; }

        @Override
        public String toString() {
            if (summonCard != null) return "Summon: " + summonCard.getName() + " (" + summonCard.getRarity() + ")";
            if (card != null)       return "Card: " + card.getImageIcon() + " (" + card.getRarity() + ")";
            if (item != null)       return "Item: " + item.getName() + " (" + item.getRarity() + ")";
            return "—";
        }
    }
}