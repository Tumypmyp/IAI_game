import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class Search {
    final private Game game;
    final private Player initialPlayer;

    final private String name;
    final private Strategy strategy;
    final History history = new History();


    Search(Game game, String name, int perception) {
        if (name.toLowerCase().charAt(0) == 'a') {
            this.strategy = new AStar(this);
        } else {
            this.strategy = new Backtracking(this);
        }

        this.name = name;
        this.game = game;
        initialPlayer = new Player(game, perception);
    }

    Player run(boolean debug) {
        if (debug) {
            System.out.println(name + ":");
            game.print();
        }
        findAWayThroughPoints(Game.INF);
        history.solveCats();
//        optimization
//        if (CLOAK == null) {
//            return findWayThrowPoints(BOOK, EXIT);
//        }
        findAWayThroughPoints(history.CLOAK, Game.INF);
        history.solveCats();

        List<Player> list = new ArrayList<>();

        list.add(findAWayThroughPoints(history.BOOK, game.EXIT));
        list.add(findAWayThroughPoints(history.BOOK, history.CLOAK, game.EXIT));
        list.add(findAWayThroughPoints(history.CLOAK, history.BOOK, game.EXIT));

        list.removeIf(Objects::isNull);
        list.removeIf(p -> p.status != Status.WON);

        list.sort(Comparator.comparingInt((Player p0) -> p0.timer));
        Player player;
        if (!list.isEmpty())
            player = list.get(0);
        else
            player = initialPlayer;

        if (debug) {
            System.out.println(name + ":");
            game.print();
            if (player != null) {
                System.out.println(player);
                System.out.println(player.getPath());
                Game.showPath(player.getPath());
            }
        }
        return player;
    }

    public Player findAWayThroughPoints(Point... points) {
        return findAWayThroughPoints(initialPlayer, points);
    }

    public Player findAWayThroughPoints(Player player, Point... points) {
        for (Point v : points) {
            player = strategy.findAWayToPoint(player, v);
        }
        return player;
    }
}
