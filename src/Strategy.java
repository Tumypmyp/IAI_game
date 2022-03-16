public interface Strategy {
    Player findWayToPoint(Point destination, Player player);

    Player[][] getHistory();
}
