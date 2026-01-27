import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;


public class GamePanel extends JPanel implements ActionListener, KeyListener {

    Timer timer;
    Bird bird;
    boolean gameOver = false;
    int score = 0;
    int highScore = 0;           // rekord odczytany z pliku
    private final String fileName = "highscore.txt"; // nazwa pliku
    boolean newHighScore = false;  // czy pobito nowy rekord w tej grze
    int blinkCounter = 0;          // licznik do migania napisu
    int bgX1 = 0;
    int bgX2 = 400;     // szerokość okna
    int bgSpeed = 1;    // prędkość przesuwania tła
    Color[] rainbow = {
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.GREEN,
            Color.CYAN,
            Color.BLUE,
            Color.MAGENTA
    };
    int rainbowIndex = 0;
    int rainbowDelay = 0;
    int highScoreTimer = 0;
    static final int HIGHSCORE_DURATION = 100;
    boolean highScoreShown = false;
    ArrayList<Pipe> pipes = new ArrayList<>();
    BufferedImage pipeImage; // ~3 sekundy (150 * 20ms)







    BufferedImage background;
    BufferedImage birdImage;

    public GamePanel() {
        this.setPreferredSize(new Dimension(400, 600));
        this.setFocusable(true);
        this.addKeyListener(this);


        loadImages();
        loadHighScore();      // wczytanie highscore przy starcie

        bird = new Bird(birdImage);

        pipes = new ArrayList<>();
        createPipe(); // pierwsza rura na start

        timer = new Timer(20, this);
        timer.start();
    }

    private void createPipe() {
        pipes.add(new Pipe(400, pipeImage));
    }
    // wczytanie highscore z pliku
    private void loadHighScore() {
        try {
            java.io.File file = new java.io.File(fileName);
            if (file.exists()) {
                java.util.Scanner scanner = new java.util.Scanner(file);
                if (scanner.hasNextInt()) {
                    highScore = scanner.nextInt();
                }
                scanner.close();
            } else {
                highScore = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            highScore = 0;
        }
    }

    // zapis highscore do pliku
    private void saveHighScore() {
        try {
            java.io.FileWriter writer = new java.io.FileWriter(fileName);
            writer.write(String.valueOf(highScore));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetGame() {
        bgX1 = 0;
        bgX2 = 400;
        score = 0;
        bird = new Bird(birdImage);
        pipes.clear();
        createPipe();
        gameOver = false;
        timer.start();for (Pipe pipe : pipes) {
            pipe.passed = false;
        }
        newHighScore = false;
        blinkCounter = 0;
        highScoreShown = false;


    }



    private void checkHighScore() {
        if (score > highScore && !highScoreShown) {
            highScore = score;
            saveHighScore();
            newHighScore = true;
            highScoreTimer = 0;
            highScoreShown = true;
        }
    }





    private void loadImages() {
        try {
            background = ImageIO.read(getClass().getResource("/images/background.png"));
            birdImage = ImageIO.read(getClass().getResource("/images/bird.png"));
            pipeImage = ImageIO.read(getClass().getResource("/images/pipe.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // najpierw tło
        g.drawImage(background, bgX1, 0, 400, 600, null);
        g.drawImage(background, bgX2, 0, 400, 600, null);


        //  rury
        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }

        //  ptak
        bird.draw(g);
        // bieżący wynik
        g.setColor(Color.WHITE);
        g.setFont(new Font("Georgia", Font.BOLD, 24));
        g.drawString("Score: " + score, 10, 30);

// highscore
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Georgia", Font.PLAIN, 18));
        g.drawString("Highscore: " + highScore, 10, 60);


        g.setFont(new Font("Georgia", Font.BOLD, 24));
        g.drawString("Score: " + score, 10, 30);
        // NEW HIGHSCORE! migający napis
        if (newHighScore && (highScoreTimer / 10) % 2 == 0) {
            g.setColor(rainbow[rainbowIndex]);
            g.setFont(new Font("Georgia", Font.BOLD, 22));
            g.drawString("NEW HIGHSCORE!", 100, 90);
        }



        //  GAME OVER tekst
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 32));
            g.drawString("GAME OVER", 90, 250);

            g.setFont(new Font("Georgia", Font.PLAIN, 16));
            g.drawString("Press R to revive", 115, 290);
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {

        if (!gameOver) {
            bgX1 -= bgSpeed;
            bgX2 -= bgSpeed;

            if (bgX1 + 400 <= 0) bgX1 = bgX2 + 400;
            if (bgX2 + 400 <= 0) bgX2 = bgX1 + 400;

            bird.update();

            for (Pipe pipe : pipes) {
                pipe.update();

                if (pipe.getTopBounds().intersects(bird.getBounds()) ||
                        pipe.getBottomBounds().intersects(bird.getBounds())) {

                    gameOver = true;
                    timer.stop();
                }

                if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                    score++;
                    pipe.passed = true;
                    checkHighScore();
                }
            }

            if (!pipes.isEmpty() && pipes.get(0).x + 60 < 0) {
                pipes.remove(0);
                createPipe();
            }

            if (bird.y > 550 || bird.y < 0) {
                gameOver = true;
            }
        }

        // NEW HIGHSCORE animacja
        if (newHighScore) {
            highScoreTimer++;

            if (highScoreTimer % 5 == 0) {
                rainbowIndex = (rainbowIndex + 1) % rainbow.length;
            }

            if (highScoreTimer > HIGHSCORE_DURATION) {
                newHighScore = false;
            }
        }

        repaint();
    }






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
