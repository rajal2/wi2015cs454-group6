package edu.pdx.gomoku.core;

import android.util.Log;

import edu.pdx.gomoku.core.Player;

/**
 * Created by yuriy on 1/21/15.
 */
public class LocalPlayer extends Player {

    private Game game;

    public LocalPlayer(StoneColor color) {
        super(PlayerType.Human, color);
    }

    @Override
    public void startMove(Game game) {
        Log.d("Player", "Local player move");
        this.game = game;
        game.setState(GameState.WaitingForPlayerMove);
    }

    @Override
    public void acceptMove(int row, int column) throws IllegalMoveExcetion, MoveNotAllowedException {
        if(game!=null)
        {
            game.acceptMove(this.getColor(), row, column);
        }
    }
}
