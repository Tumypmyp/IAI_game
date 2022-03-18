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
//                -> m.getMinimalMoves(destination) + m.player.timer);
                -> m.getDistanceTo(destination) + m.player.timer);
        Queue<Move> q = new PriorityQueue<>(1, byDistance);

        used[player.getX()][player.getY()] = true;
        search.history.add(player);

        for (Move move : search.getMoves(player)) {
            if (!used[move.coordinates.x][move.coordinates.y]) {
                q.add(move);
            }
        }

        while (!q.isEmpty()) {
            Move current = q.poll();
            player = current.execute();
            if (player == null)
                System.out.println("asdf");
            if (used[player.getX()][player.getY()])
                continue;

            used[player.getX()][player.getY()] = true;
            search.history.add(player);


            if (player.coordinates.equals(destination)) {
                return player;
            }
            for (Move move : search.getMoves(player)) {
                if (!used[move.coordinates.x][move.coordinates.y]) {
                    q.add(move);
                }
            }
        }
        return null;
    }
}
