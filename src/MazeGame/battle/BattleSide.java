package MazeGame.battle;

import MazeGame.battle.effects.BattleEffect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//+
public class BattleSide {

    private final BattleUnit unit;
    private final List<BattleEffect> effects = new ArrayList<>();

    public BattleSide(BattleUnit unit) {
        this.unit = unit;
    }

    // ===== ЭФФЕКТЫ =====

    public void addEffect(BattleEffect effect, BattleContext context) {
        effects.add(effect);
        effect.setTarget(unit);
        effect.onApply(context);
    }

    public void onTurnStart(BattleContext context) {
        effects.forEach(e -> e.onTurnStart(context));
    }

    public void onTurnEnd(BattleContext context) {
        Iterator<BattleEffect> it = effects.iterator();
        while (it.hasNext()) {
            BattleEffect e = it.next();
            e.onTurnEnd(context);
            if (e.isExpired()) {
                e.onExpire(context);
                it.remove();
            }
        }
    }

    // ===== БОЕВЫЕ СТАТЫ =====

    public int getAttack() {
        int value = unit.getTotalAttack();
        for (BattleEffect e : effects) {
            value = e.modifyAttack(unit, value);
        }
        return value;
    }

    public int getDefense() {
        int value = unit.getTotalDefense();
        for (BattleEffect e : effects) {
            value = e.modifyDefense(unit, value);
        }
        return value;
    }

    // ===== ПРОКСИ =====

    public BattleUnit getUnit() {
        return unit;
    }

    public boolean isAlive() {
        return unit.isAlive();
    }

    public void takeDamage(int dmg) {
        unit.takeDamage(dmg);
    }

    public String getName() {
        return unit.getName();
    }

    public int getLevel() {
        return unit.getLevel();
    }

    public List<BattleEffect> getEffects() {
        return effects;
    }

}
