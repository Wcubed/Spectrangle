package ss.spec.gamepieces;

public class InvalidMoveException extends Exception {

    Move move;

    public InvalidMoveException(Move move) {
        this.move = move;
    }

    public String toString() {
        return "InvalidMoveException: Move on" + "[" + move.getIndex() + "] + not valid";
    }

}
