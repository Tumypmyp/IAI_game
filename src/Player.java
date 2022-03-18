import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Agent that have sensors and actuators.
 */
public class Player {
    boolean haveBook;
    boolean haveCloak;
    final int timer;
    final Point coordinates;
    final Point[] perception;
    final static Point[][] perceptionPoints = {
            {
                    new Point(-1, -1), new Point(-1, 0), new Point(-1, 1),
                    new Point(0, -1), new Point(0, 0), new Point(0, 1),
                    new Point(1, -1), new Point(1, 0), new Point(1, 1)},
            {
                    new Point(-2, -1), new Point(-2, 0), new Point(-2, 1),
                    new Point(2, -1), new Point(2, 0), new Point(2, 1),
                    new Point(-1, -2), new Point(0, -2), new Point(1, -2),
                    new Point(1, 2), new Point(0, 2), new Point(1, 2),
            }
    };

    final static Point[] MOVES = {
            new Point(-1, -1), new Point(-1, 0), new Point(-1, 1),
            new Point(0, -1), new Point(0, 0), new Point(0, 1),
            new Point(1, -1), new Point(1, 0), new Point(1, 1)
    };


    Player parent;
    Game game;
    Status status;

    /**
     * Constructor that initiates Player instance and updates its status according to the game update function
     *
     * @param game is environment of the agent
     */
    Player(Game game, int perceptionType) {
        coordinates = new Point(0, 0);
        timer = 0;
        this.perception = perceptionPoints[perceptionType - 1];
        this.game = game;
        haveBook = false;
        haveCloak = false;
        status = Status.STARTED;
        game.updateStatus(this);
    }

    /**
     * Constructor that initiates Player from a parent using the new move and updates its status according to the game update function
     * Used as actuator of an agent: moving the agent forward
     *
     * @param parent is a parent instance
     * @param move   the direction we move the parent
     */
    Player(Player parent, Point move) {
        this.coordinates = Point.add(parent.coordinates, move);
        this.timer = parent.timer + 1;
        this.perception = parent.perception;
        this.haveBook = parent.haveBook;
        this.haveCloak = parent.haveCloak;
        this.status = parent.status;
        this.game = parent.game;
        this.parent = parent;
        game.updateStatus(this);
    }

    /**
     * Sensor of an agent, gets visible cards that placed in the point
     *
     * @param p the point agent looking at
     * @return list of cards he saw
     */
    public List<Card> getVisibleCardsByPoint(Point p) {
        if (!Game.inside(p))
            return new ArrayList<>();
        Point diff = Point.add(p, Point.not(coordinates));
        if (diff.equals(new Point(0, 0)))
            return game.getCardsByPoint(p);
        if (Arrays.asList(perception).contains(diff))
            return game.getCardsByPoint(p).stream().filter(Predicate.isEqual(Card.SEEN)).collect(Collectors.toList());
        return new ArrayList<>();
    }

    /**
     * @return coordinate x of an agent
     */
    public int getX() {
        return coordinates.x;
    }

    /**
     * @return coordinate y of an agent
     */
    public int getY() {
        return coordinates.y;
    }


    /**
     * @return the string representation af the agent state
     */
    public String toString() {
        return coordinates + " " + status + " time:" + timer + " book:" + haveBook + " cloak:" + haveCloak;
    }

    /**
     * @return the path the agent made from the beginning
     */
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
