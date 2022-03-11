public class Backtracking implements Strategy {
    Player player;
    Game game;

    final private Player[][] history = new Player[Game.ROWS][Game.COLUMNS];

    Backtracking(Game game) {
        this.game = game;
        this.player = game.player;
    }

    public Player run(boolean debug) {
        if (debug)
            System.out.println(game.getBoard());
        Player bookPlayer = dfsToCard(Card.BOOK, player, new boolean[Game.ROWS][Game.COLUMNS]);
        if (debug)
            System.out.println(getHistory());

        if (bookPlayer != null) {
            if (debug) System.out.println(bookPlayer);
            Player exitPlayer = dfsToCard(Card.EXIT, bookPlayer, new boolean[Game.ROWS][Game.COLUMNS]);
            if (debug)System.out.println(getHistory());
            if (debug) System.out.println(exitPlayer);
            if (exitPlayer == null)
                return player;
            return exitPlayer;
        }
        return player;
    }


    public Player dfsToCard(Card card, Player p, boolean[][] used) {
        if (p.status == Status.LOST)
            return null;
        history[p.coordinates.x][p.coordinates.y] = p;
        used[p.coordinates.x][p.coordinates.y] = true;
        if (p.getVisibleCardsByPoint(p.coordinates).contains(card)) {
            return p;
        }

        for (Point move : Player.MOVES) {
            Point next = Point.add(p.coordinates, move);
            if (p.ok(next) && !used[next.x][next.y]) {
                Player p2 = dfsToCard(card, game.movePlayer(p, move), used);
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
