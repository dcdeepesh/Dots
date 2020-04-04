package deepe.sh.dots;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public MainFrame() {
        setTitle("Dots");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setResizable(false);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenu optionsMenu = new JMenu("Options");
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(gameMenu);
        menuBar.add(optionsMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        add(new StatusBar(), BorderLayout.SOUTH);

        Game.boardComponent = new BoardComponent();
        add(Game.boardComponent, BorderLayout.CENTER);
        
        pack();
        setVisible(true);
    }
}
