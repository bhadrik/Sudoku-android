package com.bhadrik.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private  SudokuBoard gameBoard;
    private Solver gameBoardSolver;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameBoard = findViewById(R.id.sudokuBoard);
        gameBoardSolver = gameBoard.getSolver();
        status = findViewById(R.id.Status);
    }

    public void BTNOnePressOne(View view){
        gameBoardSolver.setNumberPosition((short)1);
        gameBoard.invalidate();
    }
    public void BTNOnePressTwo(View view){
        gameBoardSolver.setNumberPosition((short)2);
        gameBoard.invalidate();
    }
    public void BTNOnePressThree(View view){
        gameBoardSolver.setNumberPosition((short)3);
        gameBoard.invalidate();
    }
    public void BTNOnePressFour(View view){
        gameBoardSolver.setNumberPosition((short)4);
        gameBoard.invalidate();
    }
    public void BTNOnePressFive(View view){
        gameBoardSolver.setNumberPosition((short)5);
        gameBoard.invalidate();
    }
    public void BTNOnePressSix(View view){
        gameBoardSolver.setNumberPosition((short)6);
        gameBoard.invalidate();
    }
    public void BTNOnePressSeven(View view){
        gameBoardSolver.setNumberPosition((short)7);
        gameBoard.invalidate();
    }
    public void BTNOnePressEight(View view){
        gameBoardSolver.setNumberPosition((short)8);
        gameBoard.invalidate();
    }
    public void BTNOnePressNine(View view){
        gameBoardSolver.setNumberPosition((short) 9);
        gameBoard.invalidate();
    }
    public void BTNSolvePress(View view){
        status.setTextColor(Color.parseColor("#FFC107"));

        switch (gameBoardSolver.validate()){
            case 0:
                if(gameBoardSolver.solve()){
                    status.setTextColor(Color.parseColor("#72E657"));
                    status.setText("Solved successfully");
                }
                else{
                    status.setTextColor(Color.parseColor("#DF3636"));
                    status.setText("Unable to solve");
                }
                break;
            case 1:
                status.setText("Row validation failed");
                break;
            case 2:
                status.setText("Column validation failed");
                break;
            case 3:
                status.setText("Box validation failed");
                break;
        }

        gameBoard.invalidate();
    }

    public void BTNClearPress(View view){
        gameBoardSolver.clear();
    }

}