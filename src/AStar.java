import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStar implements Strategy {
    final private Search search;

    public AStar(Search search) {
        this.search = search;
    }

    public Player findWayToPoint(Player player, Point destination) {
        if (player == null || destination == null)
            return null;
        return findWayToPoint(player, destination, new boolean[Game.ROWS][Game.COLUMNS]);
    }

    /**
     * Uses A* algorithm to find the shortest path to destination
     *
     * @param player      the initial agent
     * @param destination the point we go to
     * @param used        what places where visited
     * @return the agent that came to destination
     */
    Player findWayToPoint(Player player, Point destination, boolean[][] used) {
        if (player.coordinates.equals(destination))
            return player;
        Comparator<Move> byDistance = Comparator.comparingInt((Move m)
                -> m.getMinimalMoves(destination) + m.player.timer);
//                -> m.getDistanceTo(destination) + m.player.timer);
        Queue<Move> q = new PriorityQueue<>(1, byDistance);

        used[player.getX()][player.getY()] = true;
        search.add(player);

        for (Point move : Player.MOVES) {
            Move m = new Move(player, move);
            if (player.ok(m.coordinates) && !used[m.coordinates.x][m.coordinates.y]) {
                q.add(m);
            }
        }

        while (!q.isEmpty()) {
            Move current = q.poll();
            Player p = current.execute();
            if (used[p.getX()][p.getY()])
                continue;

            used[p.getX()][p.getY()] = true;
            search.add(p);



            if (p.coordinates.equals(destination)) {
                return p;
            }
            for (Point move : Player.MOVES) {
                Move m = new Move(p, move);
                if (p.ok(m.coordinates) && !used[m.coordinates.x][m.coordinates.y]) {
                    q.add(m);
                }
            }
        }
        return null;
    }
}
