import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Search {
    Strategy strategy;
    String name;
    final Player[][] history;
    Game game;
    Point BOOK;
    Point CLOAK;
    final Point EXIT;

    Search(Game game, String s) {
        name = s;
        if (s.toLowerCase().charAt(0) == 'a')
            this.strategy = new AStar(game, this);
        else
            this.strategy = new Backtracking(game, this);

        this.game = game;
        this.history = strategy.getHistory();
        this.EXIT = game.EXIT;
    }

    Player run(boolean debug) {
        Player player = findWayToPoint(new Point(9, 9));

//        if (debug) {
//            System.out.println("Backtracking:");
//            System.out.println(game.getBoard());
//            System.out.println(getHistory());
//        }

//        optimization
//        if (CLOAK == null) {
//            return findWayThrowPoints(BOOK, EXIT);
//        }
        findWayThrowPoints(CLOAK, new Point(9, 9));

        Player p1 = findWayThrowPoints(BOOK, EXIT);
        Player p2 = findWayThrowPoints(BOOK, CLOAK, EXIT);
        Player p3 = findWayThrowPoints(CLOAK, BOOK, EXIT);

        List<Player> list = new ArrayList<>();
        if (p1 != null)
            list.add(p1);
        if (p2 != null)
            list.add(p2);
        if (p3 != null)
            list.add(p3);
        list.sort(Comparator.comparingInt((Player p0) -> p0.timer));

        if (list.isEmpty())
            return game.initialPlayer;
        player = list.get(0);

        if (debug) {
            System.out.println(name + ":");
            System.out.println(game.getBoard());
            if (player != null) {
                System.out.println(player);
                System.out.println(player.getPath());
                Game.showPath(player.getPath());
            }
        }
        return player;
    }

    public Player findWayThrowPoints(Point... points) {
        return findWayThrowPoints(game.initialPlayer, points);
    }

    public Player findWayThrowPoints(Player player, Point... points) {
        for (Point v : points) {
            player = strategy.findWayToPoint(v, player);
        }
        return player;
    }

    public Player findWayToPoint(Point destination) {
        return strategy.findWayToPoint(destination, game.initialPlayer);
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
