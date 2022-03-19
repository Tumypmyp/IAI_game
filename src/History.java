import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class History {

    Point BOOK;
    Point CLOAK;
    //    Point CAT;
//    Point FILCH;
    Set<Point> cats;
    final Player[][] players = new Player[Game.ROWS][Game.COLUMNS];

    final boolean[][] notCat = new boolean[Game.ROWS][Game.COLUMNS];
    final boolean[][] seen = new boolean[Game.ROWS][Game.COLUMNS];
    final int[][] type = new int[Game.ROWS][Game.COLUMNS];


    public void add(Player player) {
        players[player.getX()][player.getY()] = player;
        if (!player.haveCloak && player.status != Status.LOST)
            addNotCat(player.coordinates);
        if (player.getVisibleCardsByPoint(player.coordinates).contains(Card.BOOK))
            BOOK = player.coordinates;
        if (player.getVisibleCardsByPoint(player.coordinates).contains(Card.CLOAK))
            CLOAK = player.coordinates;

        for (Point diff : player.perception) {
            Point p = Point.add(player.coordinates, diff);
            if (Game.inside(p)) {
                if (!player.getVisibleCardsByPoint(p).contains(Card.SEEN)) {
                    addNotCat(p);
                } else {
                    seen[p.x][p.y] = true;
                    type[p.x][p.y] = 2;
                }
            }

        }
    }

    void addNotCat(Point p) {
        type[p.x][p.y] = 1;
        for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++)
                if (Game.inside(new Point(p.x + i, p.y + j)))
                    notCat[p.x + i][p.y + j] = true;
    }

    /**
     * Checks if agent can go to the point
     *
     * @return true if point is safe to walk
     */
    public boolean ok(Move move) {
        if (!Game.inside(move.coordinates))
            return false;
        if (move.player.haveCloak && cats != null) {
            return !cats.contains(move.coordinates);
        }


        Point p = move.coordinates;
        if (type[p.x][p.y] == 1)
            return true;
//        return false;
        for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++)
                if (Game.inside(new Point(p.x + i, p.y + j)) && !notCat[p.x + i][p.y + j])
                    return false;
//        return Game.inside(p) && notCat[p.x][p.y];//!getVisibleCardsByPoint(p).contains(Card.CAT)
//                && (!getVisibleCardsByPoint(p).contains(Card.SEEN) || haveCloak);
        return true;
    }

    List<Move> getMoves(Player player) {
        List<Move> res = new ArrayList<>();
        for (Point p : Player.MOVES) {
            Move move = new Move(player, p);
            if (ok(move))
                res.add(move);
        }
        return res;
    }

    class Pair<L, R> {
        private L l;
        private R r;

        public Pair(L l, R r) {
            this.l = l;
            this.r = r;
        }
    }

    public int solveCats() {
        List<Pair<Point, Point>> variants = new ArrayList<>();
        for (int i = 0; i < Game.ROWS; i++)
            for (int j = 0; j < Game.COLUMNS; j++) {
                Point FILCH = new Point(i, j);
                if (possible(FILCH, 3)) {

                    for (int i1 = 0; i1 < Game.ROWS; i1++)
                        for (int j1 = 0; j1 < Game.COLUMNS; j1++) {
                            Point CAT = new Point(i1, j1);
                            if (possible(CAT, 2)) {
                                final boolean[][] twos = new boolean[Game.ROWS][Game.COLUMNS];
                                twos(twos);
                                twos(FILCH, 3, twos);
                                twos(CAT, 2, twos);
                                boolean have = false;
                                for (boolean[] ar : twos)
                                    for (boolean value : ar) {
                                        if (value) {
                                            have = true;
                                            break;
                                        }
                                    }
                                if (!have)
                                    variants.add(new Pair<>(FILCH, CAT));
                            }
                        }
                }
            }

        cats = new HashSet<>();
        for (Pair<Point, Point> p : variants) {
            cats.add(p.l);
            cats.add(p.r);
        }
        return 0;
    }

    void twos(boolean[][] a) {
        for (int i = 0; i < Game.ROWS; i++)
            for (int j = 0; j < Game.COLUMNS; j++) {
                a[i][j] = type[i][j] == 2;
            }
    }

    void twos(Point p, int k, boolean[][] a) {
        int ans = 0;
        for (int i = p.x - k + 1; i < p.x + k; i++)
            for (int j = p.y - k + 1; j < p.y + k; j++) {
                if (Game.inside(new Point(i, j)))
                    a[i][j] = false;
            }
    }

    boolean possible(Point p, int k) {
        for (int i = p.x - k + 1; i < p.x + k; i++)
            for (int j = p.y - k + 1; j < p.y + k; j++) {
                if (Game.inside(new Point(i, j)) && type[i][j] == 1)
                    return false;

            }
        return true;
    }

}
