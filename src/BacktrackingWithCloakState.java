//public class BacktrackingWithCloakState implements Strategy {
//    Player player;
//
//    BacktrackingWithCloakState() {
//    }
//
//    @Override
//    public void play(boolean debug) {
//
//        if (debug)
//            player.game.print();
//
//        boolean ok = dfsToCard(Card.BOOK, new boolean[Game.ROWS][Game.COLUMNS][2]);
//
//        if (debug)
//            player.game.print();
//
//        if (!ok)
//            return;
//        dfsToCard(Card.EXIT, new boolean[Game.ROWS][Game.COLUMNS][2]);
//
//        if (debug)
//            player.game.print();
//    }
//
//    @Override
//    public void setPlayer(Player player) {
//        this.player = player;
//    }
//
//
//    public boolean dfsToCard(Card card, boolean[][][] used) {
//        if (player.getStatus() == Status.LOST)
//            return false;
//        if (player.getVisibleCardsByPoint(player.coordinates).contains(card))
//            return true;
//        used[player.coordinates.x][player.coordinates.y][player.haveCloak ? 1 : 0] = true;
//
//        for (Point move : Player.MOVES) {
//            Point next = Point.add(player.coordinates, move);
//            if (player.ok(next) && !used[next.x][next.y][player.haveCloak ? 1 : 0]) {
//                player.useMove(move);
//                if (dfsToCard(card, used))
//                    return true;
//                player.useMove(Point.not(move));
//            }
//        }
//        return false;
//    }
//}
