package com.mingminna.tetris.component;

import javafx.scene.paint.Color;


public enum TetrominoType {

    I(
        new int[][][]{
                {{0, 1}, {1, 1}, {2, 1}, {3, 1}}, // 0: Spawn State
                {{2, 0}, {2, 1}, {2, 2}, {2, 3}}, // R: Right
                {{0, 2}, {1, 2}, {2, 2}, {3, 2}}, // 2: 180 Degrees
                {{1, 0}, {1, 1}, {1, 2}, {1, 3}}  // L: Left
        },
        Color.CYAN,
        true
    ),
    O(
        new int[][][]{
                {{1, 0}, {2, 0}, {1, 1}, {2, 1}},
                {{1, 0}, {2, 0}, {1, 1}, {2, 1}},
                {{1, 0}, {2, 0}, {1, 1}, {2, 1}},
                {{1, 0}, {2, 0}, {1, 1}, {2, 1}}
        },
        Color.GOLD,
        false
    ),
    T(
        new int[][][]{
                {{1, 0}, {0, 1}, {1, 1}, {2, 1}},
                {{1, 0}, {1, 1}, {2, 1}, {1, 2}},
                {{0, 1}, {1, 1}, {2, 1}, {1, 2}},
                {{1, 0}, {0, 1}, {1, 1}, {1, 2}}
        },
        Color.MEDIUMPURPLE,
        false
    ),
    S(
        new int[][][]{
                {{1, 0}, {2, 0}, {0, 1}, {1, 1}},
                {{1, 0}, {1, 1}, {2, 1}, {2, 2}},
                {{1, 1}, {2, 1}, {0, 2}, {1, 2}},
                {{0, 0}, {0, 1}, {1, 1}, {1, 2}}
        },
        Color.LIMEGREEN,
        false
    ),
    Z(
        new int[][][]{
                {{0, 0}, {1, 0}, {1, 1}, {2, 1}},
                {{2, 0}, {1, 1}, {2, 1}, {1, 2}},
                {{0, 1}, {1, 1}, {1, 2}, {2, 2}},
                {{1, 0}, {0, 1}, {1, 1}, {0, 2}}
        },
        Color.CRIMSON,
        false
    ),
    J(
        new int[][][]{
                {{0, 0}, {0, 1}, {1, 1}, {2, 1}},
                {{1, 0}, {2, 0}, {1, 1}, {1, 2}},
                {{0, 1}, {1, 1}, {2, 1}, {2, 2}},
                {{1, 0}, {1, 1}, {0, 2}, {1, 2}}
        },
        Color.DODGERBLUE,
        false
    ),
    L(
        new int[][][]{
                {{2, 0}, {0, 1}, {1, 1}, {2, 1}},
                {{1, 0}, {1, 1}, {1, 2}, {2, 2}},
                {{0, 1}, {1, 1}, {2, 1}, {0, 2}},
                {{0, 0}, {1, 0}, {1, 1}, {1, 2}}
        },
        Color.DARKORANGE,
        false
    );

    private final int[][][] shapes;
    private final Color color;
    private final boolean iType;

    TetrominoType(int[][][] shapes, Color color, boolean iType) 
    {
        this.shapes = shapes;
        this.color = color;
        this.iType = iType;
    }

    public int[][] getCells(int rotState) 
    {
        return shapes[rotState];
    }

    public Color getColor() 
    {
        return color;
    }

    public boolean isIType() 
    {
        return iType;
    }
}