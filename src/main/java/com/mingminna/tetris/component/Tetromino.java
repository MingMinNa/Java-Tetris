package com.mingminna.tetris.component;

import java.util.HashMap;
import java.util.Map;


public class Tetromino {

    private static final String[] ROT_NAME = {"0", "R", "2", "L"};

    // SRS wall kick table
    private static final Map<String, int[][]> JLSTZ_KICKS = new HashMap<>();
    private static final Map<String, int[][]> I_KICKS     = new HashMap<>();

    static {
        // JLSTZ kick table
        JLSTZ_KICKS.put("0R", new int[][]{{0, 0}, {-1, 0}, {-1, -1}, {0,  2}, {-1,  2}});
        JLSTZ_KICKS.put("R0", new int[][]{{0, 0}, { 1, 0}, { 1,  1}, {0, -2}, { 1, -2}});
        JLSTZ_KICKS.put("R2", new int[][]{{0, 0}, { 1, 0}, { 1,  1}, {0, -2}, { 1, -2}});
        JLSTZ_KICKS.put("2R", new int[][]{{0, 0}, {-1, 0}, {-1, -1}, {0,  2}, {-1,  2}});
        JLSTZ_KICKS.put("2L", new int[][]{{0, 0}, { 1, 0}, { 1, -1}, {0,  2}, { 1,  2}});
        JLSTZ_KICKS.put("L2", new int[][]{{0, 0}, {-1, 0}, {-1,  1}, {0, -2}, {-1, -2}});
        JLSTZ_KICKS.put("L0", new int[][]{{0, 0}, {-1, 0}, {-1,  1}, {0, -2}, {-1, -2}});
        JLSTZ_KICKS.put("0L", new int[][]{{0, 0}, { 1, 0}, { 1, -1}, {0,  2}, { 1,  2}});

        // I kick table
        I_KICKS.put("0R", new int[][]{{0, 0}, {-2, 0}, { 1, 0}, {-2,  1}, { 1, -2}});
        I_KICKS.put("R0", new int[][]{{0, 0}, { 2, 0}, {-1, 0}, { 2, -1}, {-1,  2}});
        I_KICKS.put("R2", new int[][]{{0, 0}, {-1, 0}, { 2, 0}, {-1, -2}, { 2,  1}});
        I_KICKS.put("2R", new int[][]{{0, 0}, { 1, 0}, {-2, 0}, { 1,  2}, {-2, -1}});
        I_KICKS.put("2L", new int[][]{{0, 0}, { 2, 0}, {-1, 0}, { 2, -1}, {-1,  2}});
        I_KICKS.put("L2", new int[][]{{0, 0}, {-2, 0}, { 1, 0}, {-2,  1}, { 1, -2}});
        I_KICKS.put("L0", new int[][]{{0, 0}, { 1, 0}, {-2, 0}, { 1,  2}, {-2, -1}});
        I_KICKS.put("0L", new int[][]{{0, 0}, {-1, 0}, { 2, 0}, {-1, -2}, { 2,  1}});
    }

    private final TetrominoType type;
    private int rotState;
    private int x, y;

    public Tetromino(TetrominoType type, int x, int y) 
    {
        this.type = type;
        this.rotState = 0;
        this.x = x;
        this.y = y;
    }

    public TetrominoType getType() 
    {
        return type;
    }

    public int getRotState() 
    {
        return rotState;
    }

    public int getX() 
    {
        return x;
    }

    public int getY() 
    {
        return y;
    }

    public int[][] getCells() 
    {
        return getCellsAt(rotState, x, y);
    }

    public int[][] getCellsAt(int rot, int cx, int cy) 
    {
        int[][] offsets = type.getCells(rot);
        int[][] cells = new int[offsets.length][2];

        for (int i = 0; i < offsets.length; i++) {
            cells[i][0] = cx + offsets[i][0];
            cells[i][1] = cy + offsets[i][1];
        }
        return cells;
    }

    public boolean move(int dx, int dy, Board board) 
    {
        int nx = x + dx;
        int ny = y + dy;

        if (board.collides(getCellsAt(rotState, nx, ny))) 
            return false;

        x = nx;
        y = ny;

        return true;
    }

    public boolean canMoveDown(Board board) 
    {
        return !board.collides(getCellsAt(rotState, x, y + 1));
    }

    public boolean tryRotate(int dir, Board board) 
    {
        int newRot = (rotState + dir + 4) % 4;

        if (type == TetrominoType.O) {
            rotState = newRot;
            return true;
        }

        String key = ROT_NAME[rotState] + ROT_NAME[newRot];
        Map<String, int[][]> table = type.isIType() ? I_KICKS : JLSTZ_KICKS;
        int[][] kicks = table.get(key);
        if (kicks == null) return false;

        for (int[] k : kicks) {
            int nx = x + k[0];
            int ny = y + k[1];
            if (!board.collides(getCellsAt(newRot, nx, ny))) {
                x = nx;
                y = ny;
                rotState = newRot;
                return true;
            }
        }
        return false;
    }

    public int computeGhostY(Board board) 
    {
        int gy = y;
        while (!board.collides(getCellsAt(rotState, x, gy + 1))) gy++;
        return gy;
    }
}