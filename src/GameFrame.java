import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("Fluffy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        showMenu();
        Assets.load();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showMenu() {
        setContentPane(new StartMenuPanel(this::startGame));
        revalidate();
        repaint();
    }

    private void startGame() {
        setContentPane(new GamePanel(this::showMenu));
        revalidate();
        repaint();
    }
}
