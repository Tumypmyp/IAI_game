import java.util.Objects;

public class Point {
    public final int x, y;
    private final int hashCode;

    /**
     * Constructor initiates an instance of immutable Cartesian coordinates
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.hashCode = Objects.hash(x, y);
    }

    /**
     * Computes scalar addition of two vectors
     *
     * @param a vector
     * @param b vector
     * @return scalar sum
     */
    public static Point add(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }

    /**
     * Checks for equality
     *
     * @param o object to check
     * @return true if this object is equal to object specified
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Point p = (Point) o;
        return x == p.x && y == p.y;
    }

    /**
     * @return hashcode of the vector
     */
    @Override
    public int hashCode() {
        return this.hashCode;
    }

    /**
     * Multiplies vector by -1
     *
     * @param a vector to multiply
     * @return result of multiplication
     */
    public static Point not(Point a) {
        return new Point(-a.x, -a.y);
    }

    /**
     * @return ASCII representation of the point
     */
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
