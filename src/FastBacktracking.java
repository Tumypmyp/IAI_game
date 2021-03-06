import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class FastBacktracking implements Strategy {

    /**
     * Searches for card in the direction of the destination
     *
     * @param player      the agent that will be moved
     * @param destination the assumed place of a card
     * @return the agent that found the card
     */
    public Player findAWayToPoint(History history, Player player, Point destination) {
        if (player == null || destination == null || player.status == Status.LOST)
            return null;
//        Comparator<Move> byDistance = Comparator.comparingInt((Move m) -> m.getMinimalMoves(destination));
        Comparator<Move> byDistance = Comparator.comparingInt((Move m) -> m.getDistanceTo(destination));
        return findAWayToPoint(history, player, destination, byDistance, new boolean[Game.ROWS][Game.COLUMNS]);
    }

    /**
     * Searches for card going from player
     *
     * @param player the agent the algorithm working on
     * @param cmp    comparator used for neighbourhood priorities
     * @param used   what places where visited
     * @return the agent that found the card
     */
    public Player findAWayToPoint(History history, Player player, Point destination, Comparator<Move> cmp, boolean[][] used) {
        used[player.getX()][player.getY()] = true;
        history.add(player);

        if (player.coordinates.equals(destination))
            return player;

        Queue<Move> q = new PriorityQueue<>(cmp);
        q.addAll(history.getMoves(player));
        while (!q.isEmpty()) {
            Move move = q.poll();
            if (!used[move.coordinates.x][move.coordinates.y]) {
                Player p2 = findAWayToPoint(history, move.execute(), destination, cmp, used);
                if (p2 != null)
                    return p2;
            }
        }
        return null;
    }


    @Override
    public String getName() {
        return "Backtracking with time O(Game.ROWS * Game.COLUMNS)";
    }
}