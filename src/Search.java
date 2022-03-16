import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Search {
    Strategy strategy;
    String name;
    Game game;
//    Point BOOK;
//    Point CLOAK;
    final Player[][] history;

    Search(String s) {
        name = s;
        if (s.toLowerCase().charAt(0) == 'a')
            this.strategy = new AStar();
        else
            this.strategy = new Backtracking();
        this.history = strategy.getHistory();
//        this.BOOK = strategy.getBOOK();
//        this.CLOAK = strategy.getCLOAK();
    }

    public Search setGame(Game game) {
        this.game = game;
        strategy.setGame(game);
        return this;
    }


    Player run(boolean debug) {
        Player player = findWayToPoint(new Point(9, 9));

//        if (debug) {
//            System.out.println("Backtracking:");
//            System.out.println(game.getBoard());
//            System.out.println(getHistory());
//        }

//        optimization
//        if (strategy.getCLOAK() == null) {
//            return strategy.findWayToPoint(game.EXIT, strategy.findWayToPoint(strategy.getBOOK()));
//        }
        strategy.findWayToPoint(new Point(9, 9), findWayToPoint(strategy.getCLOAK()));
        Player p1 = strategy.findWayToPoint(game.EXIT, findWayToPoint(strategy.getBOOK()));
        Player p2 = strategy.findWayToPoint(game.EXIT, strategy.findWayToPoint(strategy.getCLOAK(), findWayToPoint(strategy.getBOOK())));
        Player p3 = strategy.findWayToPoint(game.EXIT, strategy.findWayToPoint(strategy.getBOOK(), findWayToPoint(strategy.getCLOAK())));


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


    public Player findWayToPoint(Point destination){
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
