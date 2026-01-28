import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Assets {
    public static BufferedImage background;
    public static BufferedImage backgroundSnow;
    public static BufferedImage bird;
    public static BufferedImage pipe;
    public static BufferedImage pipeSnow;
    public static BufferedImage pipeFlowers;
    public static BufferedImage backgroundFlowers;

    private static BufferedImage loadImage(String path) {
        try {
            var url = Assets.class.getResource(path);
            if (url == null) {
                throw new RuntimeException("BRAK PLIKU: " + path);
            }
            return ImageIO.read(url);
        } catch (Exception e) {
            throw new RuntimeException("Nie można załadować: " + path, e);
        }
    }

    public static void load() {
        background = loadImage("/images/background.png");
        backgroundSnow = loadImage("/images/background-snow.png");
        backgroundFlowers = loadImage("/images/background-flowers.png");

        bird = loadImage("/images/bird.png");
        pipe = loadImage("/images/pipe.png");
        pipeSnow = loadImage("/images/pipe-snow.png");
        pipeFlowers = loadImage("/images/pipe-flowers.png");
    }

}
