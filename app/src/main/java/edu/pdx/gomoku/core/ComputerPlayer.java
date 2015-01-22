package edu.pdx.gomoku.core;

import java.util.Random;

/**
 * Created by yuriy on 1/21/15.
 */
public class ComputerPlayer extends Player {


    public ComputerPlayer(StoneColor color) {
        super(PlayerType.Computer, color);
    }

    @Override
    public void startMove(Game game) {

        game.setState(GameState.WaitingForPlayerMove);

        Random random = new Random();

        //this is placeholder code
        while (true)
        {
            try
            {
                int row = random.nextInt() % game.getBoard().getRowCount();
                int col = random.nextInt() % game.getBoard().getColumnCount();

                game.acceptMove(this.getColor(), Math.abs(row), Math.abs(col));

                break;
            }
            catch(IllegalMoveExcetion mex)
            {
                //do nothing here
            } catch (MoveNotAllowedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void acceptMove(int row, int column) {
        //do nothing ()
    }
}
