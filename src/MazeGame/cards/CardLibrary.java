package MazeGame.cards;

import MazeGame.Monster;
import MazeGame.battle.effects.*;

import java.util.List;
//++
public final class CardLibrary {

    private CardLibrary() {}



    // ===== BUFF CARDS =====

    public static BuffCard ATTACK_BUFF_1_2() {
        return new BuffCard(1000,
                p -> new AttackBuffEffect(1, 2), CardRarity.GRAY, TypeEffect.ATTACK_BUFF,"/images/buff/attack_1_2.jpg"
        );
    }
    public static BuffCard ATTACK_BUFF_2_3() {
        return new BuffCard(1001,
                p -> new AttackBuffEffect(2, 3), CardRarity.GREEN,TypeEffect.ATTACK_BUFF,"/images/buff/attack_2_3.jpg"
        );
    }
    public static BuffCard ATTACK_BUFF_3_4() {
        return new BuffCard(1002,
                p -> new AttackBuffEffect(3, 4), CardRarity.BLUE,TypeEffect.ATTACK_BUFF,"/images/buff/attack_3_4.jpg"
        );
    }
    public static BuffCard ATTACK_BUFF_4_4() {
        return new BuffCard(1003,
                p -> new AttackBuffEffect(4, 4), CardRarity.VIOLETTE,TypeEffect.ATTACK_BUFF,"/images/buff/attack_4_4.jpg"
        );
    }
    public static BuffCard ATTACK_BUFF_5_5() {
        return new BuffCard(1004,
                p -> new AttackBuffEffect(5, 5), CardRarity.RED,TypeEffect.ATTACK_BUFF,"/images/buff/attack_5_5.jpg"
        );
    }
    public static BuffCard ATTACK_BUFF_6_6() {
        return new BuffCard(1005,
                p -> new AttackBuffEffect(6, 6), CardRarity.GOLD,TypeEffect.ATTACK_BUFF,"/images/buff/attack_6_6.jpg"
        );
    }
    public static BuffCard DEFENSE_BUFF_1_2() {
        return new BuffCard(1100,
                p -> new DefenseBuffEffect(1, 2), CardRarity.GRAY,TypeEffect.DEFENSE_BUFF,"/images/buff/defense_1_2.jpg"
        );
    }
    public static BuffCard DEFENSE_BUFF_2_2() {
        return new BuffCard(1101,
                p -> new DefenseBuffEffect(2, 2), CardRarity.GREEN,TypeEffect.DEFENSE_BUFF,"/images/buff/defense_2_2.jpg"
        );
    }
    public static BuffCard DEFENSE_BUFF_3_3() {
        return new BuffCard(1102,
                p -> new DefenseBuffEffect(3, 3), CardRarity.BLUE,TypeEffect.DEFENSE_BUFF,"/images/buff/defense_3_3.jpg"
        );
    }
    public static BuffCard DEFENSE_BUFF_4_4() {
        return new BuffCard(1103,
                p -> new DefenseBuffEffect(4, 4), CardRarity.VIOLETTE,TypeEffect.DEFENSE_BUFF,"/images/buff/defense_4_4.jpg"
        );
    }
    public static BuffCard DEFENSE_BUFF_5_5() {
        return new BuffCard(1104,
                p -> new DefenseBuffEffect(5, 5), CardRarity.RED,TypeEffect.DEFENSE_BUFF,"/images/buff/defense_5_5.jpg"
        );
    }
    public static BuffCard DEFENSE_BUFF_6_6() {
        return new BuffCard(1105,
                p -> new DefenseBuffEffect(6, 6), CardRarity.GOLD,TypeEffect.DEFENSE_BUFF,"/images/buff/defense_6_6.jpg"
        );
    }
    // ===== CURSE CARDS =====
    public static CurseCard CURSE_1_2() {
        return new CurseCard(1200,
                monster -> new AttackDebuffEffect(1, 2), CardRarity.GRAY,TypeEffect.CURSE,"/images/curses/curse_1_2.jpg"
        );
    }
    public static CurseCard CURSE_2_3() {
        return new CurseCard(1201,
                monster -> new AttackDebuffEffect(2, 3), CardRarity.GREEN,TypeEffect.CURSE,"/images/curses/curse_2_3.jpg"
        );
    }
    public static CurseCard CURSE_3_3() {
        return new CurseCard(1202,
                monster -> new AttackDebuffEffect(3, 3), CardRarity.BLUE,TypeEffect.CURSE,"/images/curses/curse_3_3.jpg"
        );
    }
    public static CurseCard CURSE_4_4() {
        return new CurseCard(1203,
                monster -> new AttackDebuffEffect(4, 4), CardRarity.VIOLETTE,TypeEffect.CURSE,"/images/curses/curse_4_4.jpg"
        );
    }
    public static CurseCard CURSE_5_5() {
        return new CurseCard(1204,
                monster -> new AttackDebuffEffect(5, 5), CardRarity.RED,TypeEffect.CURSE,"/images/curses/curse_5_5.jpg"
        );
    }
    public static CurseCard CURSE_6_6() {
        return new CurseCard(1205,
                monster -> new AttackDebuffEffect(6, 6), CardRarity.GOLD,TypeEffect.CURSE,"/images/curses/curse_6_6.jpg"
        );
    }
    // ===== POISON CARDS =====
    public static PoisonCard POISON_1_2() {
        return new PoisonCard(1300,
                monster -> new AttackPoisonEffect(1, 2), CardRarity.GRAY,TypeEffect.POISON,"/images/poison/poison_1_2.jpg"
        );
    }
    public static PoisonCard POISON_2_3() {
        return new PoisonCard(1301,
                monster -> new AttackPoisonEffect(2, 3), CardRarity.GREEN,TypeEffect.POISON,"/images/poison/poison_2_3.jpg"
        );
    }
    public static PoisonCard POISON_3_3() {
        return new PoisonCard(1302,
                monster -> new AttackPoisonEffect(3, 3), CardRarity.BLUE,TypeEffect.POISON,"/images/poison/poison_3_3.jpg"
        );
    }
    public static PoisonCard POISON_4_4() {
        return new PoisonCard(1303,
                monster -> new AttackPoisonEffect(4, 4), CardRarity.VIOLETTE,TypeEffect.POISON,"/images/poison/poison_4_4.jpg"
        );
    }
    public static PoisonCard POISON_5_5() {
        return new PoisonCard(1304,
                monster -> new AttackPoisonEffect(5, 5), CardRarity.RED,TypeEffect.POISON,"/images/poison/poison_5_5.jpg"
        );
    }
    public static PoisonCard POISON_6_6() {
        return new PoisonCard(1305,
                monster -> new AttackPoisonEffect(6, 6), CardRarity.GOLD,TypeEffect.POISON,"/images/poison/poison_6_6.jpg"
        );
    }


