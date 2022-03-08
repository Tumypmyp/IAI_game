public class Main {

    public static void main(String[] args) {
        System.out.println(new Game(12345676).run(true));
        for (int i = 1; i < 100; i++)
            System.out.println(i + ": " + new Game(i).run(false));

        System.out.println(new Game(88).run(true));
    }
}

