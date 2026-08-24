package com.mingminna.tetris.component;


public class Board {

    public static final int COLS = 10;
    public static final int ROWS = 20;

    private final TetrominoType[][] grid 
        = new TetrominoType[ROWS][COLS];

    public void clear() 
    {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                grid[r][c] = null;
            }
        }
    }

    public TetrominoType get(int row, int col) 
    {
        return grid[row][col];
    }

    public boolean collides(int[][] cells) 
    {
        for (int[] cell : cells) {
            int x = cell[0];
            int y = cell[1];
            if (x < 0 || x >= COLS || y >= ROWS) return true;
            if (y >= 0 && grid[y][x] != null)    return true;
        }
        return false;
    }

    public boolean lockPiece(Tetromino piece) 
    {
        for (int[] cell : piece.getCells()) {
            int x = cell[0];
            int y = cell[1];
            if (y < 0) {
                return false;
            }
            grid[y][x] = piece.getType();
        }
        return true;
    }

    public int clearLines() 
    {
        int cleared = 0;
        for (int r = ROWS - 1; r >= 0; r--) {
            if (isRowFull(r)) {
                removeRow(r);
                cleared++;
                r++;
            }
        }
        return cleared;
    }

    private boolean isRowFull(int row) 
    {
        for (int c = 0; c < COLS; c++) {
            if (grid[row][c] == null) return false;
        }
        return true;
    }

    private void removeRow(int row) 
    {
        for (int r = row; r > 0; r--) {
            grid[r] = grid[r - 1].clone();
        }
        grid[0] = new TetrominoType[COLS];
    }
}