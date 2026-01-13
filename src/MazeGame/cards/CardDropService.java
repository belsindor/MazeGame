package MazeGame.cards;

import MazeGame.GameState;
import MazeGame.Monster;
import MazeGame.MonsterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class CardDropService {

    private static final Random RANDOM = new Random();

    private CardDropService() {}

    /* ================= ОСНОВНОЙ МЕТОД ================= */

    public static List<Card> generateDrop(Monster enemy) {

        List<Card> drop = new ArrayList<>();

        CardRarity monsterRarity =
                CardRarity.fromLevel(enemy.getLevel());

        // ===== 1️⃣ СУММОН (100%, ТОЛЬКО СООТВЕТСТВУЮЩЕЙ РЕДКОСТИ) =====
        drop.add(
                MonsterFactory.createSummonCardByRarity(monsterRarity)
        );

        // ===== 2️⃣ ДОПОЛНИТЕЛЬНАЯ КАРТА =====
        CardRarity rolled = rollRarity();

        if (rolled != null) {
            CardRarity finalRarity =
                    limitRarity(rolled, monsterRarity);

            drop.add(randomCardByRarity(finalRarity));
        }

        return drop;
    }

    public class MonsterDropService {

        public static void onMonsterKilled(Monster monster) {

            GameState state = GameState.get();

            // 1️⃣ обычные карты (бафы и т.п.)
            List<Card> drops = CardDropService.dropCards(monster);
            drops.forEach(state.cards()::add);

            // 2️⃣ суммон (100%)
            SummonCard summon = SummonFactory.fromMonster(monster);

            state.cards().add(summon);
            state.summons().tryAddOrReplace(summon);
        }
    }


    /* ================= ОГРАНИЧЕНИЕ РЕДКОСТИ ================= */

    private static CardRarity limitRarity(
            CardRarity rolled,
            CardRarity monster
    ) {
        return rolled.ordinal() <= monster.ordinal()
                ? rolled
                : monster;
    }

    /* ================= РЕДКОСТЬ (ШАНСЫ) ================= */

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

        if (pool.isEmpty()) {
            throw new IllegalStateException(
                    "Нет карт редкости: " + rarity
            );
        }

        return pool.get(RANDOM.nextInt(pool.size()));
    }

    public static SummonCard dropSummon(Monster monster) {
        return MonsterFactory.createSummonCard(monster);
    }


}
