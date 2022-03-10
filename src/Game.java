import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    final static int ROWS = 9;
    final static int COLUMNS = 9;

    final private List<List<List<Card>>> board = new ArrayList<>();
    final private int[][] history = new int[ROWS][COLUMNS];
    final private Random random;

    final public Player player;

    private Point PLAYER;
    private Point BOOK;
    private Point EXIT;
    private Point CLOAK;
    public Status status;

    Game (Point cat1, Point cat2, Point book, Point cloak, Point exit, Player player) throws Exception {
        this.random = null;
        this.player = player;
        this.BOOK = book;
        this.EXIT = exit;
        this.CLOAK = cloak;
        player.game = this;
        player.EXIT = exit;
        PLAYER = player.coordinates;

        getCardsByPoint(book).add(Card.BOOK);
        getCardsByPoint(cloak).add(Card.CLOAK);
        getCardsByPoint(cat1).add(Card.CAT);
        getCardsByPoint(cat2).add(Card.CAT);

        if (!validate())
            throw new Exception("bad input parameters");
    }

    boolean validate(){
        if (getCardsByPoint(EXIT).contains(Card.SEEN)
            || getCardsByPoint(EXIT).contains(Card.BOOK))
            return false;
        if (getCardsByPoint(BOOK).contains(Card.SEEN))
            return false;
        if (getCardsByPoint(CLOAK).contains(Card.SEEN))
            return false;
        return true;
    }


    Game(int seed, Player player) {
        random = new Random(seed);

        while (true) {
            board.clear();
            for (int i = 0; i < ROWS; i++) {
                board.add(new ArrayList<>());
                for (int j = 0; j < COLUMNS; j++)
                    board.get(i).add(new ArrayList<>());
            }

            addCat(2);
            addCat(3);
            addBook();
            addCloak();
            addExit();
            if (validate())
                break;
        }
        this.player = player;
        player.game = this;
        player.EXIT = EXIT;
        PLAYER = player.coordinates;

        getCardsByPoint(PLAYER).add(Card.PLAYER);

        status = Status.STARTED;
        updateStatus();
        if (!validate()) {
            System.out.println("????");
        }
    }


    void addCat(int perception) {
        Point CAT = makeRandomPoint();
        getCardsByPoint(CAT).add(Card.CAT);
        for (int i = -perception + 1; i < perception; i++)
            for (int j = -perception + 1; j < perception; j++) {
                Point SEEN = Point.add(CAT, new Point(i, j));
                if (inside(SEEN))
                    getCardsByPoint(SEEN).add(Card.SEEN);
            }
    }


    void addBook() {
        // {0, 0}?
        BOOK = makeRandomPoint();
//        while (getCardsByPoint(BOOK).contains(Card.SEEN)) {
//            BOOK = makeRandomPoint();
//        }
        getCardsByPoint(BOOK).add(Card.BOOK);
    }

    void addExit() {
        EXIT = makeRandomPoint();
//        while (getCardsByPoint(EXIT).contains(Card.SEEN) || getCardsByPoint(EXIT).contains(Card.BOOK)) {
//            EXIT = makeRandomPoint();
//        }
        getCardsByPoint(EXIT).add(Card.EXIT);
    }

    void addCloak() {
        // {0, 0}?
        CLOAK = makeRandomPoint();
//        while (getCardsByPoint(CLOAK).contains(Card.SEEN)) {
//            CLOAK = makeRandomPoint();
//        }
        getCardsByPoint(CLOAK).add(Card.CLOAK);
    }

    Point makeRandomPoint() {
        return new Point(getRandomNumber(0, ROWS), getRandomNumber(0, COLUMNS));
    }

    public int getRandomNumber(int min, int max) {
        return random.nextInt(max - min) + min;
    }
    public String run(boolean debug) {
        player.play(debug);
        return status + " in " + player.timer + " moves";
    }

    public List<Card> getCardsByPoint(Point p) {
        return board.get(p.x).get(p.y);
    }


    public static boolean inside(Point point) {
        return 0 <= point.x && point.x < ROWS
                && 0 <= point.y && point.y < COLUMNS;
    }

    private void updateStatus() {
        if (!inside(PLAYER)) {
            status = Status.LOST;
            return;
        }
        List<Card> cards = getCardsByPoint(PLAYER);
        if (cards.contains(Card.CAT) || (!player.haveCloak && cards.contains(Card.SEEN))) {
            status = Status.LOST;
            return;
        }
        if (cards.contains(Card.BOOK))
            player.haveBook = true;
        if (cards.contains(Card.CLOAK))
            player.haveCloak = true;
        if (cards.contains(Card.EXIT) && player.haveBook)
            status = Status.WON;
    }

    public void movePlayer(Point move) {
        if (status == Status.LOST)
            return;
        PLAYER.add(move);
        history[PLAYER.x][PLAYER.y] = ++player.timer;
        updateStatus();
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

    public String getHistory() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                result.append(history[i][j]).append("\t");
            }
            result.append("\n");
        }
        return result.toString();
    }


    public void print() {
        System.out.print(getBoard());
        System.out.print(getHistory());
        System.out.println(status);
    }
}

