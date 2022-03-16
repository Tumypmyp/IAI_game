public interface Strategy {
    Strategy setGame(Game game);

    Point getBOOK();

    Point getCLOAK();

    Player findWayToPoint(Point destination, Player player);

    Player[][] getHistory();
}
