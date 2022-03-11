public class Point {
    public int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point add(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }

    public void add(Point a) {
        x += a.x;
        y += a.y;
    }

    public static Point not(Point a) {
        return new Point(-a.x, -a.y);
    }


    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
