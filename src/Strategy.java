public interface Strategy {

    Game getGame();
    Point getBOOK();

    Point getCLOAK();

    Player findWayToPoint(Point destination, Player player);

    Player[][] getHistory();
}
