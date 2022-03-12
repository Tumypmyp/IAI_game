import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Backtracking implements Strategy {
    Game game;
    final private Player[][] history = new Player[Game.ROWS][Game.COLUMNS];

    Point BOOK;
    Point CLOAK;

    Backtracking() {
    }

    @Override
    public Strategy setGame(Game game) {
        this.game = game;
        return this;
    }

    @Override
    public Player run(boolean debug) {

        Player exitPlayer = dfsToCard(Card.EXIT, game.EXIT, game.initialPlayer);
//        Player bookPlayer = dfsToCard(Card.BOOK, new Point(0, 0), , new boolean[Game.ROWS][Game.COLUMNS]);

        if (debug) System.out.println(game.getBoard());
        if (debug) System.out.println(getHistory());

//        if (bookPlayer != null) {
//            if (debug) System.out.println(bookPlayer);
//
//
//            if (debug) System.out.println(getHistory());
//            if (debug) System.out.println(exitPlayer);
//            if (exitPlayer == null)
//                return game.initialPlayer;
//            return exitPlayer;
//        }
        return game.initialPlayer;
    }

    public Player dfsToCard(Card card, Point destination, Player player) {
        Comparator<Move> byDistance = Comparator.comparingInt((Move m) -> m.getDistanceTo(destination));
        return dfsToCard(card, byDistance, player, new boolean[Game.ROWS][Game.COLUMNS]);
    }

    public Player dfsToCard(Card card, Comparator<Move> cmp, Player player, boolean[][] used) {
        if (player.status == Status.LOST)
            return null;

        history[player.coordinates.x][player.coordinates.y] = player;
        used[player.coordinates.x][player.coordinates.y] = true;

        if (player.getVisibleCardsByPoint(player.coordinates).contains(Card.BOOK))
            BOOK = player.coordinates;
        if (player.getVisibleCardsByPoint(player.coordinates).contains(Card.CLOAK))
            CLOAK = player.coordinates;

        if (player.getVisibleCardsByPoint(player.coordinates).contains(card)) {
            return player;
        }

        Queue<Move> q = new PriorityQueue<>(1, cmp);
        for (Point move : Player.MOVES) {
            q.add(new Move(player, move));
        }
        while (!q.isEmpty()) {
            Move move = q.poll();
            if (player.ok(move.coordinates) && !used[move.coordinates.x][move.coordinates.y]) {
                Player p2 = dfsToCard(card, cmp, move.execute(), used);
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
