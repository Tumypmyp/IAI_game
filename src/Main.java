import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println(new Game(9994).run(true));
        Map<Status, List<Integer>> statistic = new HashMap<>();
        for (int i = 1; i < 100; i++) {
            Game game = new Game(i);
            System.out.println(i + ": " + game.run(false));
            if (statistic.get(game.status) == null)
                statistic.put(game.status, new ArrayList<>());
            statistic.get(game.status).add(game.player.timer);
        }
        for (Map.Entry<Status, List<Integer>> entry : statistic.entrySet()) {
            OptionalDouble average = entry.getValue().stream().mapToDouble(a -> a).average();
            double avg = average.isPresent() ? average.getAsDouble() : 0;
            System.out.println(entry.getKey() + ":" + avg + " " + entry.getValue().toString());
        }
    }
}

