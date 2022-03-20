public class Move {
    final Player player;
    final Point nextMove;
    final Point coordinates;

    /**
     * Constructor that initiates Move instance and computes player future coordinates
     *
     * @param player player to move
     * @param move   point that indicates the movement vector
     */
    Move(Player player, Point move) {
        this.player = player;
        this.nextMove = move;
        coordinates = Point.add(player.coordinates, move);
    }

    /**
     * Executes the move on the player
     *
     * @return new player that moved
     */
    Player execute() {
        if (player.status == Status.LOST)
            return player;
        return new Player(player, nextMove);
    }

    /**
     * Heuristic function that finds the Manhattan distance between player and the point
     *
     * @param point point we find the distance to
     * @return the Manhattan distance
     */
    public int getDistanceTo(Point point) {
        Point p = Point.add(Point.not(coordinates), point);
        return Math.abs(p.x) + Math.abs(p.y);
    }

    /**
     * Heuristic function that finds minimal number of moves player should make to get to point
     *
     * @param point point we find the distance to
     * @return minimal number of moves
     */
    public int getMinimalMoves(Point point) {
        Point p = Point.add(Point.not(coordinates), point);
        int x = Math.abs(p.x);
        int y = Math.abs(p.y);
        return Math.max(x, y);
    }

}
