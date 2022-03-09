import java.util.ArrayList;
import java.util.List;

public class Player {
    Point coordinates;
    int timer;
    int perception;
    boolean haveBook = false;
    boolean haveCloak = false;
    final Game game;
    final Strategy strategy;
    final static Point[] MOVES = new Point[9];
    Point EXIT;

    Player(Game game, int perception, Point EXIT, Strategy strategy) {
        for (int x = -1, i = 0; x <= 1; x++)
            for (int y = -1; y <= 1; y++, i++)
                MOVES[i] = new Point(x, y);

        coordinates = new Point(0, 0);
        timer = 0;
        this.game = game;
        this.perception = perception;
        this.strategy = strategy;
        this.strategy.setPlayer(this);
        this.EXIT = EXIT;
    }

    public void useMove(Point move) {
        game.movePlayer(move);
    }

    public Status getStatus() {
        return game.status;
    }


    public void play(boolean debug) {
        strategy.run(debug);
    }

    public List<Card> getVisibleCardsByPoint(Point p) {
        Point diff = Point.add(p, Point.not(coordinates));
        if (Math.abs(diff.x) <= perception && Math.abs(diff.y) <= perception)
            return game.getCardsByPoint(p);
        return new ArrayList<>();
    }


}
