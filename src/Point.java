import java.util.Objects;

public class Point {
    int x, y;
    private final int hashCode;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.hashCode = Objects.hash(x, y);
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Point p2 = (Point) o;
        return x == p2.x && y == p2.y;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }
}
