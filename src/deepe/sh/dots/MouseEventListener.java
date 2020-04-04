package deepe.sh.dots;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static deepe.sh.dots.Game.*;

class MouseEventListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent event) {
        if (!Hintline.exists())
            return;
                
        if (Thinker.isThinking())
            return;

        Game.playerScored = false;
        
        int x = -1;
        for (int i = 0; i < Board.size; i++) {
            if (Board.Xs[i] == Hintline.getX1()) {
                x = i;
                break;
            }
        }

        int y = -1;
        for (int i = 0; i < Board.size; i++) {
            if (Board.Ys[i] == Hintline.getY1()) {
                y = i;
                break;
            }
        }

        if (Hintline.getType() == Hintline.Type.HORIZONTAL) {
            if (y != 0)
                Board.boxAt(x, y - 1).close(Box.Side.DOWN);
            if (y != Board.size - 1)
                Board.boxAt(x, y).close(Box.Side.UP);
        } else {
            if (x != 0)
                Board.boxAt(x - 1, y).close(Box.Side.RIGHT);
            if (x != Board.size - 1)
                Board.boxAt(x, y).close(Box.Side.LEFT);
        }

        Game.boardComponent.repaint();
        if (Game.isOver()) Game.finish();
        if (Game.playerScored)
            return;

        Game.turn = Game.Turn.COMPUTER;
        Thinker.think();
        try {
            Thinker.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Game.isOver()) Game.finish();
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        if (Thinker.isThinking())
            return;
                    
        int x = event.getX(), y = event.getY();

        int maxCoords = (Board.size - 1) * (2*RAD + DIST) + 2*RAD + MARGIN;
        if (x <= MARGIN || y <= MARGIN || x >= maxCoords || y >= maxCoords) {
            Hintline.destroy();
            return;
        }

        for (int i = 0; i < Board.size; i++) {
            if (x >= Board.Xs[i] - PLUSMINUS && x <= Board.Xs[i] + PLUSMINUS) {
                Hintline.create(Board.Xs[i], y, Hintline.Type.VERTICAL);
                return;
            }
        }

        for (int i = 0; i < Board.size; i++) {
            if (y >= Board.Ys[i] - PLUSMINUS && y <= Board.Ys[i] + PLUSMINUS) {
                Hintline.create(x, Board.Ys[i], Hintline.Type.HORIZONTAL);
                return;
            }
        }

        Hintline.destroy();
    }
}