    // ===== REGENERATION CARDS =====
    public static RegenerationCard REGENERATION_1_2() {
        return new RegenerationCard(1400,
                p -> new RegenerationEffect(1, 2), CardRarity.GRAY,TypeEffect.REGENERATION,"/images/consumables/regeneration_1_2.jpg"
        );
    }
    public static RegenerationCard REGENERATION_2_3() {
        return new RegenerationCard(1401,
                p -> new RegenerationEffect(2, 3), CardRarity.GREEN,TypeEffect.REGENERATION,"/images/consumables/regeneration_2_3.jpg"
        );
    }

    // ===== CONSUMABLE CARDS =====
    public static ConsumableCard DESTRUCTION_UNIT_TYPE() {
        return new ConsumableCard(1500,
                ctx -> {
                    ctx.getEnemySide().addEffect(
                            new DisableUnitTypeEffect(5),
                            ctx
                    );
                },
                CardRarity.GOLD,
                TypeEffect.DESTRUCTION,"/images/consumables/delete_type.jpg"
        );
    }

    public static ConsumableCard METEOR_SHOWER() {
        return new ConsumableCard(1600,
                ctx -> ctx.getEnemy().takeDamage(50), CardRarity.GOLD,
                TypeEffect.METEOR_SHOWER,"/images/consumables/meteor_shower.jpg"
        );
    }
    public static ConsumableCard RESURRECTION() {
        return new ConsumableCard(1700,
                ctx -> {
                    Monster summon = ctx.getSummon();

                    if (summon == null) return;
                    if (summon.isAlive()) return;

                    summon.revive(); // ⬅ метод в Monster
                },
                CardRarity.GOLD,TypeEffect.RESURRECTION,"/images/consumables/resurrection.jpg"
        );
    }

    public static List<Card> getCardsByRarity(CardRarity rarity) {
        return ALL_CARDS.stream()
                .filter(c -> c.getRarity() == rarity)
                .toList();
    }

    private static final List<Card> ALL_CARDS = List.of(
            // Buff Cards
            ATTACK_BUFF_1_2(),
            ATTACK_BUFF_2_3(),
            ATTACK_BUFF_3_4(),
            ATTACK_BUFF_4_4(),
            ATTACK_BUFF_5_5(),
            ATTACK_BUFF_6_6(),
            DEFENSE_BUFF_1_2(),
            DEFENSE_BUFF_2_2(),
            DEFENSE_BUFF_3_3(),
            DEFENSE_BUFF_4_4(),
            DEFENSE_BUFF_5_5(),
            DEFENSE_BUFF_6_6(),

            // Curse Cards
            CURSE_1_2(),
            CURSE_2_3(),
            CURSE_3_3(),
            CURSE_4_4(),
            CURSE_5_5(),
            CURSE_6_6(),

            // Poison Cards
            POISON_1_2(),
            POISON_2_3(),
            POISON_3_3(),
            POISON_4_4(),
            POISON_5_5(),
            POISON_6_6(),

            // Regeneration Cards
            REGENERATION_1_2(),
            REGENERATION_2_3(),

            // Consumable Cards
            DESTRUCTION_UNIT_TYPE(),
            METEOR_SHOWER(),
            RESURRECTION()
    );


    public static List<Card> getAllCards() {
        return ALL_CARDS;
    }

}
