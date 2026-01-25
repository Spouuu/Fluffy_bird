import java.awt.*;
import java.awt.image.BufferedImage;

public class Pipe {

    int x;
    int width = 60;

    int gapY;
    int gapHeight = 150;

    int speed = 3;

    BufferedImage image;

    public Pipe(int startX, BufferedImage image) {
        this.x = startX;
        this.image = image;

        gapY = 100 + (int)(Math.random() * 250);
    }

    public void update() {
        x -= speed;
    }

    public void draw(Graphics g) {
        // górna rura
        g.drawImage(image, x, 0, width, gapY, null);

        // dolna rura
        int bottomY = gapY + gapHeight;
        g.drawImage(image, x, bottomY, width, 600 - bottomY, null);
    }

    public Rectangle getTopBounds() {
        return new Rectangle(x, 0, width, gapY);
    }

    public Rectangle getBottomBounds() {
        int bottomY = gapY + gapHeight;
        return new Rectangle(x, bottomY, width, 600 - bottomY);
    }
}
