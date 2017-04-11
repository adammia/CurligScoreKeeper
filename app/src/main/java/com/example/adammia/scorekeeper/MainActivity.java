package com.example.adammia.scorekeeper;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {
    final int TOAST_DURATION = Toast.LENGTH_SHORT;
    public final int MAX_END = 9;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;
    CardView cardPoint;
    CustomDialog alert;
    TextView scoreViewR, scoreViewY, setEndViewR, setEndViewY, setOutViewR, setOutViewY;
    private int scoreTeamR, scoreTeamY;
    private int endR, endY, outR, outY;

    boolean gameOverR, gameOverY = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // recovering the instance state
        if (savedInstanceState != null) {
            scoreTeamR = savedInstanceState.getInt(getString(R.string.TRS));
            scoreTeamY = savedInstanceState.getInt(getString(R.string.TYS));
            endR = savedInstanceState.getInt(getString(R.string.TRE));
            endY = savedInstanceState.getInt(getString(R.string.TYE));
            outR = savedInstanceState.getInt(getString(R.string.TRO));
            outY = savedInstanceState.getInt(getString(R.string.TYO));
        } else
            init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.TRS), scoreTeamR);
        outState.putInt(getString(R.string.TYS), scoreTeamY);
        outState.putInt(getString(R.string.TRE), endR);
        outState.putInt(getString(R.string.TYE), endY);
        outState.putInt(getString(R.string.TRO), outR);
        outState.putInt(getString(R.string.TYO), outY);
    }

    //initialize
    public void init() {
        cardPoint = (CardView) findViewById(R.id.card_point);

        scoreViewR = (TextView) findViewById(R.id.team_r_score);
        scoreViewY = (TextView) findViewById(R.id.team_y_score);

        setEndViewR = (TextView) findViewById(R.id.set_View_R);
        setEndViewY = (TextView) findViewById(R.id.set_View_Y);

        setOutViewR = (TextView) findViewById(R.id.set_Out_R);
        setOutViewY = (TextView) findViewById(R.id.set_Out_Y);

        setActionOnScrollUp();
    }

    //Set text on actionBar when scrolling up.
    public void setActionOnScrollUp() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(getString(R.string.title));
        collapsingToolbar.setExpandedTitleColor(ResourcesCompat.getColor(getResources(), android.R.color.transparent, null));
    }

    //Increase the score.

    public void updateScore(Integer score, char team, char scoreType) {
        TextView[][] textViews = {{
                (TextView) findViewById(R.id.team_r_score), // Player Red point types
                (TextView) findViewById(R.id.set_View_R),   // Player Red point types
                (TextView) findViewById(R.id.set_Out_R)   // Player Red point types
        }, {
                (TextView) findViewById(R.id.team_y_score), // Player Yellow point types
                (TextView) findViewById(R.id.set_View_Y),   // Player Yellow point types
                (TextView) findViewById(R.id.set_Out_Y)   // Player Yellow point types
        }};
        Integer row = -1;
        switch (team) {
            case 'r':
                row = 0;
                break;
            case 'y':
                row = 1;
                break;
            default:
                break;
        }
        Integer col = -1;
        switch (scoreType) {
            case 'p':
                col = 0;
                break;
            case 'e':
                col = 1;
                break;
            case 'o':
                col = 2;
            default:
                break;
        }
        if (row > -1 && col > -1) {
            textViews[row][col].setText(String.valueOf(score));
        } else {
            for (TextView[] textView : textViews) {
                for (TextView aTextView : textView) {
                    aTextView.setText("0");
                }
            }
        }
    }

    public void redPoint(View view) {
        if (!gameOverR) {
            if (endY < (MAX_END-1)|| !gameOverY)
                scoreTeamR++;
            updateScore(scoreTeamR, 'r', 'p');
        } else gameOverR = true;
    }

    public void setRedEnd(View view) {
        if (!gameOverR) {
            if (endR < (MAX_END-1))
                endR++;
            updateScore(endR, 'r', 'e');
        } else
            gameOverR = true;
    }

    public void takeOutRed(View view) {
        if (!gameOverY){
            outR = outR + 2;
            updateScore(outR, 'r', 'o');
        }else  gameOverR = true;
    }

    public void yellowPoint(View view) {
        if (!gameOverY){
            scoreTeamY++;
        updateScore(scoreTeamY, 'y', 'p');
    }else  gameOverY = true;
    }

    public void setYellowEnd(View view) {
        if (!gameOverY) {
            if (endY == (MAX_END - 1) && endR == (MAX_END - 1) && scoreTeamR == scoreTeamY) {
                Context context = getApplicationContext();
                String text = getString(R.string.draw);
                Toast toast = makeText(context, text, TOAST_DURATION);
                toast.show();

            } else if (endY < (MAX_END-1)) {
                endY++;
                updateScore(endY, 'y', 'e');


            } else if ((endR == MAX_END-1 && endY == MAX_END-1) || (endR == MAX_END && endY == MAX_END))
            {winner();
                gameOverY = true;
            }}}

    public void takeOutYellow(View view) {
        if (!gameOverY){
            outY = outY + 2;
            updateScore(outY, 'y', 'o');
        }else  gameOverY = true;
    }


    //Winner is..
    public void winner() {
        if (endR == MAX_END - 1 && endY == MAX_END - 1 || endR == MAX_END && endY == MAX_END) {
            if (scoreTeamR > scoreTeamY) {
                alert = new CustomDialog();
                alert.showDialog(MainActivity.this, getString(R.string.go_rwin));
            } else {
                alert = new CustomDialog();
                alert.showDialog(MainActivity.this, getString(R.string.go_ywin));
            }
        }
    }

    // Reset the score for Team A and Team B.
    public void resetScore(View v) {
        scoreTeamR = scoreTeamY = endR = endY = outR = outY = 0;
        gameOverR = gameOverY = false;
        updateScore(0, 'r', 'r');
        Context context = getApplicationContext();
        String text = getString(R.string.new_g);
        Toast toast = makeText(context, text, TOAST_DURATION);
        toast.show();
    }}