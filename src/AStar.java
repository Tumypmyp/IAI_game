import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStar implements Strategy {
    /**
     * @return string name of a strategy
     */
    @Override
    public String getName() {
        return "A*";
    }

    /**
     * Finds a way for a player to a point
     *
     * @param history     history that helps to determine all the possible directions
     * @param player      the initial agent
     * @param destination the point we go to
     * @return the player that achieved the destination
     */
    @Override
    public Player findAWayToPoint(History history, Player player, Point destination) {
        if (player == null || destination == null)
            return null;
        int[][] dist = new int[Game.ROWS][Game.COLUMNS];
        for (int i = 0; i < Game.ROWS; i++)
            Arrays.fill(dist[i], 1000);
        return findAWayToPoint(history, player, destination, dist);
    }

    /**
     * Uses A* algorithm to find the shortest path to destination
     *
     * @param history     history that helps to determine all the possible directions
     * @param player      the initial agent
     * @param destination the point we go to
     * @param dist        current shortest distance from the first player
     * @return the agent that that achieved the destination
     */
    Player findAWayToPoint(History history, Player player, Point destination, int[][] dist) {
        if (player.coordinates.equals(destination))
            return player;
        Comparator<Move> byDistance = Comparator.comparingInt((Move m)
//                -> m.getMinimalMoves(destination) + m.player.timer);
                -> m.getDistanceTo(destination) + m.player.timer);
        Queue<Move> q = new PriorityQueue<>(1, byDistance);

        dist[player.getX()][player.getY()] = player.timer;
        history.add(player);
        if (player.coordinates.equals(destination)) {
            return player;
        }
        for (Move move : history.getMoves(player)) {
            if (move.player.timer + 1 < dist[move.coordinates.x][move.coordinates.y]) {
                dist[move.coordinates.x][move.coordinates.y] = move.player.timer + 1;
                q.add(move);
            }
        }

        while (!q.isEmpty()) {
            Move current = q.poll();
            player = current.execute();
            if (dist[player.getX()][player.getY()] < player.timer)
                continue;

            dist[player.getX()][player.getY()] = player.timer;
            history.add(player);

            if (player.coordinates.equals(destination)) {
                return player;
            }
            for (Move move : history.getMoves(player)) {
                if (move.player.timer + 1 < dist[move.coordinates.x][move.coordinates.y]) {
                    dist[move.coordinates.x][move.coordinates.y] = move.player.timer + 1;
                    q.add(move);
                }
            }
        }
        return null;
    }
}
