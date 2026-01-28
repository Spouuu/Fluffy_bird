import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class StartMenuPanel extends JPanel implements ActionListener {


    private final BufferedImage startButton;
    private final BufferedImage startButtonHover;
    private final Runnable onStart;
    private final Background background = new Background();
    private final Timer timer;


    private final Rectangle buttonBounds = new Rectangle(120, 300, 160, 60);
    private boolean hovering = false;

    public StartMenuPanel(Runnable onStart) {
        this.onStart = onStart;

        setPreferredSize(new Dimension(400, 600));

        startButton = Assets.startButton;
        startButtonHover = Assets.startButtonHover;
        timer = new Timer(20, this);
        timer.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (buttonBounds.contains(e.getPoint())) {
                    onStart.run();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                boolean nowHovering = buttonBounds.contains(e.getPoint());
                if (nowHovering != hovering) {
                    hovering = nowHovering;
                    repaint();
                }
            }
        });

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        background.update();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (background == null || startButton == null) return;

        // tło
        background.draw(g);


        // tytuł
        g.setFont(new Font("Georgia", Font.BOLD, 42));
        g.setColor(Color.PINK);
        String title = "FLUFFY BIRD";
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(title);
        g.drawString(title, (400 - textWidth) / 2, 150);

        // przycisk START (hover!)
        BufferedImage btn = hovering ? startButtonHover : startButton;
        g.drawImage(
                btn,
                buttonBounds.x,
                buttonBounds.y,
                buttonBounds.width,
                buttonBounds.height,
                null
        );
    }

}
