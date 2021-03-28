package com.bhadrik.sudoku;

import android.util.Log;

public class Solver {
    private static final short MAXLOOP = 20;

    private short[][] board;
    private short[][] boardBackup;

    private EmptySpace[][] obj;
    private EmptySpace[][] objBackup;

    private short loop;
    private short loopBackup;

    private short empty;
    private short totalEmpty;

    //Index helpers
    private short m = -1, n = -1, value = 0;

    private int selectedRow;
    private int selectedColumn;

    //------------Solver METHODS-----------------
    public  Solver(){
        selectedRow = -1;
        selectedColumn = -1;

        board = new short[][]{
                {(short)0, (short)2, (short)0, (short)0, (short)9, (short)0, (short)8, (short)1, (short)0},
                {(short)3, (short)0, (short)0, (short)8, (short)0, (short)0, (short)9, (short)6, (short)2},
                {(short)0, (short)0, (short)0, (short)2, (short)0, (short)0, (short)0, (short)3, (short)7},
                {(short)0, (short)0, (short)0, (short)7, (short)0, (short)6, (short)0, (short)9, (short)0},
                {(short)1, (short)0, (short)0, (short)0, (short)0, (short)0, (short)7, (short)4, (short)0},
                {(short)4, (short)0, (short)0, (short)1, (short)2, (short)3, (short)0, (short)0, (short)8},
                {(short)0, (short)1, (short)3, (short)4, (short)7, (short)0, (short)5, (short)0, (short)0},
                {(short)0, (short)0, (short)7, (short)0, (short)5, (short)0, (short)3, (short)0, (short)0},
                {(short)2, (short)5, (short)4, (short)6, (short)0, (short)8, (short)1, (short)7, (short)0}
        };

        display();

        obj = new EmptySpace[9][9];

        empty = countEmptyField();
        totalEmpty = empty;
    }

    public void processObj(EmptySpace singleObj){
        Log.i("Solving", "processObj: ENTER");
        short a = 0, b = 0;
        //row
        for (short i = 0; i < 9; i++)
            singleObj.set(true, board[singleObj.get(false, (short)1)][i], (short)0);
        //column
        for (short i = 0; i < 9; i++)
            singleObj.set(true, board[i][singleObj.get(false, (short)2)], (short)0);
        //3x3
        if (singleObj.get(false, (short)1) < 3) a = 0;
        else if (singleObj.get(false, (short)1) < 6) a = 3;
        else if (singleObj.get(false, (short)1) < 9) a = 6;

        if (singleObj.get(false, (short) 2) < 3) b = 0;
        else if (singleObj.get(false, (short)2) < 6) b = 3;
        else if (singleObj.get(false, (short)2) < 9) b = 6;

        short x = (short)(a + 3), y = (short)(b + 3);

        for (short i = a; i < x; i++)
        for (short j = b; j < y; j++) {
            singleObj.set(true, board[i][j], (short)0);
        }

        //One step ahead search
        display();
        singleObj.finalProcess(board);
        Log.w("Solving", "======================AFTER FINAL PROCESS==========================");
        display();
        deepSearch(singleObj, a, b);

        Log.i("Solving", "processObj: EXIT");
    }

    public void deepSearch(EmptySpace singleObj, short a, short b){
        short x = (short)(a + 3), y = (short)(b + 3);
        if (loop > 0)
            for (short l = 1; l < 10; l++)
                if (singleObj.get(true, l) == 1) {
                    boolean possible = true;
                    for (short i = a; i < x; i++) {
                        for (short j = b; j < y; j++) {
                            //    objectNumber != 0      Not same object
                            if (obj[i][j].get(false, (short)3) != 0 && obj[i][j].get(false, (short)3) != singleObj.get(false, (short)3) && !obj[i][j].isFilled()) {
                                obj[i][j].manualProcess(board);
                                if (obj[i][j].get(true, l) != 0) { possible = false; break; }
                            }
                        }
                        if (!possible) break;
                    }
                    if (possible) { singleObj.forceApply(board, l); break; }
                }
    }

    public void athiyo(short p, short q, short value){
        for (short minimum = 2; minimum < 9; minimum++) {
            for (short r = 0; r < 9; r += 3)
                for (short l = 0; l < 9; l += 3) {
                    short x = (short)(r + 3), y = (short)(l + 3);
                    short emptyObj = 0;

                    for (short i = r; i < x; i++)
                        for (short j = l; j < y; j++) {
                            if (!obj[i][j].isFilled()) { emptyObj++; p = i; q = j; }
                            if (emptyObj > minimum) break;
                        }

                    if (emptyObj == minimum)
                        for (short i = 1; i < 10; i++)
                            if (obj[p][q].get(true, i) == 1) {
                                obj[p][q].forceApply(board, i);
                                value = i;
                                return;
                            }
                }
            minimum++;
        }
    }

