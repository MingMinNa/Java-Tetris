package com.mingminna.tetris.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.Deque;


public class Renderer {

    public static final int CELL = 30;
    public static final int BOARD_X = 30;
    public static final int BOARD_Y = 30;
    public static final int APP_W = 600;
    public static final int APP_H = 680;
    public static final int SIDE_X = BOARD_X + Board.COLS * CELL + 30;

    private final GraphicsContext gc;

    public Renderer(GraphicsContext gc) 
    {
        this.gc = gc;
    }

    public void renderTitleScreen() 
    {
        gc.setTextAlign(TextAlignment.CENTER);

        gc.setFill(Color.rgb(15, 15, 25));
        gc.fillRect(0, 0, APP_W, APP_H);

        gc.setFill(Color.CYAN);
        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 60));
        gc.fillText("TETRIS", APP_W / 2.0, 150);

        TetrominoType[] allTypes = TetrominoType.values();
        double bx = APP_W / 2.0 - (allTypes.length * 36) / 2.0;
        for (TetrominoType t : allTypes) {
            gc.setFill(t.getColor());
            gc.fillRect(bx, 180, 30, 30);
            bx += 36;
        }

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 24));
        gc.fillText("Press ENTER to Start", APP_W / 2.0, 270);

        gc.setFont(Font.font("Consolas", FontWeight.NORMAL, 15));
        String[] lines = {
                "ESC: Return to home",
                "Left / Right: Move",
                "Down: Soft Drop",
                "Space: Hard Drop",
                "Z: Rotate CCW",
                "X: Rotate CW ",
                "C: Hold      ",
                "P: Pause     ",
                "R: Restart   "
        };
        double ty = 330;
        for (String line : lines) {
            gc.fillText(line, APP_W / 2.0, ty);
            ty += 26;
        }
    }

    public void render(
        Board board, Tetromino current, TetrominoType holdType,
        Deque<TetrominoType> nextQueue, int score, int level, int lines,
        boolean canHold, boolean paused, boolean gameOver
    ) {
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFill(Color.rgb(20, 20, 30));
        gc.fillRect(0, 0, APP_W, APP_H);

        gc.setFill(Color.rgb(10, 10, 15));
        gc.fillRect(BOARD_X, BOARD_Y, Board.COLS * CELL, Board.ROWS * CELL);

        gc.setStroke(Color.rgb(45, 45, 55));
        gc.setLineWidth(1);

        for (int c = 0; c <= Board.COLS; c++) {
            gc.strokeLine(
                BOARD_X + c * CELL, 
                BOARD_Y, 
                BOARD_X + c * CELL, 
                BOARD_Y + Board.ROWS * CELL
            );
        }
        for (int r = 0; r <= Board.ROWS; r++) {
            gc.strokeLine(
                BOARD_X, 
                BOARD_Y + r * CELL, 
                BOARD_X + Board.COLS * CELL, 
                BOARD_Y + r * CELL
            );
        }

        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {
                TetrominoType t = board.get(r, c);
                if (t != null) drawCell(c, r, t.getColor());
            }
        }

        if (!gameOver && current != null) {
            int gy = current.computeGhostY(board);
            for (int[] off : current.getType().getCells(current.getRotState())) {
                int x = current.getX() + off[0];
                int y = gy + off[1];
                if (y >= 0) drawGhostCell(x, y, current.getType().getColor());
            }
            for (int[] cell : current.getCells()) {
                if (cell[1] >= 0) drawCell(cell[0], cell[1], current.getType().getColor());
            }
        }

        drawSidebar(holdType, nextQueue, score, level, lines, canHold);

        if (paused) overlay("PAUSED\nPress P to Resume");
        if (gameOver) overlay("GAME OVER\nPress R to Restart\nPress ESC to Return");
    }

    private void drawCell(int c, int r, Color color) 
    {
        gc.setFill(color);
        gc.fillRect(
            BOARD_X + c * CELL + 1, 
            BOARD_Y + r * CELL + 1, 
            CELL - 2, 
            CELL - 2
        );
    }

    private void drawGhostCell(int c, int r, Color color) 
    {
        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.strokeRect(BOARD_X + c * CELL + 2, BOARD_Y + r * CELL + 2, CELL - 4, CELL - 4);
    }

    private void drawSidebar(
        TetrominoType holdType, Deque<TetrominoType> nextQueue,
        int score, int level, int lines, boolean canHold
    ) {
        drawText("TETRIS", SIDE_X, 55, 26, true);

        drawText("Score", SIDE_X, 95, 14, false);
        drawText(String.valueOf(score), SIDE_X, 118, 20, true);

        drawText("Level: " + level, SIDE_X, 150, 14, false);
        drawText("Lines: " + lines, SIDE_X, 172, 14, false);

        drawText("Hold", SIDE_X, 205, 14, false);
        drawMiniBox(SIDE_X, 215, 90, 90);
        if (holdType != null) drawMiniPiece(holdType, SIDE_X, 215, 90, 90, !canHold);

        drawText("Next", SIDE_X, 335, 14, false);
        int ny = 345;
        int count = 0;
        for (TetrominoType t : nextQueue) {
            if (count >= 3) break;
            drawMiniBox(SIDE_X, ny, 90, 90);
            drawMiniPiece(t, SIDE_X, ny, 90, 90, false);
            ny += 100;
            count++;
        }
    }

    private void drawMiniBox(double x, double y, double w, double h) 
    {
        gc.setFill(Color.rgb(15, 15, 22));
        gc.fillRect(x, y, w, h);
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        gc.strokeRect(x, y, w, h);
    }

    private void drawMiniPiece(
        TetrominoType type, double boxX, double boxY, 
        double boxW, double boxH, boolean gray
    ) {
        double cell = 18;
        double offX = boxX + (boxW - 4 * cell) / 2.0;
        double offY = boxY + (boxH - 4 * cell) / 2.0;
        gc.setFill(gray ? Color.rgb(90, 90, 90) : type.getColor());

        for (int[] off : type.getCells(0)) {
            gc.fillRect(
                offX + off[0] * cell, 
                offY + off[1] * cell, 
                cell - 2,
                cell - 2
            );
        }
    }

    private void drawText(String s, double x, double y, double size, boolean bold) 
    {
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Consolas", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        gc.fillText(s, x, y);
    }

    private void overlay(String text) 
    {
        gc.setFill(Color.rgb(0, 0, 0, 0.65));
        gc.fillRect(BOARD_X, BOARD_Y, Board.COLS * CELL, Board.ROWS * CELL);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 26));
        gc.setTextAlign(TextAlignment.CENTER);

        String[] parts = text.split("\n");
        double ty = BOARD_Y + Board.ROWS * CELL / 2.0 - (parts.length * 30) / 2.0;

        for (String line : parts) {
            gc.fillText(line, BOARD_X + Board.COLS * CELL / 2.0, ty);
            ty += 34;
        }

        gc.setTextAlign(TextAlignment.LEFT);
    }
}