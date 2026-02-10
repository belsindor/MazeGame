package MazeGame.battle.effects;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleUnit;

//+
    public class AttackDebuffEffect extends AbstractBattleEffect {

        private final int penalty;

        public AttackDebuffEffect(int penalty, int duration) {
            super(duration);
            this.penalty = penalty;
        }

        @Override
        public int modifyAttack(BattleUnit unit, int baseAttack) {
            return baseAttack - penalty;
        }

        @Override
        public void onApply(BattleContext context) {
            context.addMessage("☠ Атака -" + penalty);
        }

        @Override
        public void onExpire(BattleContext context) {
            context.addMessage("☠ Проклятие атаки спало");
        }

        @Override
        public String getName() {
            return "Атака -" + penalty;
        }
    }

