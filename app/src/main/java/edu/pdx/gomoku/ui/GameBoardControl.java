package edu.pdx.gomoku.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import edu.pdx.gomoku.core.Game;
import edu.pdx.gomoku.core.GameBoard;
import edu.pdx.gomoku.core.GameCellState;
import edu.pdx.gomoku.core.IllegalMoveException;
import edu.pdx.gomoku.core.IllegalMoveException;
import edu.pdx.gomoku.core.MoveNotAllowedException;
import edu.pdx.gomoku.core.callbacks.IGameBoardChangedCallback;
import edu.pdx.gomoku.core.callbacks.OnGameBoardChangeEventArgs;

/**
 * Created by yuriy on 1/20/15.
 */
public class GameBoardControl extends View implements IGameBoardChangedCallback {

    Game game;

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public GameBoardControl(Context context) {
        super(context);
        init();
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p/>
     * <p/>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     * @see #View(android.content.Context, AttributeSet, int)
     */
    public GameBoardControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute. This constructor of View allows subclasses to use their
     * own base style when they are inflating. For example, a Button class's
     * constructor would call this version of the super class constructor and
     * supply <code>R.attr.buttonStyle</code> for <var>defStyleAttr</var>; this
     * allows the theme's button style to modify all of the base view attributes
     * (in particular its background) as well as the Button class's attributes.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     * @see #View(android.content.Context, android.util.AttributeSet)
     */
    public GameBoardControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //makes sure the view is square
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public void bind(Game game)
    {
        this.game = game;
        game.getBoard().registerCallback(this);


    }

    /**
     * Implement this to do your drawing.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGrid(canvas);
        drawStones(canvas);
    }

    Paint linePaint = new Paint();
    Paint blackPaint = new Paint();
    Paint whitePaint = new Paint();

    private void init()
    {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.DKGRAY);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeWidth(2F);

        whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
        whitePaint.setColor(Color.WHITE);

        blackPaint = new Paint();
        blackPaint.setAntiAlias(true);
        blackPaint.setColor(Color.BLACK);
    }

    private void drawGrid(Canvas canvas)
    {
        int width = this.getWidth();
        int height = this.getHeight();

        int rows = game.getBoard().getRowCount();
        int columns = game.getBoard().getColumnCount();

        float xRatio = (float)width / columns;
        float yRatio = (float)height / rows;

        float paddingTop = yRatio / 2;
        float paddingLeft = xRatio / 2;

        for(int row = 0; row<game.getBoard().getRowCount(); row++)
        {
            float y = paddingTop + row * yRatio;
            canvas.drawLine(0F, y,(float)getWidth(),y, linePaint);
        }

        for(int column = 0; column<game.getBoard().getColumnCount(); column++)
        {
            float x = paddingLeft + column * xRatio;
            canvas.drawLine(x,0F,x,height, linePaint);
        }
    }

    private void drawStones(Canvas canvas)
    {
        GameCellState[][] state = game.getBoard().getBoardState();

        for(int row = 0; row<game.getBoard().getRowCount(); row++)
        {
            for(int column = 0; column<game.getBoard().getColumnCount(); column++)
            {
                switch (state[row][column])
                {
                    case BlackStone:
                        drawBlackStone(canvas, row, column);
                        break;
                    case WhiteStone:
                        drawWhiteStone(canvas, row, column);
                        break;
                }
            }
        }
    }

    private void drawBlackStone(Canvas canvas,int row, int column)
    {
        //temp code, need to optimize
        int width = this.getWidth();
        int height = this.getHeight();

        int rows = game.getBoard().getRowCount();
        int columns = game.getBoard().getColumnCount();

        float xRatio = (float)width / columns;
        float yRatio = (float)height / rows;

        float paddingTop = yRatio / 2;
        float paddingLeft = xRatio / 2;

        float y = height - (paddingTop + row * yRatio);
        float x = paddingLeft + column * xRatio;

        float radius = xRatio  * 2/5;

        canvas.drawCircle(x,y, radius, blackPaint);

    }

    private void drawWhiteStone(Canvas canvas,int row, int column)
    {
        //temp code, need to optimize
        int width = this.getWidth();
        int height = this.getHeight();

        int rows = game.getBoard().getRowCount();
        int columns = game.getBoard().getColumnCount();

        float xRatio = (float)width / columns;
        float yRatio = (float)height / rows;

        float paddingTop = yRatio / 2;
        float paddingLeft = xRatio / 2;

        float y = height - (paddingTop + row * yRatio);
        float x = paddingLeft + column * xRatio;

        float radius = xRatio  * 2/5;

        canvas.drawCircle(x,y, radius, whitePaint);

    }

    @Override
    public void onGameBoardChanged(GameBoard sender, OnGameBoardChangeEventArgs args) {

        //process game board state
        this.invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x=event.getX();
                float y= this.getHeight() - event.getY(); //flip the Y coordinate so it matches standard cartesian system

                processTouch(x,y);

        }

        return  true;
    }

    private void processTouch(float x, float y)
    {

        int width = this.getWidth();
        int height = this.getHeight();

        int rows = game.getBoard().getRowCount();
        int columns = game.getBoard().getColumnCount();

        float xRatio = (float)width / columns;
        float yRatio = (float)height / rows;

        float paddingTop = yRatio / 2;
        float paddingLeft = xRatio / 2;



        int column = Math.round((x - paddingLeft)/xRatio);
        int row = Math.round((y-paddingTop)/yRatio);

        //Log.d("MULTIPLE", "X=" + x + " Y=" + y);
        //Log.d("MULTIPLE", "column=" + column + " row=" + row);

        try {



            this.game.getCurrentPlayer().acceptMove(row, column);
        } catch (IllegalMoveException illegalMoveException) {
            illegalMoveException.printStackTrace();
        } catch (MoveNotAllowedException e) {
            e.printStackTrace();
        }

    }
}
