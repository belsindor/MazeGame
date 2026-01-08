//package MazeGame;
//
//public class BattleLogic {
//
//    private final Player player;
//    private final Monster monster;
//    private int round = 1;
//
//    public BattleLogic(Player player, Monster monster) {
//        this.player = player;
//        this.monster = monster;
//    }
//
//
//    public String getStatus() {
//        return String.format(
//                "[%s] ❤ %d/%d ⚔ %d 🛡 %d\n" +
//                        "[%s] ❤ %d/%d ⚔ %d 🛡 %d\n",
//                player.getName(), player.getHealth(), player.getMaxHealth(),
//                player.getTotalAttack(), player.getTotalDefense(),
//                monster.getName(), monster.getHealth(), monster.getMaxHealth(),
//                monster.getAttack(), monster.getDefense()
//        );
//    }
//
//    public String playerAttack() {
//        StringBuilder log = new StringBuilder();
//        log.append("\n--- Раунд ").append(round++).append(" ---\n");
//
//        int damage = Math.max(1, player.getTotalAttack() - monster.getDefense());
//        monster.takeDamage(damage);
//        log.append("⚔ ").append(player.getName())
//                .append(" наносит ").append(damage).append(" урона!\n");
//
//        if (!monster.isAlive()) {
//            log.append(handleVictory());
//            return log.toString();
//        }
//
//        int monsterDamage = monster.getAttack();
//        player.takeDamage(monsterDamage);
//        log.append("☠ ").append(monster.getName())
//                .append(" наносит ").append(monsterDamage).append(" урона!\n");
//
//        if (!player.isAlive()) {
//            log.append("\n💀 Вы погибли...");
//        }
//
//        return log.toString();
//    }
//
//    public boolean tryEscape() {
//        return Math.random() < 0.8;
//    }
//
//    private String handleVictory() {
//        StringBuilder log = new StringBuilder("\n★ ПОБЕДА! ★\n");
//
//        int exp = monster.getExperienceReward();
//        player.gainExperience(exp);
//        log.append("✨ Получено опыта: ").append(exp).append("\n");
//
//        Item loot = ItemFactory.generateLoot(monster.getLevel());
//        if (loot != null) {
//            player.getInventory().addItem(loot);
//            log.append("🎁 Получен предмет: ").append(loot.getName()).append("\n");
//        }
//
//        player.getInventory().reduceEquipmentStrength();
//        return log.toString();
//    }
//
//    public boolean isBattleOver() {
//        return !player.isAlive() || !monster.isAlive();
//    }
//}
