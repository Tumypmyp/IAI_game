import java.util.Objects;

public class Point {
    public final int x, y;
    private final int hashCode;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.hashCode = Objects.hash(x, y);
    }

    public static Point add(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Point p = (Point) o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public static Point not(Point a) {
        return new Point(-a.x, -a.y);
    }

    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
