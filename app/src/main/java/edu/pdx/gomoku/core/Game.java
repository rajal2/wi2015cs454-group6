package edu.pdx.gomoku.core;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import edu.pdx.gomoku.core.callbacks.IGameBoardChangedCallback;
import edu.pdx.gomoku.core.callbacks.IGameStateChangedCallback;
import edu.pdx.gomoku.core.callbacks.OnGameBoardChangeEventArgs;

/**
 * Created by yuriy on 1/20/15.
 */
public class Game implements IGameBoardChangedCallback {

    private final GameBoard board;
    private GameState state = GameState.Unknown;
    private GameMode mode;
    private BoardSize boardSize;

    private Player currentPlayer, otherPlayer;

    private final ArrayList<IGameStateChangedCallback> callbacks = new ArrayList<>();

    public Game(BoardSize boardSize, GameMode mode) {
        //intialize the board and start listening to its events
        this.board = new GameBoard(boardSize);
        this.board.registerCallback(this);
        this.boardSize = boardSize;
        this.mode = mode;

        startGame();
    }

    /**
     * Restarts the game with the current parameters
     */
    public void restart() {
        board.clear();
        new Game(this.boardSize, this.mode);
    }

    /**
     * Starts a new game based on the parameters passed into the constructor
     */
    private void startGame() {
        initializePlayers(this.mode);

        StoneColor color = currentPlayer.getColor();
        if (color == StoneColor.Black)
            currentPlayer.startMove(this);
        else
            otherPlayer.startMove(this);
    }

    private void initializePlayers(GameMode mode) {
        //todo: randomize the players somehow
        switch (mode) {
            case PlayerVsPlayer:
                currentPlayer = new LocalPlayer(StoneColor.Black);
                otherPlayer = new LocalPlayer(StoneColor.White);

                break;
            case PlayerVsComputer:
                int blackOrWhite = randomSelectPlayerColor();
                if (blackOrWhite < 1) {
                    currentPlayer = new LocalPlayer(StoneColor.Black);
                    otherPlayer = new ComputerPlayer(StoneColor.White);
                }
                else {
                    currentPlayer = new ComputerPlayer(StoneColor.Black);
                    otherPlayer = new LocalPlayer(StoneColor.White);
                }

                break;
            case NetworkHost:
            case NetworkGuest:
                throw new IllegalStateException("This is not implemented");
            default:
                throw new IllegalArgumentException();
        }
    }

    void acceptMove(StoneColor color, int row, int column) throws IllegalMoveExcetion, MoveNotAllowedException {
        //figure out if the move is legal (i.e. the game is expecting a move) and what the color is
        if (color != currentPlayer.getColor()) {
            throw new MoveNotAllowedException();
        }

        //forward to the board and bail out
        board.acceptMove(currentPlayer.getColor(), row, column);
    }

    public GameState getState() {
        return state;
    }

    public GameMode getMode() {
        return mode;
    }

    public GameBoard getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int randomSelectPlayerColor() {
        Random rNum = new Random();
        return rNum.nextInt(2);
    }

    public void setState(GameState state) {
        //TODO: Make sure the state is legal (if needed)
        this.state = state;

        raiseCallback();
    }


    public void registerCallback(IGameStateChangedCallback target) {
        if (!callbacks.contains(target)) {
            callbacks.add(target);
        }
    }

    public void removeCallaback(IGameStateChangedCallback target) {
        if (callbacks.contains(target)) {
            callbacks.remove(target);
        }
    }

    /**
     * Notifies the listeners (if any) about the game state changes
     */
    private void raiseCallback() {
        if (callbacks.size() > 0) {
            for (IGameStateChangedCallback callback : callbacks) {
                callback.onGameStateChanged(this);
            }
        }
    }

    @Override
    public void onGameBoardChanged(GameBoard sender, OnGameBoardChangeEventArgs args) {

        //check for winner
        //args has the location of the last move. To see if there is a winner
        //we need to check only the adjacent locations

        if (args.getRow() >= 0 && args.getColumn() >= 0) {

            if (isWinner(currentPlayer.getColor(), args.getRow(), args.getColumn())) {
                state = GameState.PlayerWins;
                //Raise callback
                raiseCallback();
                return;
            }

        }

        //TODO: Check if the board is not full

        //if we made it here, start the next move
        startNextMove();

    }

    private void startNextMove() {

        //otherwise, flip the player and return
        Player temp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = temp;

        currentPlayer.startMove(this);
    }

