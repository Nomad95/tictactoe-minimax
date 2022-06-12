package minimax;

import minimax.tictactoe.*;

import java.util.Scanner;

public class Game {

    public static void main(String[] args) {
        //Tworzymy nowa plansze
        Board board = new Board();
        TicTacToe ticTacToe = new TicTacToe();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            Logger.log("--------------- Ruch kółko --------------- ");
            ticTacToe.computeNextMove(board, Player.CIRCLE);
            makeMove(board, scanner, Player.CIRCLE);
            printBoard(board);
            checkWinner(board);

            Logger.log("--------------- Ruch krzyżyk (komputer) --------------- ");
            int[] bestMove = ticTacToe.computeNextMove(board, Player.CROSS);
            makeComputerMove(board, bestMove, Player.CROSS);
            printBoard(board);
            checkWinner(board);
        }
    }

    private static void checkWinner(Board board) {
        BoardChecker boardChecker = new BoardChecker();
        Player winningPlayer = boardChecker.getWinningPlayer(board);
        if (winningPlayer == null) {
            if (board.isEndOfGame()) {
                Logger.log("REMIS!");
                System.exit(0);
            }
        }
        if (winningPlayer != null) {
            Logger.log("Wygrywa: " + winningPlayer);
            System.exit(0);
        }
    }

    private static void makeMove(Board board, Scanner scanner, Player player) {
        Logger.log("Wprowadz ruch w formacie (rząd kolumna)");

        while (true) {
            try {
                String line = scanner.nextLine();
                String[] coordinates = line.trim().split(" ");
                int coordI = Integer.parseInt(coordinates[0]);
                int coordJ = Integer.parseInt(coordinates[1]);
                board.makeMove(coordI, coordJ, player);
            } catch (Exception e) {
                Logger.log("Wprowadzono zle rzad i kolumne. Sproboj jeszcze raz");
                continue;
            }

            break;
        }
    }

    private static void makeComputerMove(Board board, int[] bestMoveCoordinates, Player player) {
        board.makeMove(bestMoveCoordinates[0], bestMoveCoordinates[1], player);
    }

    private static void printBoard(Board board) {
        Player[][] boardElements = board.getPlayers();
        for (int i = 0; i < 3; i++) {
            Logger.put("|");
            for (int j = 0; j < 3; j++) {
                Player boardElement = boardElements[i][j];
                Logger.put(boardElement.getBoardElement());
                Logger.put("|");

            }
            Logger.log(" ");
        }
    }
}
