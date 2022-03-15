import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Agent that have sensors and actuators.
 */
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

    /**
     * Constructor that initiates Player instance and updates its status according to the game update function
     *
     * @param game       is environment of the agent
     * @param perception is a perception type of the agent
     */
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
        Point diff = Point.add(p, Point.not(coordinates));
        if (Math.abs(diff.x) <= perception && Math.abs(diff.y) <= perception)
            return game.getCardsByPoint(p);
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
     * Checks if agent can go to the point
     *
     * @param p point agent wants to check
     * @return true if point is safe to walk
     */
    public boolean ok(Point p) {
        return Game.inside(p) && !getVisibleCardsByPoint(p).contains(Card.CAT)
                && (!getVisibleCardsByPoint(p).contains(Card.SEEN) || haveCloak);
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
