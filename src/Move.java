public class Move {
    final Player player;
    final Point move;
    final Point coordinates;
    Move(Player player, Point move) {
        this.player = player;
        this.move = move;
        coordinates = Point.add(player.coordinates, move);
    }

    Player execute() {
        return player.game.movePlayer(player, move);
    }
}
