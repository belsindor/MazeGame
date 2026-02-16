package MazeGame.cards;

import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleResult;

import javax.swing.*;
import java.io.Serializable;
import java.util.Objects;
//++
public abstract class Card {

    protected String title;
    protected String name;
    protected final CardType type;
    protected final CardRarity rarity;
    protected final String imagePath;
    protected int copies = 1;
    protected TypeEffect effect;
    protected int id;

    protected Card(int id, CardType type, CardRarity rarity, TypeEffect effect, String imagePath) {
        this.id = id;
        this.type = type;
        this.rarity = rarity;
        this.effect = effect;
        this.imagePath = imagePath;
    }

//    protected Card(CardRarity rarity, TypeEffect effect) {
//        this.type = null; // или нужен дефолтный тип
//        this.rarity = rarity;
//        this.imagePath = "";
//    }

    /**
     * Карта применяется к игроку
     */
    public void playOnPlayer(BattleContext context, BattleResult result) {
        if (!canTargetPlayer()) {
            result.addMessage("❌ Карту нельзя применить к игроку");
            return;
        }

        context.setCurrentTarget(context.getPlayerSide());
        play(context, result);
    }

    /**
     * Карта применяется к суммону
     */
    public void playOnSummon(BattleContext context, BattleResult result) {
        if (context.getSummonSide() == null) {
            result.addMessage("❌ Нет активного суммона");
            return;
        }
        if (!canTargetSummon()) {
            result.addMessage("❌ Карту нельзя применить к суммону");
            return;
        }

        context.setCurrentTarget(context.getSummonSide());
        play(context, result);
    }

    /**
     * Карта применяется к врагу
     */
    public void playOnEnemy(BattleContext context, BattleResult result) {
        if (!canTargetEnemy()) {
            result.addMessage("❌ Карту нельзя применить к врагу");
            return;
        }

        context.setCurrentTarget(context.getEnemySide());
        play(context, result);
    }

    // === Ограничения (переопределяются при необходимости) ===

    protected boolean canTargetPlayer() {
        return true;
    }

    protected boolean canTargetSummon() {
        return true;
    }

    protected boolean canTargetEnemy() {
        return true;
    }

    public CardRarity getRarity() {
        return rarity;
    }

    public int getId() {
        return id;
    }

    public int getCopies() {
        return copies;
    }
    public CardType getType() {
        return type;
    }


    public String getImagePath() {
        return imagePath;
    }

    public abstract void play(BattleContext context, BattleResult result);

    public ImageIcon getImageIcon() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
    }

    protected Card(int id, CardType type, CardRarity rarity, TypeEffect effect) {
        this(id, type, rarity, effect,"");
    }

    public TypeEffect getEffect() {
        return effect;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {return title; }
}
