package MazeGame.battle;

import MazeGame.cards.Card;
import MazeGame.item.Item;

import java.util.ArrayList;
import java.util.List;

public class BattleResult {

    public final List<String> messages = new ArrayList<>();

    private boolean battleOver = false;
    private BattleOutcome outcome;
    private BattleReward reward;
    private final List addDrop = new ArrayList<>();

    private List<Item> items = new ArrayList<>();
    private List<Card> cards = new ArrayList<>();


    public void addMessage(String msg) {
        messages.add(msg);
    }

    public void setPlayerWin() {
        this.battleOver = true;
        this.outcome = BattleOutcome.PLAYER_WIN;
    }

    public void setPlayerLose() {
        this.battleOver = true;
        this.outcome = BattleOutcome.PLAYER_LOSE;
    }

    public boolean isPlayerWin() {
        return outcome == BattleOutcome.PLAYER_WIN;
    }

    public boolean isPlayerLose() {
        return outcome == BattleOutcome.PLAYER_LOSE;
    }

    public boolean isBattleOver() {
        return battleOver;
    }

    public BattleOutcome getOutcome() {
        return outcome;
    }
    public void setReward(BattleReward reward) {
        this.reward = reward;
    }

    public BattleReward getReward() {
        return reward;
    }

    public void setDroppedItems(List<Item> items) {
        this.items = items;
    }

    public void setDroppedCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Item> getDroppedItems() {
        return items;
    }

    public List<Card> getDroppedCards() {
        return cards;
    }


}