package minimax.tictactoe;

public class BoardChecker {

    public Player getWinningPlayer(Board board) {
        Player player = checkColumns(board);
        if (player != null) {
            return player;
        }

        player = checkRows(board);
        if (player != null) {
            return player;
        }

        return checkDiagonals(board);
    }

    private Player checkDiagonals(Board board) {
        Player[][] boardElements = board.getPlayers();
        return (boardElements[0][0] == boardElements[1][1] && boardElements[1][1] == boardElements[2][2]) ||
                (boardElements[0][2] == boardElements[1][1] && boardElements[1][1] == boardElements[2][0])
                ? getWinningPlayerOrNull(boardElements[1][1])
                : null;
    }

    private Player checkRows(Board board) {
        Player[][] boardElements = board.getPlayers();
        for (int i = 0; i < 3; i++) {
            if (boardElements[0][i] == boardElements[1][i] && boardElements[1][i] == boardElements[2][i]) {
                return getWinningPlayerOrNull(boardElements[0][i]);
            }
        }

        return null;
    }

    private Player checkColumns(Board board) {
        Player[][] boardElements = board.getPlayers();
        for (int i = 0; i < 3; i++) {
            if (boardElements[i][0] == boardElements[i][1] && boardElements[i][1] == boardElements[i][2]) {
                return getWinningPlayerOrNull(boardElements[i][0]);
            }
        }
        return null;
    }

    private Player getWinningPlayerOrNull(Player player) {
        if (player == Player.EMPTY) {
            return null;
        }

        return player;
    }
}
