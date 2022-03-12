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

//    Game (Point cat1, Point cat2, Point book, Point cloak, Point exit, Player player) throws Exception {
//        this.random = null;
////        this.player = player;
//        this.BOOK = book;
//        this.EXIT = exit;
//        this.CLOAK = cloak;
////        player.game = this;
//        player.EXIT = exit;
////        PLAYER = player.coordinates;
//
//        getCardsByPoint(book).add(Card.BOOK);
//        getCardsByPoint(cloak).add(Card.CLOAK);
//        getCardsByPoint(cat1).add(Card.CAT);
//        getCardsByPoint(cat2).add(Card.CAT);
//
//        if (!validate())
//            throw new Exception("bad input parameters");
//    }

    boolean validate() {
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

        while (true) {
            board.clear();
            for (int i = 0; i < ROWS; i++) {
                board.add(new ArrayList<>());
                for (int j = 0; j < COLUMNS; j++)
                    board.get(i).add(new ArrayList<>());
            }

            addCat(2);
            addCat(3);
            BOOK = addCard(Card.BOOK);
            CLOAK = addCard(Card.CLOAK);
            EXIT = addCard(Card.EXIT);
            if (validate())
                break;
        }

        this.initialPlayer = new Player(this, perception);
    }


    void addCat(int perception) {
        Point CAT = addCard(Card.CAT);
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

