package minimax.tictactoe;

public enum Player {
    CROSS,
    CIRCLE,
    EMPTY;

    static Player getNextPlayer(Player player) {
        if (player == CROSS)
            return CIRCLE;

        return CROSS;
    }

    public String getBoardElement() {
        if (this == CROSS) {
            return "X";
        }
        if (this == CIRCLE) {
            return "0";
        }
        return " ";
    }
}
