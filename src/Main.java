import java.util.*;

public class Main {
    public static void main(String[] args) {
//        interesting game case
//        runGame("[0, 0] [5, 5] [3, 7] [1, 2] [8, 7] [8, 8]", 1, new Backtracking());
//        runGame("[0, 0] [5, 5] [3, 7] [1, 2] [8, 7] [8, 8]", 1, new AStar());

//        example of a random game
//        new AStar().setGame(new Game(10, 1)).run(true);
//        new Backtracking().setGame(new Game(10, 1)).run(true);

//        to input console test
//        consoleTest();

//        run generated tests
        test(100);
    }

    static void consoleTest() {
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        int perception = scan.nextInt();

        runGame(input, perception, new Backtracking());
        runGame(input, perception, new AStar());
    }

    static void runGame(String input, int perception, Strategy strategy) {
        String[] tokens = input.replaceAll("[^],0-8]", "").split("]");

        Point[] points = new Point[6];
        for (int i = 0; i < points.length; i++) {
            String[] idx = tokens[i].split(",");
            points[i] = new Point(Integer.parseInt(idx[0]), Integer.parseInt(idx[1]));
//            System.out.println(points[i]);
        }
        try {
            Game game = new Game(points, perception);
            strategy.setGame(game).run(true);
//            new AStar().setGame(game).run(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void test(int n) {
        Map<Status, List<Integer>> statistic1 = new HashMap<>();
        Map<Status, List<Integer>> statistic2 = new HashMap<>();
        for (int i = 0; i < n; i++) {
            Strategy strategy1 = new Backtracking().setGame(new Game(i, 1));
            Strategy strategy2 = new AStar().setGame(new Game(i, 1));

            Player p1 = strategy1.run(false);
            Player p2 = strategy2.run(false);
            System.out.println(i + ": " + p1 + "\t" + p2);
            if (p1.status != p2.status)
                break;
            statistic1.computeIfAbsent(p1.status, k -> new ArrayList<>());
            statistic1.get(p1.status).add(p1.timer);

            statistic2.computeIfAbsent(p2.status, k -> new ArrayList<>());
            statistic2.get(p2.status).add(p2.timer);
        }

        System.out.println("Backtracking:");
        for (Map.Entry<Status, List<Integer>> entry : statistic1.entrySet()) {
            OptionalDouble average = entry.getValue().stream().mapToDouble(a -> a).average();
            double avg = average.isPresent() ? average.getAsDouble() : 0;
            System.out.println(entry.getKey() + ":" + avg + " " + entry.getValue().size());
        }

        System.out.println("A*:");
        for (Map.Entry<Status, List<Integer>> entry : statistic2.entrySet()) {
            OptionalDouble average = entry.getValue().stream().mapToDouble(a -> a).average();
            double avg = average.isPresent() ? average.getAsDouble() : 0;
            System.out.println(entry.getKey() + ":" + avg + " " + entry.getValue().size());
        }
    }
}

