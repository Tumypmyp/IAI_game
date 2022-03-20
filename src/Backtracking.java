import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Backtracking implements Strategy {
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
        Comparator<Move> byDistance = Comparator.comparingInt((Move m) -> m.getMinimalMoves(destination));
//        Comparator<Move> byDistance = Comparator.comparingInt((Move m) -> m.getDistanceTo(destination));


        int[][] dist = new int[Game.ROWS][Game.COLUMNS];
        for (int i = 0; i < Game.ROWS; i++)
            Arrays.fill(dist[i], 1000);
        return findAWayToPoint(history, player, destination, byDistance, dist);
    }

    @Override
    public String getName() {
        return "Backtracking (full brute force with clipping)";
    }

    /**
     * Searches for card going from player
     *
     * @param player the agent the algorithm working on
     * @param cmp    comparator used for neighbourhood priorities
     * @return the agent that found the card
     */
    public Player findAWayToPoint(History history, Player player, Point destination, Comparator<Move> cmp, int[][] dist) {
        dist[player.getX()][player.getY()] = player.timer;
        history.add(player);

        if (player.coordinates.equals(destination))
            return player;

        Queue<Move> q = new PriorityQueue<>(cmp);
        q.addAll(history.getMoves(player));

        Player minPlayer = null;
        while (!q.isEmpty()) {
            Move move = q.poll();
            if (move.player.timer + 1 < dist[move.coordinates.x][move.coordinates.y]) {
                Player p2 = findAWayToPoint(history, move.execute(), destination, cmp, dist);
                if (minPlayer == null || (p2 != null && p2.timer < minPlayer.timer))
                    minPlayer = p2;
            }
        }
        return minPlayer;
    }
}
