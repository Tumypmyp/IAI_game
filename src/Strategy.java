public interface Strategy {

    Player run(boolean debug);
    Strategy setGame(Game game);

}
