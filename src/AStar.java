import java.util.*;

public class AStar implements Strategy {
    Game game;
    final private Move[][] history = new Move[Game.ROWS][Game.COLUMNS];

    Point BOOK;
    Point CLOAK;

    /**
     * Searches for game solution
     *
     * @param debug indicates if debug output should be shown
     * @return the final solution as agent that won
     */
    @Override
    public Player run(boolean debug) {
        Player p = findWayToPoint(new Point(9, 9), game.initialPlayer, new boolean[Game.ROWS][Game.COLUMNS]);

        findWayToPoint(new Point(9, 9), findWayToPoint(CLOAK, game.initialPlayer));

        List<Player> list = new ArrayList<>();
        p = findWayToPoint(game.EXIT, findWayToPoint(BOOK, game.initialPlayer));
        if (p != null) {
            list.add(p);
        }
        p = findWayToPoint(game.EXIT, findWayToPoint(BOOK, findWayToPoint(CLOAK, game.initialPlayer)));
        if (p != null) {
            list.add(p);
        }
        p = findWayToPoint(game.EXIT, findWayToPoint(CLOAK, findWayToPoint(BOOK, game.initialPlayer)));
        if (p != null) {
            list.add(p);
        }
        list.sort(Comparator.comparingInt((Player p0) -> p0.timer));
        if (list.isEmpty())
            return game.initialPlayer;
        p = list.get(0);
        if (debug) {
            System.out.println("A*:");
            System.out.println(game.getBoard());
            System.out.println(p);
            System.out.println(p.getPath());
            Game.showPath(p.getPath());
        }
        return p;
    }

    Player findWayToPoint(Point finish, Player player) {
        if (player == null || finish == null)
            return null;
        return findWayToPoint(finish, player, new boolean[Game.ROWS][Game.COLUMNS]);
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
                history[m.coordinates.x][m.coordinates.y] = m;
            }
        }


        while (!q.isEmpty()) {
            Move current = q.poll();
            Player p = current.execute();
            history[p.coordinates.x][p.coordinates.y] = current;
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
                    history[m.coordinates.x][m.coordinates.y] = m;
                }
            }
        }
        return null;
    }

    @Override
    public Strategy setGame(Game game) {
        this.game = game;
        return this;
    }


    public String getHistory() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < Game.ROWS; i++) {
            for (int j = 0; j < Game.COLUMNS; j++) {
                Move m = history[i][j];
                result.append(m == null ? "-1" : m.execute().timer).append("\t");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
