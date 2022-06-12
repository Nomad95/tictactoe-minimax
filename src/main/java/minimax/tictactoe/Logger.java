package minimax.tictactoe;

public enum Logger {
    I;

    public static void log(String msg) {
        System.out.println(msg);
    }

    public static void put(String msg) {
        System.out.print(msg);
    }
}
