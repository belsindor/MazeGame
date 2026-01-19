package MazeGame.cards;

import MazeGame.item.Item;
import MazeGame.item.ItemFactory;
import MazeGame.Monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardDropService {

    private static final Random RANDOM = new Random();

    // Шанс выпадения суммона — всегда 100%
    private static final double SUMMON_CARD_DROP_CHANCE = 1.00;

    // Общий шанс выпадения дополнительного (карта или предмет) — 65%
    private static final double ADDITIONAL_DROP_CHANCE = 0.65;

    // Шансы по раритету (кумулятивно суммируются до 76%, остальное — "ничего" или ниже)
    private static final double[] RARITY_CHANCES = {
            0.25,  // GRAY
            0.20,  // GREEN
            0.15,  // BLUE
            0.10,  // VIOLETTE
            0.05,  // RED
            0.01   // GOLD
    };

    /**
     * Генерирует дроп после победы над монстром
     *
     * @param monster монстр (для ID и уровня)
     * @return список выпавших карт/предметов (1 суммон + макс. 1 доп.)
     */
    public List<DropEntry> generateDrop(Monster monster) {
        List<DropEntry> drops = new ArrayList<>();

        int monsterLevel = monster.getLevel();
        int monsterId = monster.getId();
        CardRarity maxRarity = getMaxRarityByMonsterLevel(monsterLevel);

        // Всегда 1 суммон по ID монстра
        SummonCard summon = pickSummonByMonsterId(monster.getId());
        if (summon != null) {
            drops.add(new DropEntry(summon));
        }

        // Дополнительный дроп (карта или предмет) — максимум один
        if (RANDOM.nextDouble() < ADDITIONAL_DROP_CHANCE) {
            // 60% шанс на карту, 40% на предмет
            if (RANDOM.nextDouble() < 0.60) {
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
            default -> CardRarity.GRAY;
        };
    }

    /**
     * Выбирает суммон-карту по ID монстра (ID карты = ID монстра / 10)
     * Если не найден — случайный суммон
     */
    private SummonCard pickSummonByMonsterId(int monsterId) {
        int baseId = monsterId / 10;

        // Ищем суммон по baseId
        for (SummonCard card : SummonFactory.ALL_SUMMON_CARDS) {
            if (card.getId() == baseId) {
                return card;
            }
        }

        // Если не нашли — случайный
        return SummonFactory.ALL_SUMMON_CARDS.get(RANDOM.nextInt(SummonFactory.ALL_SUMMON_CARDS.size()));
    }

    private SummonCard pickRandomSummonCard(int monsterLevel) {
        List<SummonCard> candidates = SummonFactory.ALL_SUMMON_CARDS.stream()
                .filter(card -> {
                    int cardLevel = card.getMonsterTemplate().level();
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