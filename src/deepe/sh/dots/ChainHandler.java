package deepe.sh.dots;

import java.util.ArrayList;
import java.util.Random;

class ChainHandler {
    public static class Chain {
        private ArrayList<Box> boxes = new ArrayList<>();

        public Box getRandomBox() {
            return boxes.get(new Random().nextInt(boxes.size()));
        }

        public void add(Box box) { boxes.add(box); }
        public void pack() { boxes.trimToSize(); }
        public int length() { return boxes.size(); }
        public boolean contains(Box box) { return boxes.contains(box); }
    }

    private static ArrayList<Chain> chains = new ArrayList<>();

    public static Chain getSmallestChain() {
        return chains.get(0);
    }

    public static void generateChains() {
        chains.clear();
        
        Box box;
        for (int y = 0; y < Board.size-1; y++) {
            for (int x = 0; x < Board.size-1; x++) {
                box = Board.boxAt(x, y);
                if (isChainable(box) && !isInAChain(box))
                    constructChain(box);
            }
        }

        chains.trimToSize();
        chains.sort((chain1, chain2) -> (chain1.length() - chain2.length()));
    }

    private static boolean isInAChain(Box box) {
        for (Chain chain : chains)
            if (chain.contains(box))
                return true;

        return false;
    }

    private static void constructChain(Box box) {
        Chain newChain = new Chain();
        newChain.add(box);

        for (Box.Side side : box.openSides()) {
            Box.Side nextSide = side;
            Box nextBox = box.boxTowards(nextSide);
            
            if (nextBox == null)
                continue;

            Box.Side enteredSide = Box.oppositeSide(nextSide);
            while (nextBox != box && isChainable(nextBox)) {
                newChain.add(nextBox);
                nextSide = nextBox.openSideExcept(enteredSide);
                nextBox = nextBox.boxTowards(nextSide);
                enteredSide = Box.oppositeSide(nextSide);
            }

            if (nextBox == box)
                break;
        }

        newChain.pack();
        chains.add(newChain);
    }

    private static boolean isChainable(Box box) {
        return (box != null && box.openSides().size() == 2);
    }
}
