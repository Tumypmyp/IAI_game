public class History {
    final Player[][] players = new Player[Game.ROWS][Game.COLUMNS];

    void add(Player player) {
        players[player.getX()][player.getY()] = player;
    }

}
