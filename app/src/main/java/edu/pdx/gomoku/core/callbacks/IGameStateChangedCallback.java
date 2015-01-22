package edu.pdx.gomoku.core.callbacks;

import edu.pdx.gomoku.core.Game;

/**
 * Created by yuriy on 1/20/15.
 */
public interface IGameStateChangedCallback {
    public void onGameStateChanged(Game sender);
}
