package MazeGame.battle;

import MazeGame.UnitType;

public final class DamageCalculator {

    private DamageCalculator() {}

    public static int calculate(BattleSide attackerSide, BattleSide defenderSide) {
        BattleUnit attUnit = attackerSide.getUnit();
        BattleUnit defUnit = defenderSide.getUnit();

        int base = attackerSide.getAttack() - defenderSide.getDefense();
        base = Math.max(1, base);

        // Модификаторы по типу
        if (hasAdvantage(attUnit.getUnitType(), defUnit.getUnitType())) {
            base = (int) (base * 1.5);
        }

        return base;
    }

    private static boolean hasAdvantage(UnitType attacker, UnitType defender) {
        return (attacker == UnitType.FLYING && defender == UnitType.INFANTRY) ||
                (attacker == UnitType.INFANTRY && defender == UnitType.ARCHER) ||
                (attacker == UnitType.ARCHER && defender == UnitType.FLYING);
    }
}