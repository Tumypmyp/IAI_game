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

//        if (debug) {
//            System.out.println(name + ":");
//            System.out.println(game.getBoard());
//            System.out.println(getHistory());
//            System.out.flush();
//        }
        Player player = findWayThrowPoints(new Point(9, 9));
        history.solveCats();
//        if (initialPlayer.status != Status.LOST) {
//
//            System.out.println(name + ":");
//            System.out.println(game.getBoard());
//            System.out.println(getHistory());
//            System.out.println( history.solveCats());
//        }

//        optimization
//        if (CLOAK == null) {
//            return findWayThrowPoints(BOOK, EXIT);
//        }
        findWayThrowPoints(history.CLOAK, new Point(9, 9));
        history.solveCats();

        List<Player> list = new ArrayList<>();

        list.add(findWayThrowPoints(history.BOOK, game.EXIT));
        list.add(findWayThrowPoints(history.BOOK, history.CLOAK, game.EXIT));
        list.add(findWayThrowPoints(history.CLOAK, history.BOOK, game.EXIT));

        list.removeIf(Objects::isNull);
        list.removeIf(p -> p.status != Status.WON);

        list.sort(Comparator.comparingInt((Player p0) -> p0.timer));
        if (list.isEmpty())
            return initialPlayer;
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
        return findWayThrowPoints(initialPlayer, points);
    }

    public Player findWayThrowPoints(Player player, Point... points) {
        for (Point v : points) {
            player = strategy.findWayToPoint(player, v);
        }
        return player;
    }


    List<Move> getMoves(Player player) {
        return history.getMoves(player);
    }

    public String getHistory() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < Game.ROWS; i++) {
            for (int j = 0; j < Game.COLUMNS; j++) {
                Player p = history.players[i][j];
                result.append(p == null ? "-1" : p.timer).append("\t");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
