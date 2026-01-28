import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private Bird bird;
    private final GameState state = new GameState();
    private final Background background = new Background();
    private final PipeManager pipes = new PipeManager();
    private final Runnable onExit;


    public GamePanel(Runnable onExit) {
        this.onExit = onExit;

        setPreferredSize(new Dimension(400, 600));
        setFocusable(true);
        addKeyListener(this);

        state.highScore = HighScoreManager.load();

        bird = new Bird(Assets.bird);
        pipes.spawn(background.mode);

        timer = new Timer(20, this);
        timer.start();
        SwingUtilities.invokeLater(this::requestFocusInWindow);


    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (!state.gameOver) {
            background.update();
            bird.update();
            pipes.update(bird, state, background);

            if (bird.y < 0 || bird.y > 550) state.gameOver = true;
        } else {
            timer.stop();
            if (state.score > state.highScore) {
                state.highScore = state.score;
                HighScoreManager.save(state.highScore);
            }
        }
        if (state.newHighScore) {
            state.highScoreTimer++;
            if (state.highScoreTimer > 120) { // ~2 sekundy
                state.newHighScore = false;
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        background.draw(g);
        pipes.draw(g);
        bird.draw(g);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Georgia", Font.PLAIN, 18));
        g.drawString("Score: " + state.score, 10, 20);
        g.drawString("Highscore: " + state.highScore, 10, 40);

        if (state.gameOver) {
            g.drawString("GAME OVER (R)", 130, 300);
        }
        if (state.newHighScore && (state.highScoreTimer / 10) % 2 == 0) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Georgia", Font.BOLD, 28));
            g.drawString("HIGHSCORE!", 95, 100);
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_SPACE && !state.gameOver) {
            bird.jump();
        }

        if (e.getKeyCode() == KeyEvent.VK_R && state.gameOver) {
            reset();
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            timer.stop();
            onExit.run(); // ⬅ wracamy do menu
        }
    }


    private void reset() {
        state.score = 0;
        state.gameOver = false;
        state.newHighScore = false;
        state.beatenThisGame = false;

        bird = new Bird(Assets.bird);
        pipes.reset();
        background.mode = GameMode.NORMAL;
        pipes.spawn(background.mode);


        timer.start();
        SwingUtilities.invokeLater(this::requestFocusInWindow);

    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
