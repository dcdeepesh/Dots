package deepe.sh.dots;

import java.awt.EventQueue;

class Dots {
    public static void main(String[] args) {
        Board.init(10);

        EventQueue.invokeLater(() -> Game.mainFrame = new MainFrame());
    }
}
