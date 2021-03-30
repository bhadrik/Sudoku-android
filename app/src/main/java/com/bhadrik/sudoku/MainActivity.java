package com.bhadrik.sudoku;

import androidx.appcompat.app.AppCompatActivity;

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
        if(gameBoardSolver.solve()){
            status.setText("Solved successfully");
        }
        else{
            status.setText("Unable to solve");
        }
        gameBoard.invalidate();
    }

    public void BTNClearPress(View view){
        gameBoardSolver.clear();
    }

}