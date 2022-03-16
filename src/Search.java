import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Search {
    final private String name;
    final private Strategy strategy;
    final private Player[][] history = new Player[Game.ROWS][Game.COLUMNS];
    Game game;
    Point BOOK;
    Point CLOAK;
    Point EXIT;

    Search(Game game, String name) {
        if (name.toLowerCase().charAt(0) == 'a') {
            this.strategy = new AStar(this, history);
        } else {
            this.strategy = new Backtracking(this, history);
        }

        this.name = name;
        this.game = game;
        this.EXIT = game.EXIT;
    }

    Player run(boolean debug) {
        Player player = findWayThrowPoints(new Point(9, 9));

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

        List<Player> list = new ArrayList<>();

        list.add(findWayThrowPoints(BOOK, EXIT));
        list.add(findWayThrowPoints(BOOK, CLOAK, EXIT));
        list.add(findWayThrowPoints(CLOAK, BOOK, EXIT));

        list.removeIf(Objects::isNull);
        list.removeIf(p -> p.status != Status.WON);

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
            player = strategy.findWayToPoint(player, v);
        }
        return player;
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
