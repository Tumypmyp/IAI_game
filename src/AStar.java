import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStar implements Strategy {
    Game game;
    final private Player[][] history = new Player[Game.ROWS][Game.COLUMNS];
    Point BOOK;
    Point CLOAK;

    public AStar(Game game) {
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


    public Player findWayToPoint(Point destination, Player player) {
        if (player == null || destination == null)
            return null;
        return findWayToPoint(destination, player, new boolean[Game.ROWS][Game.COLUMNS]);
    }

    /**
     * Uses A* algorithm to find the shortest path to destination
     *
     * @param destination the point we go to
     * @param player      the initial agent
     * @param used        what places where visited
     * @return the agent that came to destination
     */
    Player findWayToPoint(Point destination, Player player, boolean[][] used) {
        if (player.coordinates.equals(destination))
            return player;
        Comparator<Move> byDistance = Comparator.comparingInt((Move m)
                -> m.getDistanceTo(destination) + m.player.timer);
        Queue<Move> q = new PriorityQueue<>(1, byDistance);


        used[player.getX()][player.getY()] = true;

        if (player.getVisibleCardsByPoint(player.coordinates).contains(Card.BOOK))
            BOOK = player.coordinates;
        if (player.getVisibleCardsByPoint(player.coordinates).contains(Card.CLOAK))
            CLOAK = player.coordinates;

        for (Point move : Player.MOVES) {
            Move m = new Move(player, move);
            if (player.ok(m.coordinates) && !used[m.coordinates.x][m.coordinates.y]) {
                used[m.coordinates.x][m.coordinates.y] = true;
                q.add(m);
                history[m.coordinates.x][m.coordinates.y] = m.player;
            }
        }

        while (!q.isEmpty()) {
            Move current = q.poll();
            Player p = current.execute();
            history[p.coordinates.x][p.coordinates.y] = current.player;
            if (p.getVisibleCardsByPoint(p.coordinates).contains(Card.BOOK))
                BOOK = p.coordinates;
            if (p.getVisibleCardsByPoint(p.coordinates).contains(Card.CLOAK))
                CLOAK = p.coordinates;

            if (p.coordinates.equals(destination))
                return p;
            for (Point move : Player.MOVES) {
                Move m = new Move(p, move);
                if (p.ok(m.coordinates) && !used[m.coordinates.x][m.coordinates.y]) {
                    used[m.coordinates.x][m.coordinates.y] = true;
                    q.add(m);
                    history[m.coordinates.x][m.coordinates.y] = m.player;
                }
            }
        }
        return null;
    }

    public Player[][] getHistory() {
        return history;
    }
}
