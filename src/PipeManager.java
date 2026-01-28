import java.awt.Graphics;
import java.util.ArrayList;

public class PipeManager {
    private final ArrayList<Pipe> pipes = new ArrayList<>();
    private static final int NORMAL_GAP = 220;
    private static final int SNOW_GAP = 180;

    public void spawn(boolean snowMode) {
        pipes.add(new Pipe(
                400,
                snowMode ? Assets.pipeSnow : Assets.pipe,
                snowMode ? SNOW_GAP : NORMAL_GAP
        ));
    }

    public void update(Bird bird, GameState state, Background bg) {
        for (Pipe p : pipes) {
            p.update();

            if (p.collidesWith(bird)) {
                state.gameOver = true;
            }

            if (!p.passed && bird.x > p.x + p.width) {
                p.passed = true;
                state.score++;

                if (state.score == 10) bg.snowMode = true;

                if (!state.beatenThisGame && state.score > state.highScore) {
                    state.beatenThisGame = true;
                    state.newHighScore = true;
                }
            }
        }

        if (!pipes.isEmpty() && pipes.get(0).x + 60 < 0) {
            pipes.remove(0);
            spawn(bg.snowMode);
        }
    }

    public void draw(Graphics g) {
        for (Pipe p : pipes) p.draw(g);
    }

    public void reset() {
        pipes.clear();
    }
}
