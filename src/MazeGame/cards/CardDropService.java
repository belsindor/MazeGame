package MazeGame.cards;


import MazeGame.MonsterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class CardDropService {

    private static final Random RANDOM = new Random();

    private CardDropService() {}

    /* ================= ОСНОВНОЙ МЕТОД ================= */

    public static List<Card> generateDrop(int playerLevel) {
        List<Card> drop = new ArrayList<>();

        // ===== 1️⃣ СУММОН (100%) =====
        drop.add(MonsterFactory.createSummonCard(playerLevel));

        // ===== 2️⃣ ДОПОЛНИТЕЛЬНАЯ КАРТА (ШАНСЫ) =====
        CardRarity rarity = rollRarity();
        if (rarity != null) {
            drop.add(randomCardByRarity(rarity));
        }

        return drop;
    }

    /* ================= РЕДКОСТЬ ================= */

    private static CardRarity rollRarity() {
        double roll = RANDOM.nextDouble() * 100;

        if (roll < 0.25) return CardRarity.GOLD;
        if (roll < 1.25) return CardRarity.RED;
        if (roll < 6.25) return CardRarity.VIOLETTE;
        if (roll < 21.25) return CardRarity.BLUE;
        if (roll < 46.25) return CardRarity.GREEN;
        if (roll < 81.25) return CardRarity.GRAY;

        return null; // ничего не выпало
    }

    /* ================= КАРТА ПО РЕДКОСТИ ================= */

    private static Card randomCardByRarity(CardRarity rarity) {
        List<Card> pool = CardLibrary.getCardsByRarity(rarity);
        return pool.get(RANDOM.nextInt(pool.size()));
    }
}
