package deepe.sh.dots;

import java.util.ArrayList;

class Box {
    public enum Side {
        UP, DOWN, LEFT, RIGHT
    }

    public enum Owner {
        NONE, PLAYER, COMPUTER
    }

    private int x, y;
    private boolean closedUp, closedDown, closedLeft, closedRight;
    private Owner owner;

    public Box(int x, int y) {
        this.x = x;
        this.y = y;
        closedUp = closedDown = closedLeft = closedRight = false;
        owner = Owner.NONE;
    }

    public void close(Side side) {
        if (isCompleted())
            return;
            
        switch (side) {
            case UP:
                closedUp = true;
                break;

            case DOWN:
                closedDown = true;
                break;

            case LEFT:
                closedLeft = true;
                break;

            case RIGHT:
                closedRight = true;
                break;

            default:
        }

        if (isCompleted()) {
            if (Game.turn == Game.Turn.PLAYER) {
                owner = Owner.PLAYER;
                Game.scorePlayer++;
                Game.playerScored = true;
            }
            else {
                owner = Owner.COMPUTER;
                Game.scoreComputer++;
            }
        }
    }

    private void open(Side side) {
        switch (side) {
            case UP:
                closedUp = false;
                break;

            case DOWN:
                closedDown = false;
                break;

            case LEFT:
                closedLeft = false;
                break;

            case RIGHT:
                closedRight = false;
                break;

            default:
        }
    }

    public boolean isClosed(Side side) {
        switch (side) {
            case UP    : return closedUp;
            case DOWN  : return closedDown;
            case LEFT  : return closedLeft;
            case RIGHT : return closedRight;
            default    : return false;  // unreachable
        }
    }

    public boolean isCompleted() {
        return (closedUp && closedDown && closedLeft && closedRight);
    }

    public boolean isChance() {
        int closeCount = 0;
        if (closedUp)    closeCount++;
        if (closedDown)  closeCount++;
        if (closedLeft)  closeCount++;
        if (closedRight) closeCount++;

        return (closeCount == 3);
    }

    public boolean isChanceExcept(Side side) {
        if (isClosed(side))
            return isChance();
        
        boolean response;
        
        close(side);
        response = isChance();
        open(side);

        return response;
    }

    public Side openSide() {
        if (!closedUp)
            return Side.UP;
        if (!closedDown)
            return Side.DOWN;
        if (!closedLeft)
            return Side.LEFT;
        if (!closedRight)
            return Side.RIGHT;

        else return null;   // unreachable
    }

    public Side openSideExcept(Side side) {
        // just for precaution, this statement should not be reached
        if (isClosed(side))
            return openSide();

        Side response;

        close(side);
        response = openSide();
        open(side);
        
        return response;
    }

    public ArrayList<Side> openSides() {
        ArrayList<Side> openSides = new ArrayList<>();
        
        if (!closedUp)    openSides.add(Side.UP);
        if (!closedDown)  openSides.add(Side.DOWN);
        if (!closedLeft)  openSides.add(Side.LEFT);
        if (!closedRight) openSides.add(Side.RIGHT);

        openSides.trimToSize();
        return openSides;
    }

    public Box boxTowards(Side side) {
        switch (side){
            case UP:
                if (y != 0) return Board.boxAt(x, y-1);
                else return null;
            case DOWN:
                if (y != Board.size - 2) return Board.boxAt(x, y+1);
                else return null;
            case LEFT:
                if (x != 0) return Board.boxAt(x-1, y);
                else return null;
            case RIGHT:
                if (x != Board.size - 2) return Board.boxAt(x+1, y);
                else return null;

            default: return null;
        }
    }

    public static Side oppositeSide(Side side) {
        switch (side) {
            case UP    : return Side.DOWN;
            case DOWN  : return Side.UP;
            case LEFT  : return Side.RIGHT;
            case RIGHT : return Side.LEFT;
            default    : return null;   // unreachable
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Owner getOwner() {
        return owner;
    }
}
