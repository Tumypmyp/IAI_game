import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    boolean haveBook;
    boolean haveCloak;
    final int timer;
    final Point coordinates;
    final int perception;
    final static List<Point> MOVES = new ArrayList<>();

    Player parent;
    Game game;
    Status status;

    Player(Game game, int perception) {
        for (int x = -1, i = 0; x <= 1; x++)
            for (int y = -1; y <= 1; y++, i++)
                MOVES.add(new Point(x, y));

        coordinates = new Point(0, 0);
        timer = 0;
        this.perception = perception;
        this.game = game;
        haveBook = false;
        haveCloak = false;
        status = Status.STARTED;
        game.updateStatus(this);
    }

    Player(Game game, Player p, Point move) {
        this.coordinates = Point.add(p.coordinates, move);
        this.timer = p.timer + 1;
        this.perception = p.perception;
        this.haveBook = p.haveBook;
        this.haveCloak = p.haveCloak;
        this.status = p.status;
        this.game = game;
        this.parent = p;
        game.updateStatus(this);
    }

    public List<Card> getVisibleCardsByPoint(Point p) {
        Point diff = Point.add(p, Point.not(coordinates));
        if (Math.abs(diff.x) <= perception && Math.abs(diff.y) <= perception)
            return game.getCardsByPoint(p);
        return new ArrayList<>();
    }

    public int getX() {
        return coordinates.x;
    }

    public int getY() {
        return coordinates.y;
    }

    public boolean ok(Point p) {
        return Game.inside(p) && !getVisibleCardsByPoint(p).contains(Card.CAT)
                && (!getVisibleCardsByPoint(p).contains(Card.SEEN) || haveCloak);
    }

    public String toString() {
        return coordinates + " " + status + " time:" + timer + " book:" + haveBook + " cloak:" + haveCloak;
    }
    public List<Point> getPath() {
        List<Point> path = new ArrayList<>();
        Player p = this;
        path.add(p.coordinates);
        while (p.parent != null) {
            p = p.parent;
            path.add(p.coordinates);
        }
        Collections.reverse(path);
        return path;
    }


}
