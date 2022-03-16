import java.util.*;

public class Main {
    public static void main(String[] args) {
//        interesting game case
        runGame("[0, 0] [5, 5] [3, 7] [1, 2] [8, 7] [8, 8]", 1, "Backtracking");
        runGame("[0, 0] [5, 5] [3, 7] [1, 2] [8, 7] [8, 8]", 1, "A*");

        new Search(new Game(0, 1), "A*").run(true);
        new Search(new Game(0, 1), "Backtracking").run(true);
//        example of a random game

//        to input console test
//        consoleTest();

//        run generated tests
        test(10);
    }

    static void consoleTest() {
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        int perception = scan.nextInt();

        runGame(input, perception, "A*");
        runGame(input, perception, "Backtracking");
    }

    static void runGame(String input, int perception, String strategy) {
        String[] tokens = input.replaceAll("[^],0-8]", "").split("]");

        Point[] points = new Point[6];
        for (int i = 0; i < points.length; i++) {
            String[] idx = tokens[i].split(",");
            points[i] = new Point(Integer.parseInt(idx[0]), Integer.parseInt(idx[1]));
//            System.out.println(points[i]);
        }
        try {
            Game game = new Game(points, perception);
            new Search(game, strategy).run(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void test(int n) {

        Map<Status, List<Integer>> statistic2 = new HashMap<>();
        Map<Status, List<Integer>> statistic1 = new HashMap<>();
        List<Integer> diff = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Search search1 = new Search(new Game(i, 1), "backtracking");
            Search search2 = new Search(new Game(i, 1), "A*");

            Player p1 = search1.run(false);
            Player p2 = search2.run(false);
            System.out.println(i + ": " + p1 + "\t" + p2);
            if (p1.status != p2.status)
                break;
            statistic1.computeIfAbsent(p1.status, k -> new ArrayList<>());
            statistic1.get(p1.status).add(p1.timer);

            statistic2.computeIfAbsent(p2.status, k -> new ArrayList<>());
            statistic2.get(p2.status).add(p2.timer);

            diff.add(p1.timer - p2.timer);

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
//        for (int i = 0; i < diff.size(); i++)
//            System.out.print(diff.get(i) + " ");
    }
}

