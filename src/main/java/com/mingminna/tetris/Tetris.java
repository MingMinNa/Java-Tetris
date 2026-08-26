package com.mingminna.tetris;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;

import com.mingminna.tetris.component.Board;
import com.mingminna.tetris.component.Generator;
import com.mingminna.tetris.component.Renderer;
import com.mingminna.tetris.component.Tetromino;
import com.mingminna.tetris.component.TetrominoType;
import com.mingminna.tetris.component.BGMPlayList;

import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;


public class Tetris extends GameApplication {
    
    // Image Resources (textures)
    public static final String iconFilename = "tetris_icon.png";
    public static final String muteFilename = "mute.png";
    public static final String unmuteFilename = "unmute.png";
    
    private ImageView musicButton;
    private Image muteImage, unmuteImage;

    private static final int MAX_LOCK_RESETS = 15;
    private static final double LOCK_DELAY_MS = 500;
    private static final double DAS_DELAY = 170;     // unit: ms
    private static final double ARR = 50;            // unit: ms
 
    private Board board;
    private Generator generator;
    private Renderer renderer;
    private Canvas canvas;
    private GraphicsContext gc;
 
    private Tetromino current;
    private TetrominoType holdType = null;
    private boolean canHold = true;
 
    private int lockResetCount = 0;
    private double fallTimer = 0, lockTimer = 0;
    private double dropInterval = 1000;
 
    private int dasDir = 0;
    private boolean softDropHeld = false;
    private double dasTimer = 0;
 
    private int score = 0, level = 1, lines = 0;
    private boolean gameOver = false, paused = false;
    private boolean started = false;
 
    public static void main(String[] args) 
    {
        launch(args);
    }
 
    @Override
    protected void initSettings(GameSettings settings) 
    {
        settings.setWidth(Renderer.APP_W);
        settings.setHeight(Renderer.APP_H);
        settings.setTitle("Java-Tetris");
        settings.setVersion("");
        settings.setAppIcon(iconFilename);
        settings.setMainMenuEnabled(false);
        settings.setGameMenuEnabled(false);
        settings.setApplicationMode(ApplicationMode.RELEASE);
    }
 
    @Override
    protected void initGame() 
    {
        BGMPlayList.start();
        board = new Board();
        generator = new Generator();
        restart();
        started = false;
    }
 
    @Override
    protected void initUI() 
    {
        canvas = new Canvas(Renderer.APP_W, Renderer.APP_H);
        gc = canvas.getGraphicsContext2D();
        renderer = new Renderer(gc);
        FXGL.getGameScene().addUINode(canvas);
        initMusicButton();
    }

    @Override
    protected void initInput() 
    {
        Input input = FXGL.getInput();
 
        UserAction left = new UserAction("Left") {
            @Override
            protected void onActionBegin() {
                tryMove(-1, 0);
                resetLockOnMove();
                dasDir = -1;
                dasTimer = 0;
            }
 
            @Override
            protected void onAction() {
                if (dasDir == -1) {
                    // 60 FPS => FXGL.tpf() = 0.01666 (sec) = 16.6 ms
                    dasTimer += FXGL.tpf() * 1000;
                    if (dasTimer >= DAS_DELAY) {
                        dasTimer -= ARR;
                        if (tryMove(-1, 0)) resetLockOnMove();
                    }
                }
            }
 
            @Override
            protected void onActionEnd() {
                if (dasDir == -1) dasDir = 0;
            }
        };
        input.addAction(left, KeyCode.LEFT);
 
        UserAction right = new UserAction("Right") {
            @Override
            protected void onActionBegin() {
                tryMove(1, 0);
                resetLockOnMove();
                dasDir = 1;
                dasTimer = 0;
            }
 
            @Override
            protected void onAction() {
                if (dasDir == 1) {
                    dasTimer += FXGL.tpf() * 1000;
                    if (dasTimer >= DAS_DELAY) {
                        dasTimer -= ARR;
                        if (tryMove(1, 0)) resetLockOnMove();
                    }
                }
            }
 
            @Override
            protected void onActionEnd() {
                if (dasDir == 1) dasDir = 0;
            }
        };
        input.addAction(right, KeyCode.RIGHT);
 
        UserAction softDrop = new UserAction("SoftDrop") {
            @Override
            protected void onActionBegin() {
                softDropHeld = true;
            }
 
            @Override
            protected void onActionEnd() {
                softDropHeld = false;
            }
        };
        input.addAction(softDrop, KeyCode.DOWN);
 
        UserAction hardDrop = new UserAction("HardDrop") {
            @Override
            protected void onActionBegin() {
                hardDrop();
            }
        };
        input.addAction(hardDrop, KeyCode.SPACE);
 
        input.addAction(new UserAction("RotateCW_X") {
            @Override
            protected void onActionBegin() {
                tryRotate(1);
            }
        }, KeyCode.X);
        
        input.addAction(new UserAction("RotateCCW_Z") {
            @Override
            protected void onActionBegin() {
                tryRotate(-1);  
            }
        }, KeyCode.Z);
 
        input.addAction(new UserAction("Hold_C") {
            @Override
            protected void onActionBegin() {
                holdPiece();
            }
        }, KeyCode.C);
 
        UserAction pause = new UserAction("Pause") {
            @Override
            protected void onActionBegin() {
                if (started && !gameOver) paused = !paused;
            }
        };
        input.addAction(pause, KeyCode.P);
 
        UserAction start = new UserAction("Start") {
            @Override
            protected void onActionBegin() {
                if (!started) {
                    started = true;
                    restart();
                }
            }
        };
        input.addAction(start, KeyCode.ENTER);
 
        UserAction restart = new UserAction("Restart") {
            @Override
            protected void onActionBegin() {
                restart();
            }
        };
        input.addAction(restart, KeyCode.R);

        UserAction home = new UserAction("Home") {
            @Override
            protected void onActionBegin() {
                initGame();
            }
        };
        input.addAction(home, KeyCode.ESCAPE);
    }
    
