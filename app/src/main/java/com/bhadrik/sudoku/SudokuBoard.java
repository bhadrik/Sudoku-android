package com.bhadrik.sudoku;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SudokuBoard extends View {
    private final int boardColor;
    private final int thinLineColor;
    private final int cellFillColor;
    private final int cellHighlightColor;
    private final int textColor;
    private final int textSolveColor;

    private final Paint boardColorPaint = new Paint();
    private final Paint cellFillColorPaint = new Paint();
    private final Paint cellHighlightColorPaint = new Paint();
    private final Paint textColorPaint = new Paint();
    private final Rect textPaintBounds = new Rect();

    private final Solver solver = new Solver();

    private int cellSize;

    public SudokuBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SudokuBoard,
                0, 0);
        try{
            boardColor = array.getInt(R.styleable.SudokuBoard_boardColor,0);
            thinLineColor = array.getInt(R.styleable.SudokuBoard_thinLineColor, 0);
            cellFillColor = array.getInt(R.styleable.SudokuBoard_cellFillColor,0);
            cellHighlightColor = array.getInt(R.styleable.SudokuBoard_cellHighlightColor,0);
            textColor = array.getInt(R.styleable.SudokuBoard_textColor, 0);
            textSolveColor = array.getInt(R.styleable.SudokuBoard_textSolveColor, 0);
        }finally {
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int width, int height){
        super.onMeasure(width, height);

        int dimension = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());

        cellSize = dimension / 9;

        setMeasuredDimension(dimension, dimension);
    }

    @Override
    protected  void onDraw(Canvas canvas){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(16);
        boardColorPaint.setColor(boardColor);
        boardColorPaint.setAntiAlias(true);

        cellFillColorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        cellFillColorPaint.setColor(cellFillColor);
        cellFillColorPaint.setStrokeWidth(10);
        cellFillColorPaint.setAntiAlias(true);

        cellHighlightColorPaint.setStyle(Paint.Style.FILL);
        cellHighlightColorPaint.setColor(cellHighlightColor);
        cellHighlightColorPaint.setAntiAlias(true);

        textColorPaint.setStyle(Paint.Style.FILL);
        textColorPaint.setAntiAlias(true);
        textColorPaint.setColor(textColor);

        colorCell(canvas, solver.getSelectedRow(), solver.getSelectedColumn());
        drawBoard(canvas);
        canvas.drawRect(0,0, getWidth(), getHeight(), boardColorPaint);
        drawNumbers(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        boolean isValid = false;

        float x = e.getX();
        float y = e.getY();

        int action = e.getAction();

        if(action == MotionEvent.ACTION_DOWN){
            solver.setSelectedRow((int)Math.ceil(y/cellSize));
            solver.setSelectedColumn((int)Math.ceil(x/cellSize));
            isValid = true;
        }

        return isValid;
    }

    private void drawNumbers(Canvas canvas){
        textColorPaint.setTextSize(cellSize*0.7f);
        for(int r=0; r<9; r++){
            for(int c=0; c<9; c++) {
                if(solver.getBoard()[r][c] != 0){
                    String text = Integer.toString(solver.getBoard()[r][c]);
                    float width, height;

                    textColorPaint.getTextBounds(text, 0 ,text.length(), textPaintBounds);
                    width = textColorPaint.measureText(text);
                    height = textPaintBounds.height();

                    canvas.drawText(text, c*cellSize+(cellSize-width)/2,
                            r*cellSize+cellSize-((cellSize-height)/2), textColorPaint);
                }
            }
        }
    }

    private void colorCell(Canvas canvas, int r, int c){
        if(solver.getSelectedColumn() != -1 && solver.getSelectedRow() != -1){
            canvas.drawRect((c-1)*cellSize, 0, c*cellSize, cellSize*9,
                    cellHighlightColorPaint);
            canvas.drawRect(0, (r-1)*cellSize, 9*cellSize, cellSize*r,
                    cellHighlightColorPaint);
            canvas.drawRect((c-1)*cellSize, (r-1)*cellSize, c*cellSize, cellSize*r,
                    cellFillColorPaint);
        }

        invalidate();
    }

    private void drawThickLine(){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(6);
        boardColorPaint.setColor(boardColor);
    }

    private  void drawThinLine(){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(2);
        boardColorPaint.setColor(thinLineColor);
    }

    private void drawBoard(Canvas canvas){
        for(int i=0; i<10; i++){
            if(i % 3 != 0){
                drawThinLine();
                canvas.drawLine(cellSize*i, 0, cellSize * i, getWidth(), boardColorPaint );
                canvas.drawLine(0,cellSize*i, getWidth(), cellSize * i, boardColorPaint );
            }
        }

        for(int i=0; i<10; i++){
            if(i % 3 == 0){
                drawThickLine();
                canvas.drawLine(cellSize*i, 0, cellSize * i, getWidth(), boardColorPaint );
                canvas.drawLine(0,cellSize*i, getWidth(), cellSize * i, boardColorPaint );
            }
        }
    }

    public Solver getSolver() {
        return solver;
    }
}
