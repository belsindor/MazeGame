package MazeGame.cards;

import MazeGame.Monster;

@FunctionalInterface
public interface CurseEffect {
    void apply(Monster target);
}
