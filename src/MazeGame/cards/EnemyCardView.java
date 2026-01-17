package MazeGame.cards;


import MazeGame.Monster;
//+
class EnemyCardView {

    private final Monster monster;

    EnemyCardView(Monster monster) {
        this.monster = monster;
    }

    String getImagePath() {
        return "/images/monsters/" +
                monster.getName().replace(" ", "_") +
                ".jpg";
    }
}

