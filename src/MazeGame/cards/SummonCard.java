package MazeGame.cards;


import MazeGame.MonsterTemplate;

//+
public class SummonCard {

    private final MonsterTemplate template;
    // другие поля...

    public SummonCard(MonsterTemplate template /*, другие параметры */) {
        this.template = template;
        // ...
    }

    // Вариант А — самый правильный
    public MonsterTemplate getMonsterTemplate() {
        return template;
    }

    // Вариант Б — ещё удобнее для частого использования
    public int getLevel() {
        return template.level();
    }

    // Желательно также:
    public String getSummonName() {
        return template.name();
    }
}
