package edu.pdx.gomoku.core.callbacks;

/**
 * Created by yuriy on 1/20/15.
 */
public class OnGameBoardChangeEventArgs {
    private int row;
    private  int column;

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public OnGameBoardChangeEventArgs(int row, int column) {
        this.row = row;
        this.column = column;
    }
}
