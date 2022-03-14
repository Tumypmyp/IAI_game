import java.util.*;

public class Main {
    public static void main(String[] args) {
//        runGame("[0, 0] [5, 5] [6, 6] [1, 8] [8, 7] [8, 1]", 1);
//        new AStar().setGame(new Game(10, 1)).run(true);
//        new Backtracking().setGame(new Game(176, 1)).run(true);
        test();
//        consoleTest();
    }
    static void consoleTest() {
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        int perception = scan.nextInt();

        runGame(input, perception);
    }
    static void runGame(String input, int perception) {
        String[] tokens = input.replaceAll("[^],0-8]", "").split("]");

        Point[] points = new Point[6];
        for (int i = 0; i < points.length; i++) {
            String[] idx = tokens[i].split(",");
            points[i] = new Point(Integer.parseInt(idx[0]), Integer.parseInt(idx[1]));
            System.out.println(points[i]);
        }
        try {
            Game game = new Game(points, perception);
            new Backtracking().setGame(game).run(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void test() {
        Map<Status, List<Integer>> statistic = new HashMap<>();
        for (int i = 1; i < 1000; i++) {
            Strategy strategy = new Backtracking().setGame(new Game(i, 1));

            Player p = strategy.run(false);
            System.out.println(i + ": " + p);
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

