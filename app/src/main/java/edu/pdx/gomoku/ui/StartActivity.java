package edu.pdx.gomoku.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import edu.pdx.gomoku.core.BoardSize;
import edu.pdx.gomoku.core.GameMode;
import edu.pdx.gomoku.gomoku.R;


public class StartActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        binEvents();
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

    private void binEvents()
    {
        View temp = this.findViewById(R.id.singlePlayerGameButton);

        if(temp!=null)
        {
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startGame(GameMode.PlayerVsComputer);
                }
            });
        }

        temp = this.findViewById(R.id.twoPlayerGameButton);

        if(temp!=null)
        {
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startGame(GameMode.PlayerVsPlayer);
                }
            });
        }

        temp = this.findViewById(R.id.startNetworkGameButton);

        if(temp!=null)
        {
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNetworkGame();
                }
            });
        }

        temp = this.findViewById(R.id.joinNetworkGameButton);

        if(temp!=null)
        {
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinNetworkGame();
                }
            });
        }
    }

    private BoardSize getSelectedBoardSize()
    {
        View temp = findViewById(R.id.size10x10radioButton);

        if(temp != null && ((RadioButton)temp).isChecked())
        {
            return BoardSize._10x10;
        }

        temp = findViewById(R.id.size15x15radioButton);

        if(temp != null && ((RadioButton)temp).isChecked())
        {
            return BoardSize._15x15;
        }

        temp = findViewById(R.id.size20x20radioButton);

        if(temp != null && ((RadioButton)temp).isChecked())
        {
            return BoardSize._20x20;
        }

        throw  new IllegalStateException("Board size not selected");
}


    private void startGame(GameMode mode)
    {
        BoardSize size = getSelectedBoardSize();

        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra(BundleKeys.GAME_MODE, mode);
        intent.putExtra(BundleKeys.BOARD_SIZE, size);

        startActivity(intent);
    }

    private void startNetworkGame()
    {
        BoardSize size = getSelectedBoardSize();

        Intent intent = new Intent(this, StartNetworkGameActivity.class);

        intent.putExtra(BundleKeys.BOARD_SIZE, size);

        startActivity(intent);
    }

    private void joinNetworkGame()
    {
        BoardSize size = getSelectedBoardSize();

        Intent intent = new Intent(this, JoinNetworkGameActivity.class);

        intent.putExtra(BundleKeys.BOARD_SIZE, size);

        startActivity(intent);
    }
}
