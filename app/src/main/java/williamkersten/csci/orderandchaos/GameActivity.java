/* * * * * * * * * * * * * * * * * * *
 * William Kersten                   *
 * CSCI 4010                         *
 * Assignment 5 - Order and Chaos    *
 * November, 2017                    *
 * * * * * * * * * * * * * * * * * * */
package williamkersten.csci.orderandchaos;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity {
    private int numSquaresEmpty = 36;   //the number of squares on the board
    //The listeners for every grid section and id's of all of the gridListeners sections
    //  we need the listeners since they hold information on what image is in each grid
    private GridButtonListener gridListeners[][];
    private int gridIds[][] = {
            {R.id.r1c1, R.id.r1c2, R.id.r1c3, R.id.r1c4, R.id.r1c5, R.id.r1c6},
            {R.id.r2c1, R.id.r2c2, R.id.r2c3, R.id.r2c4, R.id.r2c5, R.id.r2c6},
            {R.id.r3c1, R.id.r3c2, R.id.r3c3, R.id.r3c4, R.id.r3c5, R.id.r3c6},
            {R.id.r4c1, R.id.r4c2, R.id.r4c3, R.id.r4c4, R.id.r4c5, R.id.r4c6},
            {R.id.r5c1, R.id.r5c2, R.id.r5c3, R.id.r5c4, R.id.r5c5, R.id.r5c6},
            {R.id.r6c1, R.id.r6c2, R.id.r6c3, R.id.r6c4, R.id.r6c5, R.id.r6c6}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button x = (Button) findViewById(R.id.x_button);
        Button o = (Button) findViewById(R.id.o_button);
        int buttonSelectedColor = getResources().getColor(R.color.buttonSelected);
        int buttonNormalColor = getResources().getColor(R.color.buttonNormal);

        x.setOnClickListener(new XOButtonListener(x, o, buttonSelectedColor, buttonNormalColor));
        o.setOnClickListener(new XOButtonListener(o, x, buttonSelectedColor, buttonNormalColor));

        //initialize every imageview in the gridListeners to have an onClickListener
        gridListeners = new GridButtonListener[6][6];
        for(int i = 0; i < gridListeners.length; i++){
            for (int j = 0; j < gridListeners[i].length; j++){
                ImageView imageview = findViewById(gridIds[i][j]);
                gridListeners[i][j] = new GridButtonListener(i, j);
                imageview.setOnClickListener(gridListeners[i][j]);
            }
        }

        //initialize buttons in overlay
        //play again button simply calls recreate on the activity
        Button playAgain = (Button) findViewById(R.id.playAgain_button);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        //quit to menu calls finish
        Button quitToMenu = (Button) findViewById(R.id.quit_button);
        quitToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private class GridButtonListener implements View.OnClickListener{
        private int imageIdInSquare;
        private int row, col;

        //initialize the buttons to have blank as their background image
        public GridButtonListener(int row, int col){
            this.imageIdInSquare = R.drawable.blank;
            this.row = row;
            this.col = col;
        }

        //Getters
        public int getImageIdInSquare(){
            return imageIdInSquare;
        }
        public int getRow() {return row;}
        public int getCol() {return col;}

        @Override
        public void onClick(View view) {
            if (imageIdInSquare != R.drawable.blank){
                //make toast that you can't put a thing here
                Toast.makeText(getApplicationContext(),
                        "This spot already has a piece in it!",
                        Toast.LENGTH_SHORT).show();
            } else {
                //we just filled a square, so subtract from the number of squares left.
                numSquaresEmpty--;

                //swap the turns
                TextView playerText = (TextView) findViewById(R.id.playerTurn_textview);
                if (playerText.getText().toString().equals("Order's Turn")){
                    playerText.setText("Chaos's Turn");
                } else {
                    playerText.setText("Order's Turn");
                }

                //make image in square the currently selected piece
                if(XOButtonListener.getPieceSelected() == R.id.x_button){
                    imageIdInSquare = R.drawable.x;
                } else if (XOButtonListener.getPieceSelected() == R.id.o_button){
                    imageIdInSquare = R.drawable.o;
                } else {
                    throw new RuntimeException("Could not figure out which piece is selected!");
                }
                view.setBackgroundResource(imageIdInSquare);

                //check if either player won
                if (orderWon(row, col)){
                    displayWinner(ORDER_WON);
                } else if (numSquaresEmpty == 0){
                    displayWinner(CHAOS_WON);
                }
            }
        }
    }


    //Check to see if there are any sets of five in a row.
    private boolean orderWon(int row, int col) {
        //check rows
        if (checkLineInGrid(row, row, 0, 5)) {
            Log.i("ORDERWON", "Won with row check one");
            return true;
        }

        //Check columns
        if (checkLineInGrid(0, 5, col, col)) {
            Log.i("ORDERWON", "Won with column check one");
            return true;
        }

        //top left to bottom right Diags
        //middle diag
        int diag = row - col;
        if (diag == 0 && checkDiagInGrid(0, 5, 0, 5)) {
            Log.i("ORDERWON", "Won with left diag check one");
            return true;
        }
        //upper diag
        if (diag == -1 && checkDiagInGrid(0, 4, 1, 5)) {
            Log.i("ORDERWON", "Won with left diag check three");
            return true;
        }
        //lower diag
        if (diag == 1 && checkDiagInGrid(1, 5, 0, 5)) {
            Log.i("ORDERWON", "Won with left diag check two");
            return true;
        }

        //check diags starting in top right and ending in bottom left
        //middle diag
        if (checkDiagInGrid(0, 5, 5, 0)) {
            Log.i("ORDERWON", "Won with right diag check one");
            return true;
        }
        //upper diag
        if (checkDiagInGrid(0, 4, 4, 0)) {
            Log.i("ORDERWON", "Won with right diag check two");
            return true;
        }
        //lower diag
        if (checkDiagInGrid(1, 5, 5, 0)) {
            Log.i("ORDERWON", "Won with right diag check three");
            return true;
        }

        return false;
    }

    //Basically, pass in the row you wanna start at, col you wanna start at, and check if
    // there's five in a row. Idea is that for checking cols, startCol and endCol are the same
    // while row changes, for checking rows startRow and endRow are the same while cols change.
    private boolean checkLineInGrid(int startRow, int endRow, int startCol, int endCol){
        int prevImage = gridListeners[startRow][startCol].getImageIdInSquare();
        int numInOrder = 0;

        for (int i = startRow; i <= endRow; i++){
            for (int j = startCol; j <= endCol; j++) {
                if (gridListeners[i][j].getImageIdInSquare() == R.drawable.blank) {
                    numInOrder = 0;
                } else if (gridListeners[i][j].getImageIdInSquare() == prevImage){
                    numInOrder++;
                    //Log.i("CHECKLINEINGRID", "incremented numInOrder: is now " + numInOrder);
                    if (numInOrder >= 5) return true;
                }else {
                    numInOrder = 1;
                    //Log.i("CHECKLINEINGRID", "reset NumInOrder");
                    prevImage = gridListeners[i][j].getImageIdInSquare();
                }
            }
        }

        if (numInOrder >= 5)    return true;
        else                    return false;
    }

    //same as checkLineInGrid, but uses one for loop to be able to effectively go through the diagonals.
    private boolean checkDiagInGrid(int startRow, int endRow, int startCol, int endCol){
        int prevImage = gridListeners[startRow][startCol].getImageIdInSquare();
        int numInOrder = 0;

        for (int i = startRow, j = startCol; i <= endRow; i++, j += (startCol > endCol ? -1 : 1)){

            if (gridListeners[i][j].getImageIdInSquare() == R.drawable.blank) {
                numInOrder = 0;
            } else if (gridListeners[i][j].getImageIdInSquare() == prevImage){
                numInOrder++;
                //Log.i("CHECKLINEINGRID", "incremented numInOrder: is now " + numInOrder);
                if (numInOrder >= 5) return true;
            }else {
                numInOrder = 1;
                //Log.i("CHECKLINEINGRID", "reset NumInOrder");
                prevImage = gridListeners[i][j].getImageIdInSquare();
            }
        }

        if (numInOrder >= 5)    return true;
        else                    return false;
    }

    private static final int ORDER_WON = 1;
    private static final int CHAOS_WON = 2;
    private void displayWinner(int winner){
        RelativeLayout overlay = (RelativeLayout) findViewById(R.id.overlayLayout);
        TextView winnerText = (TextView) findViewById(R.id.winner_textview);

        String winnerString = "Should be initialized";
        if (winner == ORDER_WON){
            winnerString = getResources().getString(R.string.orderWins);
        }
        else if (winner == CHAOS_WON){
            winnerString = getResources().getString(R.string.chaosWins);
        }
        winnerText.setText(winnerString);

        overlay.setVisibility(View.VISIBLE);
    }
}

//-------------------------------------------------------------
class XOButtonListener implements View.OnClickListener{
    private Button thisButton, otherButton;
    private int buttonSelected, buttonNormal;
    private static int pieceSelected = 0;

    /**
     * @param thisButton the button the listener is applied to
     * @param otherButton the other button that can be selected
     * @param buttonSelected the color to make this button when it's selected
     * @param buttonNormal the color to make the other button when it's selected
     */
    public XOButtonListener(Button thisButton, Button otherButton, int buttonSelected, int buttonNormal){
        this.thisButton = thisButton;
        this.otherButton = otherButton;
        this.buttonSelected = buttonSelected;
        this.buttonNormal = buttonNormal;
        onClick(null);
    }

    public static int getPieceSelected(){
        return pieceSelected;
    }

    @Override
    public void onClick(View view){
        thisButton.setBackgroundTintList(ColorStateList.valueOf(buttonSelected));
        pieceSelected = thisButton.getId();
        otherButton.setBackgroundTintList(ColorStateList.valueOf(buttonNormal));
    }
}