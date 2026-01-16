package MazeGame.battle;

import MazeGame.UnitType;
//+
public final class DamageCalculator {

    private DamageCalculator() {}

    public static int calculate(BattleUnit attacker, BattleUnit defender) {
        int base = attacker.getTotalAttack() - defender.getTotalDefense();
        base = Math.max(1, base);

        // Модификаторы по типу
        if (hasAdvantage(attacker.getUnitType(), defender.getUnitType())) {
            base = (int)(base * 1.5);
        }

        return base;
    }

    private static boolean hasAdvantage(UnitType attacker, UnitType defender) {
        return (attacker == UnitType.FLYING && defender == UnitType.INFANTRY) ||
                (attacker == UnitType.INFANTRY && defender == UnitType.ARCHER) ||
                (attacker == UnitType.ARCHER && defender == UnitType.FLYING);
    }

}

