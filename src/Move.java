public class Move {
    final Player player;
    final Point nextMove;
    final Point coordinates;

    Move(Player player, Point move) {
        this.player = player;
        this.nextMove = move;
        coordinates = Point.add(player.coordinates, move);
    }

    Player execute() {
        return player.game.movePlayer(player, nextMove);
    }

    public int getDistanceTo(Point point) {
        Point p = Point.add(Point.not(coordinates), point);
        return Math.abs(p.x) + Math.abs(p.y);
    }
}
