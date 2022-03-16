import java.util.*;

public class Backtracking implements Strategy {
    Game game;
    final private Player[][] history = new Player[Game.ROWS][Game.COLUMNS];

    Point BOOK;
    Point CLOAK;

    @Override
    public Strategy setGame(Game game) {
        this.game = game;
        return this;
    }

    /**
     * Searches for game solution
     *
     * @param debug indicates if debug output should be shown
     * @return the final solution as agent that won
     */
    @Override
    public Player run(boolean debug) {
        Player player = run2();
        if (player == null)
            player = game.initialPlayer;

        if (debug) {
            System.out.println("Backtracking:");
            System.out.println(game.getBoard());
            System.out.println(player);
            System.out.println(player.getPath());
            Game.showPath(player.getPath());
        }
        return player;
    }


    public Player run2() {
        Player p = dfsToPoint(new Point(9, 9));
        if (CLOAK == null) {
            return dfsToPoint(game.EXIT, dfsToPoint(BOOK));
        }
        p = dfsToPoint(new Point(9, 9), dfsToPoint(CLOAK));
        Player p1 = dfsToPoint(game.EXIT, dfsToPoint(BOOK));
        Player p2 = dfsToPoint(game.EXIT, dfsToPoint(CLOAK, dfsToPoint(BOOK)));
        Player p3 = dfsToPoint(game.EXIT, dfsToPoint(BOOK, dfsToPoint(CLOAK)));
        List<Player> list = new ArrayList<>();
        if (p1 != null)
            list.add(p1);
        if (p2 != null)
            list.add(p2);
        if (p3 != null)
            list.add(p3);
        list.sort(Comparator.comparingInt((Player p0) -> p0.timer));
        return list.get(0);
    }

    public Player dfsToPoint(Point destination) {
        return dfsToPoint(destination, game.initialPlayer);
    }

    /**
     * Searches for card in the direction of the destination
     *
     * @param destination the assumed place of a card
     * @param player      the agent that will be moved
     * @return the agent that found the card
     */
    public Player dfsToPoint(Point destination, Player player) {
        if (player == null || destination == null)
            return null;
        Comparator<Move> byDistance = Comparator.comparingInt((Move m) -> m.getDistanceTo(destination));
        return dfsToPoint(destination, byDistance, player, new boolean[Game.ROWS][Game.COLUMNS]);
    }

    /**
     * Searches for card going from player
     *
     * @param cmp    comparator used for neighbourhood priorities
     * @param player the agent the algorithm working on
     * @param used   what places where visited
     * @return the agent that found the card
     */
    public Player dfsToPoint(Point destination, Comparator<Move> cmp, Player player, boolean[][] used) {
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
                Player p2 = dfsToPoint(destination, cmp, move.execute(), used);
                if (p2 != null)
                    return p2;
            }
        }
        return null;
    }


    public String getHistory() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < Game.ROWS; i++) {
            for (int j = 0; j < Game.COLUMNS; j++) {
                Player p = history[i][j];
                result.append(p == null ? "-1" : p.timer).append("\t");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
