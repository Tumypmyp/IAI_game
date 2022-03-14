import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    final static int ROWS = 9;
    final static int COLUMNS = 9;

    final private List<List<List<Card>>> board = new ArrayList<>();
    final private Random random;

    final public Player initialPlayer;

    private Point BOOK;
    public Point EXIT;
    private Point CLOAK;

    Game (Point[] points, int perception) throws Exception {
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

        if (!valid() || points[0].x != 0 || points[0].y != 0)
            throw new Exception("bad input parameters");
    }

    boolean valid() {
        if (board.isEmpty() || EXIT == null || BOOK == null || CLOAK == null) {
            return false;
        }
//        System.out.println(getBoard());

        if (getCardsByPoint(EXIT).contains(Card.SEEN)
                || getCardsByPoint(EXIT).contains(Card.BOOK))
            return false;
        if (getCardsByPoint(BOOK).contains(Card.SEEN))
            return false;
        if (getCardsByPoint(CLOAK).contains(Card.SEEN))
            return false;
        return true;
    }


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


    void addCatPerception(Point CAT, int perception) {
        for (int i = -perception + 1; i < perception; i++)
            for (int j = -perception + 1; j < perception; j++) {
                Point SEEN = Point.add(CAT, new Point(i, j));
                if (inside(SEEN) && !getCardsByPoint(SEEN).contains(Card.SEEN))
                    getCardsByPoint(SEEN).add(Card.SEEN);
            }
    }

    Point addCard(Card card) {
        Point CARD = makeRandomPoint();
        getCardsByPoint(CARD).add(card);
        return CARD;
    }

    Point makeRandomPoint() {
        return new Point(getRandomNumber(0, ROWS), getRandomNumber(0, COLUMNS));
    }

    public int getRandomNumber(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public List<Card> getCardsByPoint(Point p) {
        return board.get(p.x).get(p.y);
    }

    public static boolean inside(Point point) {
        return 0 <= point.x && point.x < ROWS
                && 0 <= point.y && point.y < COLUMNS;
    }

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

    public Player movePlayer(Player player, Point move) {
        return new Player(this, player, move);
    }

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


}

