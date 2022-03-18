import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Backtracking implements Strategy {
    Search search;

    public Backtracking(Search search) {
        this.search = search;
    }

    /**
     * Searches for card in the direction of the destination
     *
     * @param player      the agent that will be moved
     * @param destination the assumed place of a card
     * @return the agent that found the card
     */
    public Player findWayToPoint(Player player, Point destination) {
        if (player == null || destination == null)
            return null;
//        Comparator<Move> byDistance = Comparator.comparingInt((Move m) -> m.getMinimalMoves(destination));
        Comparator<Move> byDistance = Comparator.comparingInt((Move m) -> m.getDistanceTo(destination));
        return findWayToPoint(player, destination, byDistance, new boolean[Game.ROWS][Game.COLUMNS]);
    }

    /**
     * Searches for card going from player
     *
     * @param player the agent the algorithm working on
     * @param cmp    comparator used for neighbourhood priorities
     * @param used   what places where visited
     * @return the agent that found the card
     */
    public Player findWayToPoint(Player player, Point destination, Comparator<Move> cmp, boolean[][] used) {
        if (player.status == Status.LOST)
            return null;

        used[player.getX()][player.getY()] = true;
        search.history.add(player);

        if (player.coordinates.equals(destination))
            return player;

        Queue<Move> q = new PriorityQueue<>(1, cmp);
        q.addAll(search.getMoves(player));
//        for (Point p : Player.MOVES) {
//            Move move = new Move(player, p);
//            if (player.ok(move.coordinates))
//                q.add(move);
//        }
        while (!q.isEmpty()) {
            Move move = q.poll();
            if (!used[move.coordinates.x][move.coordinates.y]) {
                Player p2 = findWayToPoint(move.execute(), destination, cmp, used);
                if (p2 != null)
                    return p2;
            }
        }
        return null;
    }
}
