package deepe.sh.dots;

class Fragment {
    public static final int SIZE = 3;

    private final int width, height;
    private final Box[][] boxes;
    private final int startingX, startingY;

    public Fragment(int startingX, int startingY) {
        this.startingX = startingX;
        this.startingY = startingY;

        // BUG #2
        int ta = Board.size;
        int tb = ta - 1;
        int tc = tb % SIZE;
        int remainder = tc;

        // set the width and height of the fragment
        if (startingX + SIZE + remainder == Board.size-1)
            width = SIZE + remainder;
        else
            width = SIZE;

        if (startingY + SIZE + remainder == Board.size-1)
            height = SIZE + remainder;
        else
            height = SIZE;

        // initialize the array of boxes
        boxes = new Box[height][width];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                boxes[y][x] = new Box(startingX + x, startingY + y);
    }

    public Box boxAt(int x, int y) {
        if (x < startingX || x >= startingX + width ||
            y < startingY || y >= startingY + height)
            return null;

        return boxes[y-startingY][x-startingX];
    }

    public Box relativeBoxAt(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return null;
        return boxes[y][x];
    }

    public int getLinkCount() {
        int linkCount = 0;
        for (Box[] boxArr : boxes) {
            for (Box box : boxArr) {
                if (box.getY() == 0 && box.isClosed(Box.Side.UP))
                    linkCount++;
                if (box.getX() == 0 && box.isClosed(Box.Side.LEFT))
                    linkCount++;
                if (box.isClosed(Box.Side.DOWN))  linkCount++;
                if (box.isClosed(Box.Side.RIGHT)) linkCount++;
            }
        }

        return linkCount;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "F(" + startingX + "," + startingY + ", " + getLinkCount() + ")";
    }
}
