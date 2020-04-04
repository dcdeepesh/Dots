package deepe.sh.dots;

import static deepe.sh.dots.Game.*;

class Board {
    public static int size;
    public static int[] Xs, Ys;   // they contain the coordinates not the indices
    // MAKE THIS PRIVATE!
    public static Fragment[][] fragments;

    public static void init(int size) {
        Board.size = size;

        Xs = new int[size];
        Ys = new int[size];

        int seed = MARGIN + RAD;
        int distance = 2*RAD + DIST;
        for (int i = 0; i < size; i++) {
            Xs[i] = seed + i * distance;
            Ys[i] = seed + i * distance;
        }

        // BUG #1
        int ta = size - 1;
        int tb = Fragment.SIZE;
        int tc = ta / tb;
        int nof = tc;
        //int nof = (int)Math.floor(size-1 / Fragment.SIZE);   // number of fragments
        fragments = new Fragment[nof][nof];

        int startingX = 0, startingY = 0;
        for (int y = 0; y < nof; y++) {
            startingX = 0;
            for (int x = 0; x < nof; x++) {
                fragments[y][x] = new Fragment(startingX, startingY);
                startingX += Fragment.SIZE;
            }
            startingY += Fragment.SIZE;
        }
    }

    public static Box boxAt(int x, int y) {
        if (x >= size-1 || y >= size-1)
            return null;
            
        int xIndex = x / Fragment.SIZE;
        int yIndex = y / Fragment.SIZE;

        if (x >= (fragments[0].length) * (Fragment.SIZE))
            xIndex--;
        
        if (y >= (fragments.length) * (Fragment.SIZE))
            yIndex--;

        return (fragments[yIndex][xIndex]).boxAt(x, y);
    }
}
