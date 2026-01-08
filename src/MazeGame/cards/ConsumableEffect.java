package MazeGame.cards;


import MazeGame.battle.BattleContext;

@FunctionalInterface
public interface ConsumableEffect {
    void apply(BattleContext ctx);
}
