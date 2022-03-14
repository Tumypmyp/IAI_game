import java.util.*;

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
        if (debug) System.out.println(game.getBoard());
        Player player = run2(debug);
        if (player == null)
            player = game.initialPlayer;
        if (debug) System.out.println(game.getBoard());
        if (debug) System.out.println(getHistory());
        if (debug) System.out.println(player);
        return player;
    }

    public Player run2(boolean debug) {
        Player p = dfsToCard(Card.CLOAK, game.EXIT, game.initialPlayer);
        if (p == null) {
            return dfsToCard(Card.EXIT, game.EXIT, dfsToCard(Card.BOOK, BOOK, game.initialPlayer));
        }
        p = dfsToCard(Card.BOOK, new Point(0, 0), p);
        Player p1 = dfsToCard(Card.EXIT, game.EXIT, dfsToCard(Card.BOOK, BOOK, game.initialPlayer));
        Player p2 = dfsToCard(Card.EXIT, game.EXIT, dfsToCard(Card.CLOAK, CLOAK, dfsToCard(Card.BOOK, BOOK, game.initialPlayer)));
        Player p3 = dfsToCard(Card.EXIT, game.EXIT, dfsToCard(Card.BOOK, BOOK, dfsToCard(Card.CLOAK, CLOAK, game.initialPlayer)));
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

    public Player dfsToCard(Card card, Point destination, Player player) {
        if (player == null || destination == null)
            return null;
        Comparator<Move> byDistance = Comparator.comparingInt((Move m) -> m.getDistanceTo(destination));
        return dfsToCard(card, byDistance, player, new boolean[Game.ROWS][Game.COLUMNS]);
    }

    public Player dfsToCard(Card card, Comparator<Move> cmp, Player player, boolean[][] used) {
        if (player.status == Status.LOST)
            return null;

//        Player last = history[player.getX()][player.getY()];
//        if (last == null || last.timer > player.timer)
        history[player.getX()][player.getY()] = player;
        used[player.getX()][player.getY()] = true;

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