    /**
     * Check the current state of the game board to see if the provided move resulted in a win for the current player
     *
     * @param color
     * @param row
     * @param column
     * @return
     */
    private boolean isWinner(StoneColor color, int row, int column) {
        Log.d("isWinner", "color is " + color + " position " + row + ", " + column);

        //TODO: Valiate the input (row/column >= 0 and < max rows/columns, etc.)
        //TODO: isn't it better to put this part into the GameBoard.acceptMove()
        if (row < 0 || column < 0 || row >= board.getRowCount() || column >= board.getColumnCount()) {
            Log.d("isWinner", "invalid input!");
        }


        //TODO: This is dumb placeholder code. Needs to be replace with the actual implementation

        GameCellState[][] cells = board.getBoardState();
        GameCellState targetState = color == StoneColor.Black ? GameCellState.BlackStone : GameCellState.WhiteStone;

        //algorithm:
        // 1. count back (examine the adjacent cell) till one of the following 2 cases happens:
        //edge of board: position <= 0
        //cell is not same color or is empty: cell at position != targetState
        //2. count forward till one of the following 2 cases happens:
        //edge of board: position >= board.getColumnCount()
        //cell is not same color or is empty: cell at position != targetState
        // 3. check if count == 4, do step 4 only if it's true
        // 4. check if both ends are blocked:
        // Note that on the edge of board, checking next cell state may go beyond array boundary
        // But java uses lazy evaluation, so we don't run into trouble as long as we don't change the order of expressions.
        // boolean endsBlocked = ((startX == 0 || startY == 0 || cell next to start != empty) &&
        //    (endX == board.getColumnCount()-1 || endY == board.getRowCount()-1 || cell next to end != empty)
        //
        // 5. if !endsBlocked return true;

        boolean checkRowResult = checkRow(cells, targetState, row, column);
        boolean checkColumnResult = checkColumn(cells, targetState, row, column);
        boolean checkFromUpLfResult = checkFromUpperLeft(cells, targetState, row, column);
        boolean checkFromLoLfResult = checkFromLowerLeft(cells, targetState, row, column);

        return checkRowResult ||
                checkColumnResult ||
                checkFromUpLfResult ||
                checkFromLoLfResult;

    }


    private boolean checkRow(GameCellState[][] cells, GameCellState targetState, int row, int column) {

        int start = column, end = column;

        while (start > 0 && cells[row][start - 1] == targetState) {
            start--;
        }

        while (end < board.getColumnCount() - 1 && cells[row][end + 1] == targetState) {
            end++;
        }

        if (end - start == 4) {
            boolean endsBlocked = ((start == 0 || cells[row][start-1] != GameCellState.Empty) &&
                          (end == board.getColumnCount()-1 || cells[row][end + 1] != GameCellState.Empty));
            if(!endsBlocked)
            return true;
        }

        return false;

    }

    private boolean checkColumn(GameCellState[][] cells, GameCellState targetState, int row, int column) {
        int start = row, end = row;
        while(start>0 && cells[start - 1][column]== targetState)
        {
            start--;
        }

        while(end<board.getRowCount()-1 && cells[end + 1][column]== targetState)
        {
            end++;
        }

        Log.d("check column:", "start at " + start + ", end at " + end);
        if(end-start == 4)
        {
            boolean endsBlocked = ((start == 0 || cells[start - 1][column] != GameCellState.Empty) &&
                   (end == board.getRowCount()-1 || cells[end + 1][column] != GameCellState.Empty));
            if(!endsBlocked)
                return true;
        }
        return false;
    }

    private boolean checkFromUpperLeft(GameCellState[][] cells, GameCellState targetState, int row, int column) {
        int startC = column, endC = column, startR = row, endR = row;

        while(startC>0 && startR < board.getRowCount() - 1 && cells[startR+1][startC-1]== targetState)
        {
            startC--;
            startR++;
        }

        while(endC<board.getColumnCount()-1 && endR>0 && cells[endR-1][endC+1]== targetState)
        {
            endC++;
            endR--;
        }

        Log.d("check from upper left:", "start at row" + startR + " column " + startC + ", end at " + endR + ", " + endC);
        if(endC-startC == 4)
        {
            boolean endsBlocked = ((startC == 0 || startR == board.getRowCount()-1 || cells[startR+1][startC-1] != GameCellState.Empty) &&
                    (endC == board.getColumnCount()-1 || endR == 0 || cells[endR-1][endC+1] != GameCellState.Empty));
            if(!endsBlocked)
                return true;
        }

        return false;

    }

    private boolean checkFromLowerLeft(GameCellState[][] cells, GameCellState targetState, int row, int column) {
        int startC = column, endC = column, startR = row, endR = row;
        while(startC>0 && startR >0 && cells[startR-1][startC-1]== targetState)
        {
            startR--;
            startC--;
        }

        while(endC<board.getColumnCount()-1 && endR < board.getRowCount()-1 && cells[endR+1][endC+1]== targetState)
        {
            endR++;
            endC++;
        }

        if(endR-startR == 4)
        {
            //Log.d("isWinner", "checking ends!");
            boolean endsBlocked = ((startC == 0 || startR == 0 || cells[startR-1][startC-1] != GameCellState.Empty) &&
                   (endC == board.getColumnCount()-1 || endR == board.getRowCount()-1 || cells[endR+1][endC+1] != GameCellState.Empty));
            //Log.d("isWinner", "endsBlocked is: " + endsBlocked);
            if(!endsBlocked)
                return true;
        }

        return false;
    }
}
