import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStar implements Strategy {
    final private Search search;
    final private Player[][] history;

    public AStar(Search search, Player[][] history) {
        this.search = search;
        this.history = history;
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
        history[player.getX()][player.getY()] = player;

        if (player.getVisibleCardsByPoint(player.coordinates).contains(Card.BOOK))
            search.BOOK = player.coordinates;
        if (player.getVisibleCardsByPoint(player.coordinates).contains(Card.CLOAK))
            search.CLOAK = player.coordinates;

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
            history[p.getX()][p.getY()] = current.player;

            if (p.getVisibleCardsByPoint(p.coordinates).contains(Card.BOOK)) {
                search.BOOK = p.coordinates;
            }
            if (p.getVisibleCardsByPoint(p.coordinates).contains(Card.CLOAK)) {
                search.CLOAK = p.coordinates;
            }

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
