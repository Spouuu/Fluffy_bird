import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Background {
    private int x1 = 0;
    private int x2 = 400;
    private static final int SPEED = 1;

    public boolean snowMode = false;

    public void update() {
        x1 -= SPEED;
        x2 -= SPEED;

        if (x1 + 400 <= 0) x1 = x2 + 400;
        if (x2 + 400 <= 0) x2 = x1 + 400;
    }

    public void draw(Graphics g) {
        BufferedImage bg = snowMode ? Assets.backgroundSnow : Assets.background;
        g.drawImage(bg, x1, 0, 400, 600, null);
        g.drawImage(bg, x2, 0, 400, 600, null);
    }
}
