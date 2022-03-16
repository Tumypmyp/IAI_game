import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Backtracking implements Strategy {
    Game game;
    final private Player[][] history = new Player[Game.ROWS][Game.COLUMNS];
    Point BOOK;
    Point CLOAK;

    public Backtracking(Game game) {
        this.game = game;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public Point getBOOK() {
        return BOOK;
    }

    @Override
    public Point getCLOAK() {
        return CLOAK;
    }

    /**
     * Searches for card in the direction of the destination
     *
     * @param destination the assumed place of a card
     * @param player      the agent that will be moved
     * @return the agent that found the card
     */
    public Player findWayToPoint(Point destination, Player player) {
        if (player == null || destination == null)
            return null;
        Comparator<Move> byDistance = Comparator.comparingInt((Move m) -> m.getDistanceTo(destination));
        return findWayToPoint(destination, byDistance, player, new boolean[Game.ROWS][Game.COLUMNS]);
    }

    /**
     * Searches for card going from player
     *
     * @param cmp    comparator used for neighbourhood priorities
     * @param player the agent the algorithm working on
     * @param used   what places where visited
     * @return the agent that found the card
     */
    public Player findWayToPoint(Point destination, Comparator<Move> cmp, Player player, boolean[][] used) {
        if (player.status == Status.LOST)
            return null;

        history[player.getX()][player.getY()] = player;
        used[player.getX()][player.getY()] = true;

        if (player.getVisibleCardsByPoint(player.coordinates).contains(Card.BOOK))
            BOOK = player.coordinates;
        if (player.getVisibleCardsByPoint(player.coordinates).contains(Card.CLOAK))
            CLOAK = player.coordinates;

        if (player.coordinates.equals(destination))
            return player;
//        if (player.getVisibleCardsByPoint(player.coordinates).contains(card)) {
//            return player;
//        }

        Queue<Move> q = new PriorityQueue<>(1, cmp);
        for (Point p : Player.MOVES) {
            Move move = new Move(player, p);
            if (player.ok(move.coordinates))
                q.add(move);
        }
        while (!q.isEmpty()) {
            Move move = q.poll();
            if (!used[move.coordinates.x][move.coordinates.y]) {
                Player p2 = findWayToPoint(destination, cmp, move.execute(), used);
                if (p2 != null)
                    return p2;
            }
        }
        return null;
    }


    public Player[][] getHistory() {
        return history;
    }
}
