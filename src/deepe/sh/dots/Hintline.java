package deepe.sh.dots;

class Hintline {
    public enum Type {
        VERTICAL,
        HORIZONTAL
    }

    private static Type type;
    private static int x1, x2, y1, y2;
    private static boolean exists = false;

    public static void create(int x, int y, Type type) {
        //if (!exists) {
        Hintline.type = type;

        if (type == Type.HORIZONTAL) {
            y1 = y;
            y2 = -1;
            for (int i = 0; i < Board.size - 1; i++) {
                if (Board.Xs[i] <= x &&
                   (Board.Xs[i+1] > x || i+1 == Board.Xs.length-1)) {
                    x1 = Board.Xs[i];
                    x2 = Board.Xs[i+1];
                    break;
                }
            }
        } else {
            x1 = x;
            x2 = -1;
            for (int i = 0; i < Board.size - 1; i++) {
                if (Board.Ys[i] <= y &&
                   (Board.Ys[i+1] > y || i+1 == Board.Ys.length-1)) {
                    y1 = Board.Ys[i];
                    y2 = Board.Ys[i+1];
                    break;
                }
            }
        }
            
        exists = true;

        Game.boardComponent.repaint();
        //}
    }

    public static void destroy() {
        if (exists) {
            type = null;
            x1 = x2 = y1 = y2 = -1;
            exists = false;

            Game.boardComponent.repaint();
        }
    }

    public static Type getType() {
        return type;
    }

    public static int getX1() {
        return x1;
    }

    public static int getY1() {
        return y1;
    }

    public static int getX2() {
        return x2;
    }

    public static int getY2() {
        return y2;
    }

    public static boolean exists() {
        return exists;
    }
}
