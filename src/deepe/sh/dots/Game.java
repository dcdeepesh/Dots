package deepe.sh.dots;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Game {
    public enum Turn {
        PLAYER,
        COMPUTER
    }

    public static BoardComponent boardComponent;

    public static Turn turn = Turn.PLAYER;
    public static JFrame mainFrame;

    public static boolean playerScored = false;
    public static int scorePlayer = 0;
    public static int scoreComputer = 0;
    
    public static final int DIST = 45;
    public static final int RAD = 4;
    public static final int MARGIN = 40;
    public static final int PLUSMINUS = RAD;
    public static final int INDICATOR_MARGIN = 3;

    public static final Color CIRCLE_COLOUR = Color.DARK_GRAY;
    public static final Color HINTLINE_COLOUR = Color.GRAY;

    public static boolean isOver() {
        for (int y = 0; y < Board.size-1; y++)
            for (int x = 0; x < Board.size-1; x++)
                if (!Board.boxAt(x, y).isCompleted())
                    return false;

        return true;
    }

    public static void finish() {
        if (Game.scorePlayer > Game.scoreComputer)
            JOptionPane.showMessageDialog(Game.mainFrame, "You Won!  :D", "Yay!", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(Game.mainFrame, "You Lose!  :(", "Uh, oh", JOptionPane.INFORMATION_MESSAGE);

        System.exit(0);
    }
}
