import java.util.*;

public class Main {
    public static void main(String[] args) {
        new Backtracking().setGame(new Game(23, 1)).run(true);
//        test();
    }

    static void test() {
        Map<Status, List<Integer>> statistic = new HashMap<>();
        for (int i = 1; i < 10000; i++) {
            Strategy strategy = new Backtracking().setGame(new Game(i, 1));

            Player p = strategy.run(false);
//            System.out.println(i + ": " + p);
            if (statistic.get(p.status) == null)
                statistic.put(p.status, new ArrayList<>());
            statistic.get(p.status).add(p.timer);
        }
        for (Map.Entry<Status, List<Integer>> entry : statistic.entrySet()) {
            OptionalDouble average = entry.getValue().stream().mapToDouble(a -> a).average();
            double avg = average.isPresent() ? average.getAsDouble() : 0;
            System.out.println(entry.getKey() + ":" + avg + " " + entry.getValue().size());
        }
    }
}

