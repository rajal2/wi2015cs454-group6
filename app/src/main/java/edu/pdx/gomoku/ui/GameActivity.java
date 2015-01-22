package edu.pdx.gomoku.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.pdx.gomoku.core.BoardSize;
import edu.pdx.gomoku.core.Game;
import edu.pdx.gomoku.core.GameMode;
import edu.pdx.gomoku.core.Player;
import edu.pdx.gomoku.core.callbacks.IGameStateChangedCallback;
import edu.pdx.gomoku.gomoku.R;


public class GameActivity extends ActionBarActivity implements IGameStateChangedCallback {

    Game game;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        GameMode mode = (GameMode)this.getIntent().getSerializableExtra(BundleKeys.GAME_MODE);

        BoardSize size = (BoardSize)this.getIntent().getSerializableExtra(BundleKeys.BOARD_SIZE);

        game = new Game(size,mode);

        game.registerCallback(this);

        GameBoardControl boardControl = (GameBoardControl) this.findViewById(R.id.gameBoardControl);

        boardControl.bind(game);


        TextView title = (TextView)findViewById(R.id.titleTextView);
        Button newGameButton = (Button)findViewById(R.id.newGameButton);

        switch (mode)
        {
            case PlayerVsPlayer:
                title.setText("Two Player Game");
                newGameButton.setVisibility(View.VISIBLE);
                break;

            case PlayerVsComputer:
                title.setText("Single Player Game");
                newGameButton.setVisibility(View.VISIBLE);
                break;

            case  NetworkGuest:
                title.setText("Network Game (Guest)");
                newGameButton.setVisibility(View.GONE);
                break;

            case NetworkHost:
                title.setText("Network Game (Host)");
                newGameButton.setVisibility(View.GONE);
                break;
        }

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.restart();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGameStateChanged(Game sender) {

        TextView status = (TextView)findViewById(R.id.statusTextView);

        Player player = sender.getCurrentPlayer();



        switch (sender.getState())
        {
            case WaitingForPlayerMove:

                switch (game.getCurrentPlayer().getType())
                {
                    case Computer:
                        status.setText("Waiting for computer's move");
                        break;
                    case Remote:
                        status.setText("Waiting for remote player's move");
                        break;
                    case Human:
                        break;
                }
                break;
            case PlayerWins:
                ShowWinner("Player wins.\r\nRestart?");
                break;
        }
    }

    private void ShowWinner(String message)
    {
        new AlertDialog.Builder(this)
                .setTitle("Game Over!")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        game.restart();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        GameActivity.this.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}
