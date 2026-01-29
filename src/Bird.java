import java.awt.*;
import java.awt.image.BufferedImage;

public class Bird {
    int x = 100;
    int y = 200;
    int velocity = 0;

    BufferedImage image;

    public Bird(BufferedImage image) {
        this.image = image;
    }

    public void update() {
        velocity += 1;
        y += velocity;
    }

    public void jump() {
        velocity = -12;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, 36, 30, null);
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, 50, 50);
    }

}
