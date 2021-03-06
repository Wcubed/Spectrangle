package ss.spec.gamepieces;

public class BoardCoordinates {
    private final int row;
    private final int column;

    public BoardCoordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    /**
     * method to convert an index representation of a board field to a
     * coordinate representation.
     *
     * @param index Index value to be translated to coordinates
     * @return The points scored with this move.
     */
    public static BoardCoordinates fromIndex(int index) throws IndexException {

        if (index >= 0 && index < Board.BOARD_SIZE) {

            int row = (int) Math.floor((int) Math.sqrt((double) index));
            int column = index - (((int) Math.pow(row, 2)) + row);

            return new BoardCoordinates(row, column);

        } else {

            throw new IndexException();
        }
    }

    public int asIndex() throws IndexException {
        int index = getRow() + ((int) Math.pow(getRow(), 2)) + getColumn();

        if (index >= 0 && index < Board.BOARD_SIZE) {
            return index;
        } else {
            throw new IndexException();
        }
    }

    /**
     * @return The coordinates of the flat side neighbour.
     */
    public BoardCoordinates getFlatNeighbourCoordinates() throws IndexException {
        BoardCoordinates result = null;

        if (flatSideIsFacingDown()) {
            // See if we have a bottom neighbour.
            if (row >= 0 && row <= 4) {
                result = new BoardCoordinates(row + 1, column);
            }
        } else {
            // See if we have a top neighbour.
            if (row >= 1 && row <= 5) {
                result = new BoardCoordinates(row - 1, column);
            }
        }

        if (result == null) {
            throw new IndexException();
        }

        return result;
    }

    /**
     * @return The coordinates of the clockwise side neighbour.
     */
    public BoardCoordinates getClockwiseNeighbourCoordinates() throws IndexException {
        BoardCoordinates result = null;

        if (flatSideIsFacingDown()) {
            // See if we have a left neighbour.

            if (column > -row) {
                result = new BoardCoordinates(row, column - 1);
            }
        } else {
            // See if we have a right neighbour.
            if (column < row) {
                result = new BoardCoordinates(row, column + 1);
            }
        }

        if (result == null) {
            throw new IndexException();
        }

        return result;
    }

    /**
     * @return The coordinates of the counterclockwise side neighbour.
     */
    public BoardCoordinates getCounterclockwiseNeighbourCoordinates() throws IndexException {
        BoardCoordinates result = null;

        if (flatSideIsFacingDown()) {
            // See if we have a right neighbour.
            if (column < row) {
                result = new BoardCoordinates(row, column + 1);
            }
        } else {
            // See if we have a left neighbour.
            if (column > -row) {
                result = new BoardCoordinates(row, column - 1);
            }
        }

        if (result == null) {
            throw new IndexException();
        }

        return result;
    }

    public boolean flatSideIsFacingDown() {
        return (row + column) % 2 == 0;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != BoardCoordinates.class) {
            return false;
        }

        BoardCoordinates coords = (BoardCoordinates) other;

        return coords.getRow() == getRow() && coords.getColumn() == getColumn();
    }
}
