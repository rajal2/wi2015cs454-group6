package edu.pdx.gomoku.core;

import java.util.ArrayList;

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

    private Player currentPlayer, otherPlayer;

    private final ArrayList<IGameStateChangedCallback> callbacks = new ArrayList<>();

    public  Game(BoardSize boardSize, GameMode mode)
    {
        //intialize the board and start listening to its events
        this.board = new GameBoard(boardSize);
        this.board.registerCallback(this);

        this.mode = mode;

        initializePlayers(mode);
        startGame();
    }

    /**
     * Restarts the game with the current parameters
     */
    public  void restart()
    {
        board.clear();
        startGame();
    }

    /**
     * Starts a new game based on the parameters passed into the constructor
     */
    private void startGame()
    {
        initializePlayers(this.mode);
        currentPlayer.startMove(this);
    }

    private void initializePlayers(GameMode mode)
    {
        //todo: randomize the players somehow
        switch (mode)
        {
            case PlayerVsPlayer:
                currentPlayer = new LocalPlayer(StoneColor.White);
                otherPlayer = new LocalPlayer(StoneColor.Black);
                break;
            case PlayerVsComputer:

                currentPlayer = new LocalPlayer(StoneColor.White);
                otherPlayer = new ComputerPlayer(StoneColor.Black);
                break;
            case NetworkHost:
            case NetworkGuest:
                throw  new IllegalStateException("This is not implemented");
            default:
                throw  new IllegalArgumentException();
        }
    }

    void acceptMove(StoneColor color, int row, int column) throws IllegalMoveExcetion, MoveNotAllowedException
    {
        //figure out if the move is legal (i.e. the game is expecting a move) and what the color is
        if(color != currentPlayer.getColor())
        {
            throw  new MoveNotAllowedException();
        }


        //forward to the board and bail out
        board.acceptMove(currentPlayer.getColor(), row, column);
    }

    public  GameState getState()
    {
        return  state;
    }

    public GameMode getMode() { return mode; }

    public GameBoard getBoard() { return  board; }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }


    public void setState(GameState state)
    {
        //TODO: Make sure the state is legal (if needed)
        this.state = state;

        raiseCallback();
    }



    public void registerCallback(IGameStateChangedCallback target)
    {
        if(!callbacks.contains(target))
        {
            callbacks.add(target);
        }
    }

    public  void removeCallaback(IGameStateChangedCallback target)
    {
        if(callbacks.contains(target))
        {
            callbacks.remove(target);
        }
    }

    /**
     * Notifies the listeners (if any) about the game state changes
     */
    private void raiseCallback()
    {
        if(callbacks.size()>0) {
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

        if(args.getRow()>=0 && args.getColumn()>=0)
        {

            if(isWinner(currentPlayer.getColor(), args.getRow(), args.getRow()))
            {
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
     * @param color
     * @param row
     * @param column
     * @return
     */
    private boolean isWinner(StoneColor color, int row, int column)
    {
        //TODO: Valiate the input (row/column >= 0 and < max rows/columns, etc.)


        //TODO: This is dumb placeholder code. Needs to be replace with the actual implementation

        GameCellState[][] cells = board.getBoardState();

        GameCellState targetState = color== StoneColor.Black ? GameCellState.BlackStone : GameCellState.WhiteStone;

        //rought temp code:

        //check row

        int start = column, end = column;

        while(start>0 && cells[row][start]== targetState)
        {
            start--;
        }

        while(end<board.getColumnCount() && cells[row][end]== targetState)
        {
            end++;
        }

        if(end-start>5)
        {
            return true;
        }

        //check column


        //check diagonals

        return false;
    }

}
