import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    // === GAME ===
    private Timer timer;
    private Bird bird;
    private boolean gameOver = false;

    private int score = 0;
    private int highScore = 0;
    private boolean highScoreBeatenThisGame = false;

    // === FILE ===
    private static final String FILE_NAME = "highscore.txt";

    // === BACKGROUND ===
    private int bgX1 = 0;
    private int bgX2 = 400;
    private static final int BG_SPEED = 1;
    private BufferedImage backgroundSnow;


    // === HIGHSCORE ANIMATION ===
    private boolean newHighScore = false;
    private int highScoreTimer = 0;
    private static final int HIGHSCORE_DURATION = 100;

    private final Color[] rainbow = {
            Color.RED, Color.ORANGE, Color.YELLOW,
            Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA
    };
    private int rainbowIndex = 0;

    // === OBJECTS ===
    private final ArrayList<Pipe> pipes = new ArrayList<>();

    // === IMAGES ===
    private BufferedImage background;
    private BufferedImage birdImage;
    private BufferedImage pipeImage;
    private BufferedImage pipeSnowImage;
    private static final int NORMAL_GAP = 220;
    private static final int SNOW_GAP = 160;
    private boolean snowMode = false;



    // =========================

    public GamePanel() {
        setPreferredSize(new Dimension(400, 600));
        setFocusable(true);
        addKeyListener(this);

        loadImages();
        loadHighScore();

        bird = new Bird(birdImage);
        spawnPipe();

        timer = new Timer(20, this);
        timer.start();
    }

    // === GAME LOOP ===

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            updateBackground();
            bird.update();
            updatePipes();
            checkOutOfBounds();
        }

        updateHighScoreAnimation();
        repaint();
    }

    // === UPDATE ===

    private void updateBackground() {
        bgX1 -= BG_SPEED;
        bgX2 -= BG_SPEED;

        if (bgX1 + 400 <= 0) bgX1 = bgX2 + 400;
        if (bgX2 + 400 <= 0) bgX2 = bgX1 + 400;
    }

    private void updatePipes() {
        for (Pipe pipe : pipes) {
            pipe.update();

            if (pipe.collidesWith(bird)) {
                endGame();
            }

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score++;

                if (score == 10) {
                    snowMode = true;
                }

                checkHighScore();
            }
        }

        if (!pipes.isEmpty() && pipes.get(0).x + 60 < 0) {
            pipes.remove(0);
            spawnPipe();
        }
    }


    private void checkOutOfBounds() {
        if (bird.y < 0 || bird.y > 550) {
            endGame();
        }
    }

    // === HIGHSCORE ===

    private void checkHighScore() {
        if (!highScoreBeatenThisGame && score > highScore) {
            highScoreBeatenThisGame = true;
            newHighScore = true;
            highScoreTimer = 0;
        }
    }

    private void updateHighScoreAnimation() {
        if (!newHighScore) return;

        highScoreTimer++;

        if (highScoreTimer % 5 == 0) {
            rainbowIndex = (rainbowIndex + 1) % rainbow.length;
        }

        if (highScoreTimer > HIGHSCORE_DURATION) {
            newHighScore = false;
        }
    }

    private void endGame() {
        if (!gameOver) {
            gameOver = true;
            timer.stop();

            if (score > highScore) {
                highScore = score;
                saveHighScore();
            }
        }
    }

    // === RESET ===

    private void resetGame() {
        score = 0;
        gameOver = false;
        newHighScore = false;
        highScoreBeatenThisGame = false;

        bird = new Bird(birdImage);
        pipes.clear();
        spawnPipe();

        bgX1 = 0;
        bgX2 = 400;

        timer.start();
        snowMode = false;
    }

    // === DRAW ===

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, bgX1, 0, 400, 600, null);
        g.drawImage(background, bgX2, 0, 400, 600, null);
        BufferedImage bgToDraw = snowMode ? backgroundSnow : background;
        g.drawImage(bgToDraw, bgX1, 0, 400, 600, null);
        g.drawImage(bgToDraw, bgX2, 0, 400, 600, null);


        for (Pipe pipe : pipes) pipe.draw(g);
        bird.draw(g);

        drawUI(g);
    }

    private void drawUI(Graphics g) {
        g.setColor(Color.GRAY);
        g.setFont(new Font("Georgia", Font.BOLD, 24));
        g.drawString("Score: " + score, 10, 30);

        g.setFont(new Font("Georgia", Font.PLAIN, 18));
        g.setColor(Color.YELLOW);
        g.drawString("Highscore: " + highScore, 10, 60);

        if (newHighScore && (highScoreTimer / 10) % 2 == 0) {
            g.setColor(rainbow[rainbowIndex]);
            g.setFont(new Font("Georgia", Font.BOLD, 22));
            g.drawString("NEW HIGHSCORE!", 90, 100);
        }

        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 32));
            g.drawString("GAME OVER", 90, 250);

            g.setFont(new Font("Georgia", Font.PLAIN, 16));
            g.drawString("Press R to restart", 115, 290);
        }
    }

    // === IO ===

    private void loadHighScore() {
        try {
            java.io.File file = new java.io.File(FILE_NAME);
            if (file.exists()) {
                java.util.Scanner scanner = new java.util.Scanner(file);
                if (scanner.hasNextInt()) {
                    highScore = scanner.nextInt();
                }
                scanner.close();
            }
        } catch (Exception ignored) {}
    }

    private void saveHighScore() {
        try (java.io.FileWriter writer = new java.io.FileWriter(FILE_NAME)) {
            writer.write(String.valueOf(highScore));
        } catch (IOException ignored) {}
    }

    private void loadImages() {
        try {
            background = ImageIO.read(getClass().getResource("/images/background.png"));
            birdImage = ImageIO.read(getClass().getResource("/images/bird.png"));
            pipeImage = ImageIO.read(getClass().getResource("/images/pipe.png"));
            pipeSnowImage   = ImageIO.read(getClass().getResource("/images/pipe-snow.png"));
            backgroundSnow = ImageIO.read(getClass().getResource("/images/background-snow.png"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void spawnPipe() {
        BufferedImage img = snowMode ? pipeSnowImage : pipeImage;
        int gap = snowMode ? SNOW_GAP : NORMAL_GAP;

        pipes.add(new Pipe(400, img, gap));
    }


    // === INPUT ===

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
            bird.jump();
        }

        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            resetGame();
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
