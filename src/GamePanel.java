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


    }



    private void checkHighScore() {
        if (score > highScore) {
            highScore = score;
            saveHighScore();
            newHighScore = true;  // ustaw flagę, że pobito rekord
            blinkCounter = 0;     // reset licznika migania
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
    ArrayList<Pipe> pipes = new ArrayList<>();
    BufferedImage pipeImage;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // najpierw tło
        g.drawImage(background, 0, 0, 400, 600, null);

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
        if (newHighScore) {
            if (blinkCounter < 20) { // pierwsza połowa cyklu
                g.setColor(Color.RED);
            } else {                 // druga połowa cyklu
                g.setColor(Color.WHITE);
            }
            g.setFont(new Font("Georgia", Font.BOLD, 22));
            g.drawString("NEW HIGHSCORE!", 200 - 100, 90); // wyśrodkowane
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
            bird.update();

            for (Pipe pipe : pipes) {
                pipe.update();

                // kolizje
                if (pipe.getTopBounds().intersects(bird.getBounds()) ||
                        pipe.getBottomBounds().intersects(bird.getBounds())) {

                    gameOver = true;
                    timer.stop();
                    checkHighScore();
                }

                // punkt za przejście
                if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                    score++;
                    pipe.passed = true;
                }
            }

            // usuwanie rur poza ekranem
            if (!pipes.isEmpty() && pipes.get(0).x + 60 < 0) {
                pipes.remove(0);
                createPipe();
            }

            // game over jeśli ptak wyleci poza ekran
            if (bird.y > 550 || bird.y < 0) {
                gameOver = true;
                timer.stop();
                checkHighScore();
            }
        }
        // animacja migania „NEW HIGHSCORE!”
        if (newHighScore) {
            blinkCounter++;
            if (blinkCounter > 40) { // zmiana koloru co ~40 klatek (~0.8 sekundy przy Timer=20ms)
                blinkCounter = 0;
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