    public void fix(short m, short n, short value){
        obj[m][n].manualProcess(board);
        for (short i = 1; i < 10; i++)
            if (i != value && obj[m][n].get(true, i) == 1) {
                obj[m][n].forceApply(board, i);
                break;
            }
    }

    public short countEmptyObject(){
        short tmp = 0;
        for (short i = 0; i < 9; i++)
            for (short j = 0; j < 9; j++)
                if (!obj[i][j].isFilled()) tmp++;

        return tmp;
    }

    public short countEmptyField(){
        short tmp = 0;
        for (short i = 0; i < 9; i++)
            for (short j = 0; j < 9; j++)
                if (board[i][j] == 0) tmp++;

        return tmp;
    }

    public boolean solve(){
        Log.i("Solving", "Start solving");

        short k = 1;
        //Setup all objects
        for (short i = 0; i < 9; i++)
            for (short j = 0; j < 9; j++) {
                obj[i][j] = new EmptySpace();
                if (board[i][j] == 0) {
                    obj[i][j].set(false, (short)1, i);	//rowAddress
                    obj[i][j].set(false, (short)2, j);	//columnAddress
                    obj[i][j].set(false, (short)3, k);	//objectNumber
                    k++;
                    Log.i("Solve", "Row:"+obj[i][j].get(false, (short)1) + " Column:"
                            + obj[i][j].get(false, (short) 2) + " Num:" + obj[i][j].get(false, (short)3));
                }
                else {
                    obj[i][j].changeStatus(true);
                    obj[i][j].set(false, (short)3, (short)0);     // objectNumber of already filled number is 0.
                }
            }

        boolean again = true;
        boolean againFix = true;
        short limit = 0;

        Log.i("Solve", "Starting Loop.........................................");

        //Call the processObj until all the
        //blanks are filled and variable "totalEmpty" become 0
        Log.i("Solving", "Jumbing to loooooooooooooooooooooooop");
        Log.i("Solving", "Empty:"+totalEmpty + " "+loop+"<"+MAXLOOP);
        while (totalEmpty != 0 && loop < MAXLOOP) {
            short temp = totalEmpty;

            for (short i = 0; i < 9; i++)
                for (short j = 0; j < 9; j++) {
                    if (!obj[i][j].isFilled()) {
                        Log.i("Solving", "Process object:"+i+", "+j);
                        processObj(obj[i][j]);
                    }
                }

            totalEmpty = countEmptyObject();

            if (temp == totalEmpty) {
                if (again) {
                    loopBackup = (short) (loop - 2);
                    boardBackup = board.clone();
                    objBackup = obj.clone();
                    athiyo(m, n, value);
                    again = false;
                } else if (againFix) {
                    loop = loopBackup;
                    board = boardBackup.clone();
                    obj = objBackup.clone();
                    fix(m, n, value);
                    againFix = false;
                }
                if (limit == 4) {
                    break;
                }
                limit++;
            }
            loop++;
        }
        Log.i("Solving", "----------------------Loop end--------------------------");

        if (loop >= MAXLOOP || totalEmpty != 0) {
            Log.i("Solving", "solve: ================Can't solve===================");
            Log.i("Solving", MAXLOOP + "," + totalEmpty);
            return false;
        }
        else{
            Log.i("Solving", "**************************SOLVED SUCESS*******************************");
            Log.i("Solving", MAXLOOP + "," + totalEmpty);
            return true;
        }
    }

    //--------------UI METHODS-------------------
    public void setNumberPosition(short num){
        if(selectedRow != -1 && selectedColumn != -1){
            if(board[selectedRow-1][selectedColumn-1] == num){
                board[selectedRow-1][selectedColumn-1] = 0;
            }
            else{
                board[selectedRow-1][selectedColumn-1] = num;
            }
        }
    }

    public short[][] getBoard() {
        return board;
    }
    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedColumn() {
        return selectedColumn;
    }

    public void setSelectedColumn(int selectedColumn) {
        this.selectedColumn = selectedColumn;
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    public void display() {
        for(int i=0; i<9; i++){
            StringBuilder str = new StringBuilder();
            int j = 0;
            for(j=0; j<9; j++){
                str.append(Integer.toString(board[i][j]) + " ");
            }
            Log.i("Solving", str.toString());
        }
    }
}
