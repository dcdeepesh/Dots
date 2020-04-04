package deepe.sh.dots;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class StatusBar extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel scoreLabel;
    private JLabel timerLabel;

    public StatusBar() {
        setBorder(BorderFactory.createEtchedBorder());
        scoreLabel = new JLabel("Board size: 10 ");
        timerLabel = new JLabel("Mode: Against computer");

        add(scoreLabel, BorderLayout.WEST);
        add(timerLabel, BorderLayout.EAST);
    }

    @Override
    public void paintComponent(Graphics g) {
        add(scoreLabel, BorderLayout.WEST);
        add(timerLabel, BorderLayout.EAST);
    }
}
