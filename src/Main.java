import java.util.*;

public class Main {
    public static void main(String[] args) {
//        interesting game case
//        runGame("[0, 0] [5, 5] [3, 7] [1, 2] [8, 7] [8, 8]", "Backtracking", 2);
//        runGame("[0, 0] [5, 5] [3, 7] [1, 2] [8, 7] [8, 8]", "A*", 2);


//        runGame("[0, 0] [0, 4] [3, 0] [7, 8] [1, 0] [8, 8]", "A*1", 2);
//        runGame("[0, 0] [0, 4] [3, 0] [7, 8] [1, 0] [8, 8]", 2, "B");
//        runGame("[0, 0] [0, 3] [1, 3] [7, 8] [0, 0] [8, 8]", 1, "B");


//        runGame("[0, 0] [0, 3] [1, 3] [7, 8] [0, 0] [8, 8]", 1, "A*1");
//        runGame("[0, 0] [0, 3] [1, 3] [7, 8] [0, 0] [8, 8]", 1, "B");
//
//        new Search(new Game(0), "A*", 2).run(true);
//        new Search(new Game(3), "Back", 1).run(true);
//        new Search(new Game(26), "A", 2).run(true);
//        new Search(new Game(26), "Backtracking", 2).run(true);

//          example of a random game

//        to input console test
//        consoleTest();

//        run generated tests
        test(0, 1000, 1);

    }

    static void consoleTest() {
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        int perception = scan.nextInt();

        runGame(input, "A*", perception);
        runGame(input, "Backtracking", perception);
    }

    static void runGame(String input, String strategy, int perception) {
        String[] tokens = input.replaceAll("[^],0-8]", "").split("]");

        Point[] points = new Point[tokens.length];
        for (int i = 0; i < points.length; i++) {
            String[] idx = tokens[i].split(",");
            points[i] = new Point(Integer.parseInt(idx[0]), Integer.parseInt(idx[1]));
            System.out.println(points[i]);
        }
        try {
            Game game = new Game(points);
            Player player = new Search(game, strategy, perception).run(true);
//            System.out.println(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void test(int L, int R, int perception) {

        Map<Status, List<Integer>> statistic2 = new HashMap<>();
        Map<Status, List<Integer>> statistic1 = new HashMap<>();
        List<Integer> diff = new ArrayList<>();

        for (int i = L; i < R; i++) {
            Search search1 = new Search(new Game(i), "backtracking", perception);
            Search search2 = new Search(new Game(i), "A*", perception);

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

        System.out.println("\n|Backtracking:status | avg len | number af games|\n|---|---|---|");
        for (Map.Entry<Status, List<Integer>> entry : statistic1.entrySet()) {
            OptionalDouble average = entry.getValue().stream().mapToDouble(a -> a).average();
            double avg = average.isPresent() ? average.getAsDouble() : 0;
            System.out.printf("|%s|%f|%d|\n", entry.getKey(), avg, entry.getValue().size());
        }

        System.out.println("\n|A*:status | avg len | number af games|\n|---|---|---|");
        for (Map.Entry<Status, List<Integer>> entry : statistic2.entrySet()) {
            OptionalDouble average = entry.getValue().stream().mapToDouble(a -> a).average();
            double avg = average.isPresent() ? average.getAsDouble() : 0;
            System.out.printf("|%s|%f|%d|\n", entry.getKey(), avg, entry.getValue().size());
        }
        for (int i = 0; i < diff.size(); i++)
            if (diff.get(i) != 0) {
                System.out.println(i + " : " + diff.get(i));
            }
    }
}

