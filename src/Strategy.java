public interface Strategy {

    /**
     * @return string name of a strategy
     */
    String getName();

    /**
     * Using some strategy finds the shortest path to destination
     *
     * @param player      the initial agent
     * @param destination the point we go to
     * @return the agent that came to destination
     */
    Player findAWayToPoint(History history, Player player, Point destination);

}
