package MazeGame.cards;

import MazeGame.Monster;
import MazeGame.MonsterTemplate;
import MazeGame.UnitType;
import MazeGame.battle.BattleContext;
import MazeGame.battle.BattleResult;

public class SummonCard extends UnitCard {

    public SummonCard(MonsterTemplate template) {
        super(template);
    }

    @Override
    public CardType getType() {
        return CardType.SUMMON;
    }

    @Override
    public Monster summon() {
        return new Monster(template).createSummon();
    }

    @Override
    public void play(BattleContext context, BattleResult result) {
        // Базовая реализация призыва
        Monster m = summon();
        // Здесь можно добавить логику вставки в бой, если у вас есть соответствующие методы.
        // Пример (раскомментируйте и адаптируйте под ваш API):
//        context.addUnitToBattle(m);
//        result.registerSummon(m);
    }


}
