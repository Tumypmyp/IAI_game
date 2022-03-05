public class Strategy {
    Player player;
    Strategy(){}

    public boolean ok(Point p) {
        return 0 <= p.x && p.x < Game.ROWS && 0 <= p.y && p.y < Game.COLUMNS
                && !player.getVisibleCardsByPoint(p).contains(Card.CAT);
    }

    public boolean dfsToCard(Card card, boolean[][] used) {
        if (player.getStatus() == Status.LOST)
            return false;
        if (player.getVisibleCardsByPoint(player.coordinates).contains(card))
            return true;
        used[player.coordinates.x][player.coordinates.y] = true;

        for (Point move : Player.MOVES) {
            Point next = Point.add(player.coordinates, move);
            if (ok(next) && !used[next.x][next.y]) {
                player.useMove(move);
                if (dfsToCard(card, used))
                    return true;
                player.useMove(Point.not(move));
            }
        }
        return false;
    }
}
