import java.util.*;

public class Game {
    final static int ROWS = 9;
    final static int COLUMNS = 9;

    final private Map<Card, Point> board = new HashMap<>();
    final private int[][] history = new int[ROWS][COLUMNS];
    final private Random random;

    final private Player player;
    public Status status;

    Game(int seed) {
        random = new Random(seed);

        Point EXIT = new Point(getRandomNumber(0, ROWS), getRandomNumber(0, COLUMNS));
        board.put(Card.EXIT, EXIT);

        player = new Player(this, 1, EXIT, new Strategy());
        board.put(Card.PLAYER, player.coordinates);

        Point BOOK = new Point(getRandomNumber(0, ROWS), getRandomNumber(0, COLUMNS));
        board.put(Card.BOOK, BOOK);

        addCat(new Point(getRandomNumber(0, ROWS), getRandomNumber(0, COLUMNS)), 2);

        status = Status.STARTED;
    }

    void addCat(Point CAT, int perception) {
        board.put(Card.CAT, CAT);
        for (int i = -perception + 1; i < perception; i++)
            for (int j = -perception + 1; j < perception; j++) {
                System.out.println(i + " " + j);
                board.put(Card.SEEN_BY_CAT, Point.add(CAT, new Point(i, j)));
            }
    }

    public String run() {
        player.play();
//        return status == Status.LOST ? -1 : player.timer;
        return status + " in " + player.timer + " moves";
    }


    public List<Card> getCardsByPoint(Point p) {
        List<Card> result = new ArrayList<>();
        for (Map.Entry<Card, Point> entry : board.entrySet()) {
            if (p.equals(entry.getValue()))
                result.add(entry.getKey());
        }
        return result;
    }

    public static boolean ok(Point point) {
        return 0 <= point.x && point.x < ROWS
                && 0 <= point.y && point.y < COLUMNS;
    }

    private void updateStatus() {
        if (!ok(player.coordinates)) {
            status = Status.LOST;
            return;
        }
        if (player.coordinates.equals(board.get(Card.CAT))) {
            status = Status.LOST;
            return;
        }
        if (player.coordinates.equals(board.get(Card.BOOK)))
            player.haveBook = true;

        if (player.coordinates.equals(board.get(Card.EXIT)) && player.haveBook)
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
                result.append(getCardsByPoint(new Point(i, j)).toString());
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

