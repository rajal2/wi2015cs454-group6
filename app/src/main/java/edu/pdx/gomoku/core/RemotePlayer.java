package edu.pdx.gomoku.core;

/**
 * Created by yuriy on 1/21/15.
 */
public class RemotePlayer extends Player {

    public RemotePlayer(StoneColor color) {
        super(PlayerType.Remote, color);
    }

    @Override
    public void startMove(Game game) {
        //TODO: Implement the network game functionality here
        //tell the other networked player that it's his/her move
    }

    @Override
    public void acceptMove(int row, int column) throws IllegalMoveExcetion, MoveNotAllowedException {
        //do nothing
    }
}
