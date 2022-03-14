import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStar implements Strategy{
    Game game;
    final private Player[][] history = new Player[Game.ROWS][Game.COLUMNS];

    @Override
    public Player run(boolean debug) {


//        Player p = findWayToPoint(game.EXIT, game.initialPlayer, new boolean[Game.ROWS][Game.COLUMNS]);
        Player p = findWayToPoint(new Point (0, 7), game.initialPlayer, new boolean[Game.ROWS][Game.COLUMNS]);

        if (debug) System.out.println(game.getBoard());
        if (debug) System.out.println(getHistory());
        if (debug) System.out.println(p);
        return p;
    }

    Player findWayToPoint(Point finish, Player player, boolean[][] used) {
        Comparator<Move> byDistance = Comparator.comparingInt((Move m) -> m.getDistanceTo(finish));
        Queue<Move> q = new PriorityQueue<>(1, byDistance);
        q.add(new Move(player, new Point(0, 0)));

        while(!q.isEmpty()) {
            Move current = q.poll();
            Player p = current.execute();
            history[p.coordinates.x][p.coordinates.y] = p;
            if (p.coordinates.equals(finish))
                return p;
            for (Point move : Player.MOVES) {
                Move m = new Move(p, move);
//                Point next = Point.add(p.coordinates, move);
                if (p.ok(m.coordinates) && !used[m.coordinates.x][m.coordinates.y]) {
                    used[m.coordinates.x][m.coordinates.y] = true;
                    q.add(m);
//                    Player p2 = dfsToCard(card, game.movePlayer(p, move), used);
//                    if (p2 != null)
//                        return p2;
                }
            }
        }
        return game.initialPlayer;
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
                Player p = history[i][j];
                result.append(p == null ? "-1" : p.timer).append("\t");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
