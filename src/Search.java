import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class Search {
    private Game game;
    private Player initialPlayer;
    final private Strategy strategy;
    History history;
    int playerPerception;


    Search(Strategy strategy, int playerPerception) {
        this.strategy = strategy;
        this.playerPerception = playerPerception;
    }

    Search setGame(Game game) {
        this.game = game;
        initialPlayer = new Player(game, playerPerception);
        history = new History();
        return this;
    }


    Player run(boolean debug) {
        if (debug) {
            System.out.println(strategy.getName() + ":");
            game.print();
        }
        long startTime = System.nanoTime();

        findAWayThroughPoints(Game.INF);
        history.solveCats();
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
            System.out.println(strategy.getName() + ":");
            if (player != null) {
                System.out.println(player);
                System.out.println(player.getPath());
                Game.showPath(player.getPath());
            }
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(duration);
        return player;
    }

    public Player findAWayThroughPoints(Point... points) {
        return findAWayThroughPoints(initialPlayer, points);
    }

    public Player findAWayThroughPoints(Player player, Point... points) {
        for (Point v : points) {
            player = strategy.findAWayToPoint(history, player, v);
        }
        return player;
    }
}
