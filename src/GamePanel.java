import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private Bird bird;
    private final GameState state = new GameState();
    private final Background background = new Background();
    private final PipeManager pipes = new PipeManager();

    public GamePanel() {
        setPreferredSize(new Dimension(400, 600));
        setFocusable(true);
        addKeyListener(this);

        Assets.load();
        state.highScore = HighScoreManager.load();

        bird = new Bird(Assets.bird);
        pipes.spawn(false);

        timer = new Timer(20, this);
        timer.start();
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
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        background.draw(g);
        pipes.draw(g);
        bird.draw(g);

        g.setColor(Color.WHITE);
        g.drawString("Score: " + state.score, 10, 20);
        g.drawString("Highscore: " + state.highScore, 10, 40);

        if (state.gameOver) {
            g.drawString("GAME OVER (R)", 130, 300);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !state.gameOver) bird.jump();
        if (e.getKeyCode() == KeyEvent.VK_R && state.gameOver) reset();
    }

    private void reset() {
        state.score = 0;
        state.gameOver = false;
        state.newHighScore = false;
        state.beatenThisGame = false;

        bird = new Bird(Assets.bird);
        pipes.reset();
        pipes.spawn(false);
        background.snowMode = false;

        timer.start();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
