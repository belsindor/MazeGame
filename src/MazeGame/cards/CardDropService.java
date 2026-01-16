package MazeGame.cards;

import MazeGame.item.Item;
import MazeGame.item.ItemFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//+
public class CardDropService {

    private static final Random RANDOM = new Random();

    // Шанс выпадения карты призыва (почти всегда)
    private static final double SUMMON_CARD_DROP_CHANCE = 1.00; // 100%

    // Шанс выпадения обычной карты (Buff/Curse/Poison/etc) или предмета
    private static final double CARD_OR_ITEM_DROP_CHANCE = 0.65; // 65%

    // Максимальное количество обычных карт/предметов за один дроп (кроме summon)
    private static final int MAX_ADDITIONAL_DROPS = 3;

    /**
     * Генерирует дроп после победы над монстром
     *
     * @param monsterLevel уровень монстра (1..5)
     * @return список выпавших карт и/или предметов
     */
    public List<DropEntry> generateDrop(int monsterLevel) {
        List<DropEntry> drops = new ArrayList<>();

        CardRarity maxRarity = getMaxRarityByMonsterLevel(monsterLevel);

        // Всегда 1 призыв
        SummonCard summon = pickRandomSummonCard(monsterLevel);
        if (summon != null) {
            drops.add(new DropEntry(summon));
        }

        // Почти всегда что-то ещё (85–95%)
        if (RANDOM.nextDouble() < 0.90) {
            // 60% шанс на карту, 40% на предмет
            if (RANDOM.nextDouble() < 0.60) {
                Card card = pickRandomNonSummonCard(maxRarity);
                if (card != null) drops.add(new DropEntry(card));
            } else {
                Item item = pickRandomItem(maxRarity);
                if (item != null) drops.add(new DropEntry(item));
            }
        }

        // Довольно часто — второй дополнительный дроп (45–55%)
        if (RANDOM.nextDouble() < 0.50) {
            if (RANDOM.nextDouble() < 0.55) {
                Card card = pickRandomNonSummonCard(maxRarity);
                if (card != null) drops.add(new DropEntry(card));
            } else {
                Item item = pickRandomItem(maxRarity);
                if (item != null) drops.add(new DropEntry(item));
            }
        }

        return drops;
    }

    // -------------------------------------------------------------------------
    // Вспомогательные методы
    // -------------------------------------------------------------------------

    private CardRarity getMaxRarityByMonsterLevel(int level) {
        return switch (level) {
            case 1 -> CardRarity.GRAY;
            case 2 -> CardRarity.GREEN;
            case 3 -> CardRarity.BLUE;
            case 4 -> CardRarity.VIOLETTE;
            case 5 -> CardRarity.RED;
            default -> CardRarity.GRAY;
        };
    }

    private SummonCard pickRandomSummonCard(int monsterLevel) {
        List<SummonCard> candidates = SummonFactory.ALL_SUMMON_CARDS.stream()
                .filter(card -> {
                    int cardLevel = card.getMonsterTemplate().level();
                    // ±1 уровень от монстра — основной диапазон
                    return cardLevel >= monsterLevel - 1 && cardLevel <= monsterLevel + 1;
                })
                .toList();

        if (candidates.isEmpty()) {
            return SummonFactory.ALL_SUMMON_CARDS.get(RANDOM.nextInt(SummonFactory.ALL_SUMMON_CARDS.size()));
        }

        return candidates.get(RANDOM.nextInt(candidates.size()));
    }

    private Card pickRandomNonSummonCard(CardRarity maxRarity) {
        List<Card> candidates = CardLibrary.getAllCards().stream()
                .filter(c -> c.getRarity().compareTo(maxRarity) <= 0)
                // Золотые обычные карты не дропаются (только consumable могут быть золотыми)
                .filter(c -> !(c.getRarity() == CardRarity.GOLD && !(c instanceof ConsumableCard)))
                .toList();

        if (candidates.isEmpty()) {
            return null;
        }

        return candidates.get(RANDOM.nextInt(candidates.size()));
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
    // Класс-обёртка для результата дропа
    // -------------------------------------------------------------------------
    public static class DropEntry {
        private final Card card;
        private final SummonCard summonCard;
        private final Item item;

        public DropEntry(Card card) {
            this.card = card;
            this.summonCard = null;
            this.item = null;
        }

        public DropEntry(Item item) {
            this.card = null;
            this.summonCard = null;
            this.item = item;
        }

        public DropEntry(SummonCard summon) {
            this.card = null;
            this.summonCard = summon;
            this.item = null;
        }

        public SummonCard getSummonCard() { return summonCard; }

        public boolean isCard() {
            return card != null;
        }

        public Card getCard() {
            return card;
        }

        public Item getItem() {
            return item;
        }

        @Override
        public String toString() {
            if (isCard()) {
                return "Card: " + card.getClass().getSimpleName() +
                        " (" + card.getRarity() + ")";
            } else {
                return "Item: " + item.getName() +
                        " (" + item.getRarity() + ")";
            }
        }
    }
}