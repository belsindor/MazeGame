package MazeGame.battle.effects;

import MazeGame.battle.BattleUnit;
//+
@FunctionalInterface
public interface BuffEffect {
    void apply(BattleUnit target);
}
