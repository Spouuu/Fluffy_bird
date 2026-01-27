import java.awt.*;
import java.awt.image.BufferedImage;

public class Pipe {

    int x;
    int width = 60;
    int gapHeight;


    int gapY;

    int speed = 3;
    boolean passed = false;

    BufferedImage image;

    public Pipe(int startX, BufferedImage image, int gapHeight) {
        this.x = startX;
        this.image = image;
        this.gapHeight = gapHeight;

        gapY = 100 + (int)(Math.random() * 250);
    }


    public void update() {
        x -= speed;
    }

    public void draw(Graphics g) {
        // top pipe
        g.drawImage(image, x, 0, width, gapY, null);

        // bottom pipe
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

    public boolean collidesWith(Bird bird) {
        return getTopBounds().intersects(bird.getBounds()) ||
                getBottomBounds().intersects(bird.getBounds());
    }
}
