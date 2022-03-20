import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Search {
    private Game game;
    private Player initialPlayer;
    private History history;
    public Strategy strategy;
    int playerPerception;

    /**
     * Constructor function initiates new instance of a search algorithm
     *
     * @param strategy         the strategy that used to find the solution
     * @param playerPerception perception that player has
     */
    Search(Strategy strategy, int playerPerception) {
        this.strategy = strategy;
        this.playerPerception = playerPerception;
    }

    /**
     * Setting the game instance for searching algorithm to solve
     *
     * @param game game to solve
     * @return this instance
     */
    Search setGame(Game game) {
        this.game = game;
        initialPlayer = new Player(game, playerPerception);
        history = new History();
        return this;
    }

    /**
     * Runs searching algorithm to find a solution for the game
     *
     * @param debug bool that indicate if there should be a debugging output
     * @return player that won
     */
    Player run(boolean debug) {
        if (debug) {
            System.out.println("\n" + strategy.getName() + ":");
            game.print();
        }
        long startTime = System.nanoTime();

        findAWayThroughPoints(Game.INF);
        history.solveCats();
        for (int i = 0; i < 3; i++) {
            findAWayThroughPoints(history.CLOAK, Game.INF);
            history.solveCats();
        }

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
            System.out.println(player);
            System.out.println(player.getPath());
            Game.showPath(player.getPath());

            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            System.out.println("Time taken: " + duration + "ms");
        }
        return player;
    }

    /**
     * Find the shortest way from initial player through some points
     *
     * @param points the sequence of destinations for the player
     * @return player that found a way through the sequence
     */
    public Player findAWayThroughPoints(Point... points) {
        return findAWayThroughPoints(initialPlayer, points);
    }

    /**
     * Find the shortest way for the player through some points
     *
     * @param player the starting player
     * @param points the sequence of destinations for the player
     * @return player that found a way through the sequence
     */
    public Player findAWayThroughPoints(Player player, Point... points) {
        for (Point v : points) {
            player = strategy.findAWayToPoint(history, player, v);
        }
        return player;
    }
}
