package deepe.sh.dots;

import java.util.ArrayList;
import java.util.Random;

class Thinker implements Runnable {
    // the static part which makes this class act like a namespace
    private static final Thinker thinker = new Thinker();

    public static boolean isThinking() {
        return thinker.thread.isAlive();
    }

    public static void think() {
        thinker.thread = new Thread(new Thinker());
        thinker.thread.start();
    }

    public static void join() throws InterruptedException {
        thinker.thread.join();
    }

    // the instance-specific part
    public Thread thread;

    private Thinker() {
        thread = new Thread(this);
    }

    @Override
    public void run() {
        stage0();
        Game.boardComponent.repaint();

        if(Game.isOver())
            return;

        if (!stage1())
            stage2();

        Game.boardComponent.repaint();
        Game.turn = Game.Turn.PLAYER;
        return;
    }

    private void stage0() {
        boolean stop;
        do {
            stop = true;
            for (int y = 0; y < Board.size - 1; y++) {
                for (int x = 0; x < Board.size - 1; x++) {
                    if (Board.boxAt(x, y).isChance()) {
                        closeBox(Board.boxAt(x, y));
                        stop = false;
                    }
                }
            }
        } while (!stop);
    }

    private void closeBox(Box box) {
        Box.Side openSide = box.openSide();
        Box nextBox = box.boxTowards(openSide);

        box.close(openSide);

        if (nextBox == null)
            return;

        switch (openSide) {
            case UP:
                if (box.getY() != 0)
                    nextBox.close(Box.Side.DOWN);
                break;

            case DOWN:
                if (box.getY() != Board.size - 2)
                    nextBox.close(Box.Side.UP);
                break;

            case LEFT:
                if (box.getX() != 0)
                    nextBox.close(Box.Side.RIGHT);
                break;

            case RIGHT:
                if (box.getX() != Board.size - 2)
                    nextBox.close(Box.Side.LEFT);
                break;
        }
        
        if (nextBox.isChance())
            closeBox(nextBox);
    }

    private boolean stage1() {
        ArrayList<Fragment> linearFragments = new ArrayList<>();

        for (Fragment[] fragments : Board.fragments)
            for (Fragment fragment : fragments)
                linearFragments.add(fragment);
        linearFragments.trimToSize();
        linearFragments.sort((frag1, frag2) -> (frag1.getLinkCount() - frag2.getLinkCount()));
        
        for (Fragment fragment : linearFragments)
            if (alterRandomBox(fragment))
                return true;
        
        return false;
    }

    private boolean alterRandomBox(Fragment fragment) {
        // create all possible pairs of coordinates
        ArrayList<Coordinate> coords = new ArrayList<>();
        for (int y = 0; y < fragment.getHeight(); y++)
            for (int x = 0; x < fragment.getWidth(); x++)
                coords.add(new Coordinate(x, y));
        coords.trimToSize();
        int coordSize = coords.size();

        // try random boxes until there aren't left any
        Random random = new Random();
        Coordinate chosenOne;
        while (coordSize > 0) {
            int randInt = random.nextInt(coordSize);
            chosenOne = coords.get(randInt);

            if (tryBox(fragment.relativeBoxAt(chosenOne.x, chosenOne.y)))
                return true;

            coords.remove(chosenOne);
            coordSize--;
        }

        return false;
    }

    private boolean tryBox(Box box) {
        int closeCount = 0;
        if (box.isClosed(Box.Side.UP))    closeCount++;
        if (box.isClosed(Box.Side.DOWN))  closeCount++;
        if (box.isClosed(Box.Side.LEFT))  closeCount++;
        if (box.isClosed(Box.Side.RIGHT)) closeCount++;
        if (closeCount >= 2)
            return false;

        ArrayList<Box.Side> sides = new ArrayList<>();
        for (Box.Side side : Box.Side.values())
            sides.add(side);
        sides.trimToSize();
        int sideSize = sides.size();

        Box.Side chosenSide;
        Random random = new Random();
        while (sideSize > 0) {
            int randInt = random.nextInt(sideSize);
            chosenSide = sides.get(randInt);

            if (trySide(box, chosenSide))
                return true;

            sides.remove(chosenSide);
            sideSize--;
        }

        return false;
    }

    private boolean trySide(Box box, Box.Side side) {
        if (box.isClosed(side))
            return false;

        Box nextBox = box.boxTowards(side);
        if (nextBox == null) {
            box.close(side);
            return true;
        }

        if (!nextBox.isChanceExcept(Box.oppositeSide(side))) {
            box.close(side);
            nextBox.close(Box.oppositeSide(side));
            return true;
        }

        return false;
    }

    private void stage2() {
        // get a random box in the smallest chain
        ChainHandler.generateChains();
        Box chosenBox = ChainHandler.getSmallestChain().getRandomBox();

        // choose a random open side
        ArrayList<Box.Side> openSides = chosenBox.openSides();
        Box.Side chosenSide = openSides.get(new Random().nextInt(openSides.size()));

        // close that side
        chosenBox.close(chosenSide);
        if (chosenBox.boxTowards(chosenSide) != null)
            chosenBox.boxTowards(chosenSide).close(Box.oppositeSide(chosenSide));
    }
}
