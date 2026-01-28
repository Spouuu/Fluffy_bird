import javax.swing.*;
import java.awt.*;

public class StartMenuPanel extends JPanel {

    public StartMenuPanel(Runnable onStart) {
        setPreferredSize(new Dimension(400, 600));
        setLayout(null);

        JLabel title = new JLabel("FLUFFY BIRD", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 36));
        title.setBounds(50, 120, 300, 50);

        JButton startButton = new JButton("START");
        startButton.setFont(new Font("Georgia", Font.BOLD, 24));
        startButton.setBounds(120, 300, 160, 50);

        startButton.addActionListener(e -> onStart.run());

        add(title);
        add(startButton);
    }
}
