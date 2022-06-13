package minimax.tictactoe;

import java.util.List;

public class TicTacToe {
    /**
     * Funkcja tworzy drzewo z mozliwymi ruchami na przekazanej planszy dla wskazanego gracza,
     * Wyszukuje najlepszy ruch i go zwraca.
     */
    public int[] computeNextMove(Board board, Player player) {
        Logger.log("Obliczanie kolejnego ruchu dla " + player + "...");

        //kopiujemy glowna plansze
        Board copy = board.copy();
        Node node = new Node(copy, player);

        //generujemy wszystkie mozliwe ruchy i dodajemy nody
        generateNextMoves(node, player);

        Logger.log("Znaleziono " + node.getChildrenCount(node) + " mozliwych stanow gry");

        //szukamy najlepszy ruch algorytmem minimax
        Node bestNode = minimaxGetBestNode(node, player);

        //wskazujemy jakie ruchy mozemy wykonac w danej rundzie -
        printAvailableMoves(player, node);
        printBestMove(bestNode);

        //zwracamy najlepszy ruch
        return createBestMove(bestNode);
    }

    /**
     * tworzy drzewo z mozliwymi ruchami
     */
    private void generateNextMoves(Node node, Player player) {
        Player[][] boardElements = node.getBoard().getPlayers();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardElements[i][j] != Player.EMPTY) {
                    //pole jest zajete, przechodzimy dalej
                    continue;
                }

                //kopiujemy plansze z node wyzej w drzewie, dodajemy do niej nowy ruch i dodajemy do grafu
                Board newBoard = node.getBoard().copy();
                newBoard.makeMove(i, j, player);
                Node newNode = new Node(newBoard, node.getPlayer(), i, j);
                node.addChild(newNode);

                BoardChecker boardChecker = new BoardChecker();
                Player winningPlayer = boardChecker.getWinningPlayer(newBoard);

                //wygeneruj kolejny ruch w rundzie jeśli gra nie jest jeszcze rozstrzygnięta
                //jesli na danym ruchu wygrywa jeden z graczy, nie generujmy dalej wgłąb
                if (winningPlayer == null) {
                    //rekurencyjnie szukamy kolejnych ruchów
                    generateNextMoves(newNode, Player.getNextPlayer(player));
                }
            }
        }

    }

    /**
     * Szuka najlepszego ruchu metoda minimax
     */
    private Node minimaxGetBestNode(Node node, Player maximizer) {
        BestNodeMiniMaxAlgorithm bestNodeMiniMaxAlgorithm = new BestNodeMiniMaxAlgorithm(maximizer);
        return bestNodeMiniMaxAlgorithm.getBestNode(node, true, 0);
    }

    /**
     * Pisze do konsoli o możliwych ruchach.
     */
    private void printAvailableMoves(Player player, Node node) {
        List<Node> children = node.getChildren();
        Logger.log("Dostępne ruchy: " + player.getBoardElement() + " na: ");
        for (Node child : children) {
            Logger.log(String.format("[%d,%d]", child.getSelectedICoordinate(), child.getSelectedJCoordinate()));
        }
    }

    /**
     * Pisze do konsoli jaki jest najlepszy ruch
     */
    private void printBestMove(Node bestNode) {
        if (bestNode != null) {
            Logger.log(String.format("Najlepszy ruch: [%d,%d]", bestNode.getSelectedICoordinate(), bestNode.getSelectedJCoordinate()));
        }
    }

    /**
     * Zwraca rzad i kolumne najlepszego ruchu
     */
    private int[] createBestMove(Node bestNode) {
        return new int[]{bestNode.getSelectedICoordinate(), bestNode.getSelectedJCoordinate()};
    }

    /**
     * Klasa pomocnicza do wybrania najlepszego ruchu wskazanego przez algorytm minimax
     */
    static class BestNodeMiniMaxAlgorithm {

        public static final int MIN_VALUE = -10;
        public static final int MAX_VALUE = 10;

        private Node bestNode;
        private Player maximizer;

        public BestNodeMiniMaxAlgorithm(Player maximizer) {
            this.maximizer = maximizer;
        }

        Node getBestNode(Node node, boolean maximization, int depth) {
            minimax(node, maximization, depth);
            return bestNode;
        }

        private MinimaxScore minimax(Node node, boolean maximization, int depth) {
            //wezel koncowy - wyliczamy wartosc
            if (node.isLeafNode()) {
                if (depth == 0) {
                    return null;
                }

                BoardChecker boardChecker = new BoardChecker();
                Player winningPlayer = boardChecker.getWinningPlayer(node.getBoard());
                if (winningPlayer == null) {
                    return new MinimaxScore(0);
                }
                if (winningPlayer == maximizer) {
                    return new MinimaxScore(MAX_VALUE - depth, depth);
                }
                if (winningPlayer != maximizer) {
                    return new MinimaxScore(MIN_VALUE + depth, depth);
                }
            }

            //szukany najlepszy wezel
            Node foundNode = null;
            //do wyszukiwania najwiekszej/najmniejszej ilosci krokow do wygranej/przegranej
            int winningDepth = maximization ? MAX_VALUE : MIN_VALUE;
            //do wyszukiwania najwiekszej/najmniejszej ilosci punktow
            int nodeValue = maximization ? Integer.MIN_VALUE : Integer.MAX_VALUE;

            for (Node child : node.getChildren()) {
                MinimaxScore childScore = minimax(child, !maximization, depth + 1);

                Integer childNodeValue = childScore.getScore();
                if (maximization) {
                    if (childNodeValue > nodeValue) {
                        nodeValue = childNodeValue;
                        foundNode = child;
                    }
                    //jesli trafi się drugi wygrany węzeł, preferuj ten w ktorym wygrana bedzie w mniejszej ilosci krokow
                    if (childNodeValue == nodeValue && childScore.getWinningDepth() < winningDepth) {
                        nodeValue = childNodeValue;
                        foundNode = child;
                        winningDepth = childScore.getWinningDepth();
                    }
                } else {
                    if (childNodeValue < nodeValue) {
                        nodeValue = childNodeValue;
                        foundNode = child;
                    }
                    //jesli trafi się drugi przegrany węzeł, preferuj ten w ktorym wygrana przeciwnika bedzie w jak najwiekszej ilosci krokow
                    if (childNodeValue == nodeValue && childScore.getWinningDepth() > winningDepth) {
                        nodeValue = childNodeValue;
                        foundNode = child;
                        winningDepth = childScore.getWinningDepth();
                    }
                }
            }

            if (depth > 0) {
                return new MinimaxScore(nodeValue, winningDepth);
            } else {
                bestNode = foundNode;
            }

            return null;
        }
    }

    /**
     * Klasa ktora zwraca wynik minimax (punkty) oraz glebokosc w ktorej zanotowana byla wygrana jednego z graczy
     */
    static class MinimaxScore {
        private int score;
        private int winningDepth = 100;

        public MinimaxScore(int score) {
            this.score = score;
        }

        public MinimaxScore(int score, int winningDepth) {
            this.score = score;
            this.winningDepth = winningDepth;
        }

        public int getScore() {
            return score;
        }

        public int getWinningDepth() {
            return winningDepth;
        }
    }


}