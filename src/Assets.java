import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Assets {
    public static BufferedImage background;
    public static BufferedImage backgroundSnow;
    public static BufferedImage bird;
    public static BufferedImage pipe;
    public static BufferedImage pipeSnow;

    public static void load() {
        try {
            background = ImageIO.read(Assets.class.getResource("/images/background.png"));
            backgroundSnow = ImageIO.read(Assets.class.getResource("/images/background-snow.png"));
            bird = ImageIO.read(Assets.class.getResource("/images/bird.png"));
            pipe = ImageIO.read(Assets.class.getResource("/images/pipe.png"));
            pipeSnow = ImageIO.read(Assets.class.getResource("/images/pipe-snow.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
