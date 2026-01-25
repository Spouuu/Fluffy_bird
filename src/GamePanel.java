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


    BufferedImage background;
    BufferedImage birdImage;

    public GamePanel() {
        this.setPreferredSize(new Dimension(400, 600));
        this.setFocusable(true);
        this.addKeyListener(this);

        loadImages();

        bird = new Bird(birdImage);

        pipes = new ArrayList<>();
        createPipe(); // pierwsza rura na start


        timer = new Timer(20, this);
        timer.start();
    }

    private void createPipe() {
        pipes.add(new Pipe(400, pipeImage));
    }

    private void resetGame() {
        bird = new Bird(birdImage);
        pipes.clear();
        createPipe(); // <-- pierwsza rura
        gameOver = false;
        timer.start();
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

        // 4️⃣ GAME OVER tekst
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

                if (pipe.getTopBounds().intersects(bird.getBounds()) ||
                        pipe.getBottomBounds().intersects(bird.getBounds())) {

                    gameOver = true;
                    timer.stop();
                }
            }

            // usuwanie rur poza ekranem
            if (!pipes.isEmpty() && pipes.get(0).x + 60 < 0) {
                pipes.remove(0);
                createPipe();
            }


            if (bird.y > 550 || bird.y < 0) {
                gameOver = true;
                timer.stop();
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
