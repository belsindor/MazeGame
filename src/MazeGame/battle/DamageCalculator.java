package MazeGame.battle;

public final class DamageCalculator {

    private DamageCalculator() {}

    public static int calculate(BattleUnit attacker, BattleUnit defender) {

        int base = attacker.getAttack() - defender.getDefense();

        // минимальный урон — 1
        return Math.max(1, base);
    }
}
