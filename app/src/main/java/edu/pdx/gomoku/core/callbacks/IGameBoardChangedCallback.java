package edu.pdx.gomoku.core.callbacks;

import edu.pdx.gomoku.core.GameBoard;

/**
 * Created by yuriy on 1/20/15.
 */
public interface IGameBoardChangedCallback {
    void onGameBoardChanged(GameBoard sender, OnGameBoardChangeEventArgs args);
}
