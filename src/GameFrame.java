import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {

        Assets.load(); // ⬅⬅⬅ MUSI BYĆ TU, NA SAMYM POCZĄTKU

        setTitle("Fluffy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(Assets.ImageIcon );

        showMenu();

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
