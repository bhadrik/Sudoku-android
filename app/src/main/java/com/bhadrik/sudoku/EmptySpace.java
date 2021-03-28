package com.bhadrik.sudoku;

public class EmptySpace {
    private short objectNum;
    private short possibleNum;
    private short applicableNum;
    private short rowAddress;
    private short columnAddress;

    private boolean status;
    private boolean[] mask;

    private void countPossibleNum(){
        possibleNum = 0;
        for (short i = 1; i < 10; i++) {
            if (mask[i]) {
                possibleNum++;
                applicableNum = i;
            }
            if (possibleNum != 1)
                applicableNum = 0;
        }
    }

    private void apply(short[][] sudoku){
        sudoku[rowAddress][columnAddress] = applicableNum;
        status = true;
    }

    public EmptySpace(){
        objectNum = 0;
        possibleNum = 0;
        applicableNum = 0;
        rowAddress = 0;
        columnAddress = 0;

        mask = new boolean[] {false, true, true, true, true, true, true, true, true, true};
        status = false;
    }

    public void finalProcess(short[][] sudoku){
        countPossibleNum();
        if(possibleNum == 1){
            status = true;
            apply(sudoku);
        }
        else{
            applicableNum = 0;
        }
    }

    public short get(boolean key, short index){
        if (!key)
            switch (index) {
                case 1: return rowAddress;              //(0,1)
                case 2: return columnAddress;           //(0,2)
                case 3: return objectNum;               //(0,3)
                case 4: return possibleNum;             //(0,4)
                case 5: return applicableNum;           //(0,5)
            }
        else if (mask[index]) return 1;
        else return 0;
        return  0;
    }

    public void set(boolean key, short index, short data){
        //(1,1-9,D)
        if (!key)
            switch (index) {
                case 1: rowAddress = data; break;      //(0,1,D)
                case 2: columnAddress = data; break;   //(0,2,D)
                case 3: objectNum = data; break;       //(0,3,D)
                case 4: possibleNum = data; break;     //(0,4,D)
                case 5: applicableNum = data; break;   //(0,5,D)
            }
        else mask[index] = data != 0;
    }

    public boolean isFilled(){
        return status;
    }

    public void changeStatus(boolean newStatus){
        status = newStatus;
    }

    public void forceApply(short[][] sudoku, short forceApplicableNumber){
        applicableNum = forceApplicableNumber;
        apply(sudoku);
    }

    public void manualProcess(short sudoku[][]){
        short a = 0, b = 0;
        //row
        for (short i = 0; i < 9; i++)
            set(true, sudoku[get(false, (short) 1)][i], (short) 0);
        //column
        for (short i = 0; i < 9; i++) set(true, sudoku[i][get(false, (short) 2)], (short)0);
        //3x3
        if (get(false, (short)1) < 3) a = 0; else if (get(false, (short)1) < 6) a = 3; else if (get(false, (short)1) < 9) a = 6;
        if (get(false, (short)2) < 3) b = 0; else if (get(false, (short)2) < 6) b = 3; else if (get(false, (short)2) < 9) b = 6;
        short x = (short)(a + 3);
        short y = (short)(b + 3);

        for (short i = a; i < x; i++)
            for (short j = b; j < y; j++)
                set(true, sudoku[i][j], (short) 0);
    }

}