    protected void initMusicButton()
    {
        double size = 32;
        double margin = 12;

        muteImage = FXGL.getAssetLoader().loadTexture(muteFilename).getImage();
        unmuteImage = FXGL.getAssetLoader().loadTexture(unmuteFilename).getImage();

        musicButton = new ImageView(BGMPlayList.isMuted() ? muteImage : unmuteImage);
        musicButton.setFitWidth(size);
        musicButton.setFitHeight(size);
        musicButton.setTranslateX(Renderer.APP_W - size - margin);
        musicButton.setTranslateY(margin);
        musicButton.setCursor(Cursor.HAND);

        musicButton.setOnMouseClicked(e -> {
            BGMPlayList.toggleMute();
            musicButton.setImage(BGMPlayList.isMuted() ? muteImage : unmuteImage);
        });

        musicButton.setVisible(true);
        FXGL.getGameScene().addUINode(musicButton);
    }

    @Override
    protected void onUpdate(double tpf) 
    {
        if (started && !gameOver && !paused) {
            double ms = tpf * 1000;
 
            if (current.canMoveDown(board)) {
                fallTimer += ms;
                double interval = softDropHeld ? Math.min(dropInterval, 50) : dropInterval;
                if (fallTimer >= interval) {
                    fallTimer = 0;
                    tryMove(0, 1);
                }
                lockTimer = 0;
            } 
            else {
                lockTimer += ms;
                if (lockTimer >= LOCK_DELAY_MS || lockResetCount > MAX_LOCK_RESETS) {
                    lockPiece();
                    fallTimer = 0;
                    lockTimer = 0;
                    lockResetCount = 0;
                }
            }
        }
        render();
    }

    @Override
    protected void onExit() 
    {
        BGMPlayList.stop();
        System.exit(0);
    }

    private void restart() 
    {
        board.clear();
        generator.reset();
        holdType = null;
        canHold = true;
        score = 0;
        level = 1;
        lines = 0;
        dropInterval = 1000;
        fallTimer = 0;
        lockTimer = 0;
        lockResetCount = 0;
        gameOver = false;
        paused = false;
        dasDir = 0;
        dasTimer = 0;
        spawnPiece();
    }
 
    private void spawnPiece() 
    {
        TetrominoType type = generator.next();
        current = new Tetromino(type, 3, 0);

        fallTimer = 0;
        lockTimer = 0;
        lockResetCount = 0;

        if (board.collides(current.getCells())) {
            gameOver = true;
        }
    }
 
    private boolean tryMove(int dx, int dy) 
    {
        if (!started || gameOver || paused) return false;
        return current.move(dx, dy, board);
    }
 
    private void resetLockOnMove() 
    {
        if (!current.canMoveDown(board)) {
            lockTimer = 0;
            lockResetCount++;
        }
    }
 
    private void tryRotate(int dir) 
    {
        if (!started || gameOver || paused) return;
        if (current.tryRotate(dir, board)) {
            resetLockOnMove();
        }
    }
 
    private void hardDrop() 
    {
        if (!started || gameOver || paused) return;

        while (tryMove(0, 1));
        lockPiece();
        fallTimer = 0;
        lockTimer = 0;
        lockResetCount = 0;
    }
 
    private void holdPiece() 
    {
        if (!started || gameOver || paused || !canHold) return;
        
        if (holdType == null) {
            holdType = current.getType();
            spawnPiece();
        } 
        else {
            TetrominoType temp = holdType;
            holdType = current.getType();
            current = new Tetromino(temp, 3, 0);

            fallTimer = 0;
            lockTimer = 0;
            lockResetCount = 0;

            if (board.collides(current.getCells())) {
                gameOver = true;
            }
        }
        canHold = false;
    }
 
    private void lockPiece() 
    {
        boolean locked = board.lockPiece(current);
        if (!locked) {
            gameOver = true;
            return;
        }

        clearLinesAndScore();
        if (!gameOver) {
            spawnPiece();
            canHold = true;
        }
    }
 
    private void clearLinesAndScore() 
    {
        int cleared = board.clearLines();
        if (cleared == 0) return;
 
        lines += cleared;
        int base = switch (cleared) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 0;
        };

        score += base * level;
        int newLevel = 1 + lines / 7;
        
        if (newLevel != level) {
            level = newLevel;
            dropInterval = Math.max(70, 1000 - (level - 1) * 150);
        }
    }
 
    private void render() 
    {
        if (!started) {
            renderer.renderHomeScreen();
            return;
        }
        
        renderer.render(
            board, current, holdType, 
            generator.getNextQueue(), score, level, lines, 
            canHold, paused, gameOver
        );
    }
}