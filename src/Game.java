import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    final static int ROWS = 9;
    final static int COLUMNS = 9;

    final private List<List<List<Card>>> board = new ArrayList<>(ROWS);

    //    final private Map<Point, List<Card>> board = new HashMap<>();
    final private int[][] history = new int[ROWS][COLUMNS];
    final private Random random;

    final private Player player;
    final private Point PLAYER;
    public Status status;

    Game(int seed) {
        for (int i = 0; i < ROWS; i++)
            board.add(new ArrayList<>(COLUMNS));

        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMNS; j++)
                board.get(i).add(new ArrayList<>());

        random = new Random(seed);

        Point EXIT = new Point(getRandomNumber(0, ROWS), getRandomNumber(0, COLUMNS));
        board.get(EXIT.x).get(EXIT.y).add(Card.EXIT);

        player = new Player(this, 1, EXIT, new Strategy());
        PLAYER = player.coordinates;
        board.get(PLAYER.x).get(PLAYER.y).add(Card.PLAYER);

        Point BOOK = new Point(getRandomNumber(0, ROWS), getRandomNumber(0, COLUMNS));
        board.get(BOOK.x).get(BOOK.y).add(Card.BOOK);

        addCat(new Point(getRandomNumber(0, ROWS), getRandomNumber(0, COLUMNS)), 2);

        status = Status.STARTED;
    }


    void addCat(Point CAT, int perception) {
        board.get(CAT.x).get(CAT.y).add(Card.CAT);
        for (int i = -perception + 1; i < perception; i++)
            for (int j = -perception + 1; j < perception; j++) {
                board.get(CAT.x + i).get(CAT.y + j).add(Card.SEEN);
            }
    }

    public String run() {
        player.play();
        return status + " in " + player.timer + " moves";
    }

    //
    public List<Card> getCardsByPoint(Point p) {
        return board.get(p.x).get(p.y);
    }


    public static boolean ok(Point point) {
        return 0 <= point.x && point.x < ROWS
                && 0 <= point.y && point.y < COLUMNS;
    }

    private void updateStatus() {
        if (!ok(PLAYER)) {
            status = Status.LOST;
            return;
        }
        List<Card> have = board.get(PLAYER.x).get(PLAYER.y);
        if (have.contains(Card.CAT) || have.contains(Card.SEEN)) {
            status = Status.LOST;
            return;
        }
        if (have.contains(Card.BOOK))
            player.haveBook = true;

        if (have.contains(Card.EXIT) && player.haveBook)
            status = Status.WON;
    }

    public void movePlayer(Point move) {
        if (status == Status.LOST)
            return;
        player.coordinates.add(move);
        history[player.coordinates.x][player.coordinates.y] = ++player.timer;
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

    public int getRandomNumber(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public void print() {
        System.out.print(getBoard());
        System.out.print(getHistory());
        System.out.println(status);
    }
}

