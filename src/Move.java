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
        if (player.status == Status.LOST)
            return player;
        return new Player(player, nextMove);
    }

    public int getDistanceTo(Point point) {
        Point p = Point.add(Point.not(coordinates), point);
        return Math.abs(p.x) + Math.abs(p.y);
    }

    public int getMinimalMoves(Point point) {
        Point p = Point.add(Point.not(coordinates), point);
        int x = Math.abs(p.x);
        int y = Math.abs(p.y);
        return Math.max(x, y);
    }

}
