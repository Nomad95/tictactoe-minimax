package minimax.tictactoe;

public class Board {
    private Player[][] boardElements;

    public Board() {
        initializeNewEmptyBoard();
    }

    private Board(Player[][] boardElements) {
        this.boardElements = boardElements;
    }

    private void initializeNewEmptyBoard() {
        boardElements = new Player[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardElements[i][j] = Player.EMPTY;
            }
        }
    }

    public void makeMove(int i, int j, Player boardElement) {
        if (boardElements[i][j] != Player.EMPTY)
            throw new IllegalArgumentException("Could not put element here!");
        boardElements[i][j] = boardElement;
    }

    Board copy() {
        return new Board(copyElements());
    }

    public Player[][] getPlayers() {
        return copyElements();
    }

    private Player[][] copyElements() {
        Player[][] copiedElements = new Player[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(boardElements[i], 0, copiedElements[i], 0, 3);
        }
        return copiedElements;
    }

    public boolean isEndOfGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardElements[i][j] == Player.EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }
}
