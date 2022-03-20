public interface Strategy {
    String getName();

    Player findAWayToPoint(History history, Player player, Point destination);
}
