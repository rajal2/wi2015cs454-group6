package edu.pdx.gomoku.core;

import java.util.ArrayList;

import edu.pdx.gomoku.core.callbacks.IGameBoardChangedCallback;
import edu.pdx.gomoku.core.callbacks.OnGameBoardChangeEventArgs;

/**
 * Created by yuriy on 1/20/15.
 */
public class GameBoard {

    private final int rows, columns;
    private final GameCellState[][] cellStates;

    private final ArrayList<IGameBoardChangedCallback> callbacks = new ArrayList<>();


    public GameBoard(BoardSize boardSize) throws IllegalArgumentException {
        switch (boardSize) {
            case _10x10:
                rows = 10;
                columns = 10;
                break;
            case _15x15:
                rows = 15;
                columns = 15;
                break;
            case _20x20:
                rows = 20;
                columns = 20;
                break;
            default:
                throw new IllegalArgumentException();
        }

        cellStates = new GameCellState[rows][columns];
        clear();

    }

    public void clear() {
        for (int row = 0; row < rows; row++) {
            cellStates[row] = new GameCellState[columns];
            for (int column = 0; column < columns; column++) {
                cellStates[row][column] = GameCellState.Empty;
            }
        }
        raiseCallback(-1,-1);
    }

    public void registerCallback(IGameBoardChangedCallback target) {
        if (!callbacks.contains(target)) {
            callbacks.add(target);
        }
    }

    public void removeCallaback(IGameBoardChangedCallback target) {
        if (callbacks.contains(target)) {
            callbacks.remove(target);
        }
    }

    private void raiseCallback(int row, int column) {

        OnGameBoardChangeEventArgs args = new OnGameBoardChangeEventArgs(row, column);

        if (callbacks.size() > 0) {
            for (IGameBoardChangedCallback callback : callbacks) {
                callback.onGameBoardChanged(this, args);
            }
        }
    }

    void acceptMove(StoneColor color, int row, int column) throws IllegalMoveExcetion {
        //do stuff here to make sure the move is valid

        if (this.cellStates[row][column] != GameCellState.Empty) {
            throw new IllegalMoveExcetion();
        }

        switch (color) {
            case Black:
                this.cellStates[row][column] = GameCellState.BlackStone;
                raiseCallback(row, column);
                break;
            case White:
                this.cellStates[row][column] = GameCellState.WhiteStone;
                raiseCallback(row, column);
                break;

        }


    }

    /**
     * Returns game board state (rows, columns)
     */
    public GameCellState[][] getBoardState() {
        return cellStates;
    }

    public int getRowCount() {
        return rows;
    }

    public int getColumnCount() {
        return columns;
    }
}
