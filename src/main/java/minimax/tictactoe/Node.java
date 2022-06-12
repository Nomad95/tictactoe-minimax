package minimax.tictactoe;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private Board board;
    private Player player;
    private List<Node> children = new ArrayList<>();
    private int selectedICoordinate;
    private int selectedJCoordinate;

    public Node(Board board, Player player) {
        this.board = board;
        this.player = player;
    }

    public Node(Board board, Player player, int selectedICoordinate, int selectedJCoordinate) {
        this.board = board;
        this.player = player;
        this.selectedICoordinate = selectedICoordinate;
        this.selectedJCoordinate = selectedJCoordinate;
    }

    void addChild(Node node){
        children.add(node);
    }

    int getChildrenCount(Node node) {
        int nodesCount = 0;
        if (node.getChildren().size() == 0) {
            return 1;
        }
        for (Node child : node.getChildren()) {
            nodesCount += getChildrenCount(child);
        }

        return nodesCount;
    }

    boolean isLeafNode() {
        return getChildren().size() == 0;
    }


    Board getBoard() {
        return board;
    }

    Player getPlayer() {
        return player;
    }

    List<Node> getChildren() {
        return children;
    }

    public int getSelectedICoordinate() {
        return selectedICoordinate;
    }

    public int getSelectedJCoordinate() {
        return selectedJCoordinate;
    }
}
