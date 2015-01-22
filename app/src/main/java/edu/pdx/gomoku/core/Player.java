package edu.pdx.gomoku.core;

/**
 * Created by yuriy on 1/20/15.
 */
public abstract class Player {
    private PlayerType type;
    private StoneColor color;

    public PlayerType getType() {
        return type;
    }

    public StoneColor getColor() {
        return color;
    }

    public Player(PlayerType type, StoneColor color) {
        this.type = type;
        this.color = color;
    }

    /**
     * Initializes player's move
     * @param game
     */
    public abstract void startMove(Game game);

    /**
     * Processes player move (from the UI)
     * @param row
     * @param column
     * @throws IllegalMoveExcetion
     * @throws MoveNotAllowedException
     */
    public  abstract  void acceptMove(int row, int column)  throws IllegalMoveExcetion, MoveNotAllowedException;
}
