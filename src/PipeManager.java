import java.awt.Graphics;
import java.util.ArrayList;

public class PipeManager {

    private final ArrayList<Pipe> pipes = new ArrayList<>();

    private static final int NORMAL_GAP = 220;
    private static final int SNOW_GAP = 180;
    private static final int FLOWER_GAP = 160;

    public void spawn(GameMode mode) {
        switch (mode) {
            case SNOW -> pipes.add(new Pipe(400, Assets.pipeSnow, SNOW_GAP));
            case FLOWER -> pipes.add(new Pipe(400, Assets.pipeFlowers, FLOWER_GAP));
            default -> pipes.add(new Pipe(400, Assets.pipe, NORMAL_GAP));
        }
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

                if (state.score == 10) bg.mode = GameMode.SNOW;
                if (state.score == 20) bg.mode = GameMode.FLOWER;

                if (!state.beatenThisGame && state.score > state.highScore) {
                    state.beatenThisGame = true;
                    state.newHighScore = true;
                    state.highScoreTimer = 0;
                }
            }
        }

        if (!pipes.isEmpty() && pipes.get(0).x + 60 < 0) {
            pipes.remove(0);
            spawn(bg.mode);
        }
    }

    public void draw(Graphics g) {
        for (Pipe p : pipes) p.draw(g);
    }

    public void reset() {
        pipes.clear();
    }
}
