import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The environment where algorithms will search for shortest path.
 * The environment never changes. It only responds to actor calls.
 */
public class Game {
    final static int ROWS = 9;
    final static int COLUMNS = 9;

    final private List<List<List<Card>>> board = new ArrayList<>();
    final private Random random;
    final public Player initialPlayer;

    private Point BOOK;
    public Point EXIT;
    private Point CLOAK;

    /**
     * Constructor function initiates new instance of manually entered game
     *
     * @param points     is a 6 points array, places of: actor, Argus Filch, Mrs Norris,
     *                   the book, the invisibility cloak and an exit door
     * @param perception is a type of perception zone for actor
     * @throws Exception when input is incorrect
     */
    Game(Point[] points, int perception) throws Exception {
        for (int i = 0; i < ROWS; i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < COLUMNS; j++)
                board.get(i).add(new ArrayList<>());
        }

        this.random = null;
        this.BOOK = points[3];
        this.CLOAK = points[4];
        this.EXIT = points[5];

        getCardsByPoint(BOOK).add(Card.BOOK);
        getCardsByPoint(CLOAK).add(Card.CLOAK);
        getCardsByPoint(EXIT).add(Card.EXIT);

        getCardsByPoint(points[1]).add(Card.CAT);
        addCatPerception(points[1], 3);

        getCardsByPoint(points[2]).add(Card.CAT);
        addCatPerception(points[2], 2);

        this.initialPlayer = new Player(this, perception);

        if (!valid() || points[0].x != 0 || points[0].y != 0 || points.length != 6)
            throw new Exception("bad input parameters");
    }

    /**
     * Checks board generated for validness
     *
     * @return true if board valid
     */
    boolean valid() {
        if (board.isEmpty() || EXIT == null || BOOK == null || CLOAK == null)
            return false;
        if (getCardsByPoint(EXIT).contains(Card.CAT)
                || getCardsByPoint(EXIT).contains(Card.BOOK))
            return false;
        if (getCardsByPoint(BOOK).contains(Card.SEEN))
            return false;
        if (getCardsByPoint(CLOAK).contains(Card.SEEN))
            return false;
        return true;
    }

    /**
     * @param seed       is a value for initializing Random
     * @param perception is a type of perception zone for actor
     */
    Game(int seed, int perception) {
        random = new Random(seed);

        while (!valid()) {
            board.clear();
            for (int i = 0; i < ROWS; i++) {
                board.add(new ArrayList<>());
                for (int j = 0; j < COLUMNS; j++)
                    board.get(i).add(new ArrayList<>());
            }

            addCatPerception(addCard(Card.CAT), 2);
            addCatPerception(addCard(Card.CAT), 3);

            BOOK = addCard(Card.BOOK);
            CLOAK = addCard(Card.CLOAK);
            EXIT = addCard(Card.EXIT);
        }

        this.initialPlayer = new Player(this, perception);
    }

    /**
     * Places SEEN cards in perception zone of an Inspector
     *
     * @param CAT        is a point that indicates place of an Inspector
     * @param perception perception of an Inspector (2 - for Mrs Norris, 3 - for Argus Filch)
     */
    void addCatPerception(Point CAT, int perception) {
        for (int i = -perception + 1; i < perception; i++)
            for (int j = -perception + 1; j < perception; j++) {
                Point SEEN = Point.add(CAT, new Point(i, j));
                if (inside(SEEN) && !getCardsByPoint(SEEN).contains(Card.SEEN))
                    getCardsByPoint(SEEN).add(Card.SEEN);
            }
    }

    /**
     * Places a card in random place of the board
     *
     * @param card is a card type
     * @return the card place
     */
    Point addCard(Card card) {
        Point CARD = makeRandomPoint();
        getCardsByPoint(CARD).add(card);
        return CARD;
    }

    /**
     * Makes random point in the board
     *
     * @return random point
     */
    Point makeRandomPoint() {
        return new Point(getRandomNumber(0, ROWS), getRandomNumber(0, COLUMNS));
    }

    /**
     * Makes random int in interval
     *
     * @param min minimum value
     * @param max maximum value (not included)
     * @return random int
     */
    public int getRandomNumber(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    /**
     * Finds cards associated with a point
     *
     * @param p is the point
     * @return associated with a point
     */
    public List<Card> getCardsByPoint(Point p) {
        return board.get(p.x).get(p.y);
    }

    /**
     * Checks if a point is inside the board
     *
     * @param point that will be checked
     * @return true if point is inside the board
     */
    public static boolean inside(Point point) {
        return 0 <= point.x && point.x < ROWS
                && 0 <= point.y && point.y < COLUMNS;
    }

    /**
     * Updated the actor status and fields according to cards on a board in same place as he.
     * Player could find a book, a cloak, or LOSE the game if he is seen by a cat
     *
     * @param player is actor whose fields to be updated
     */
    public void updateStatus(Player player) {
        if (!inside(player.coordinates)) {
            player.status = Status.LOST;
            return;
        }
        List<Card> cards = getCardsByPoint(player.coordinates);
        if (cards.contains(Card.CAT) || (!player.haveCloak && cards.contains(Card.SEEN))) {
            player.status = Status.LOST;
            return;
        }
        if (cards.contains(Card.BOOK))
            player.haveBook = true;
        if (cards.contains(Card.CLOAK))
            player.haveCloak = true;
        if (cards.contains(Card.EXIT) && player.haveBook)
            player.status = Status.WON;
    }

    /**
     * Makes string representation of a board
     *
     * @return string representation of a board
     */
    public String getBoard() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                result.append(board.get(i).get(j).toString());
            }
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Prints a string representation of the path
     *
     * @param path list of points that represent the path
     */
    public static void showPath(List<Point> path) {
        int[][] history = new int[Game.ROWS][Game.COLUMNS];
        int timer = 1;
        for (Point p : path) {
            history[p.x][p.y] = timer++;
        }
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++)
                System.out.print(history[i][j] == 0 ? ".\t" : history[i][j] + "\t");
            System.out.println();
        }

    }

}

