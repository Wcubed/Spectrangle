package ss.spec.networking;

import ss.spec.gamepieces.Color;
import ss.spec.gamepieces.Tile;

public abstract class AbstractPeer implements Runnable {

    public static final String INVALID_COMMAND_ERROR_MESSAGE = "invalidCommand";
    public static final String INVALID_NAME_ERROR_MESSAGE = "invalidName";

    private Connection connection;

    private boolean peerConnected;

    AbstractPeer(Connection connection) {
        this.connection = connection;

        peerConnected = !connection.isDead();
    }

    public boolean isPeerConnected() {
        return peerConnected;
    }

    /**
     * Watches for messages from the other end of the connection.
     */
    @Override
    public void run() {
        while (isPeerConnected()) {
            try {
                String message = connection.readMessage();
                handleReceivedMessage(message);
            } catch (DeadConnectionException e) {
                // Connection dead.
                // Thread can stop now.
                peerConnected = false;

                // TODO: proper logging.
            }
        }

        // TODO: Nice logging.
        System.out.println("Connection read thread stopping...");
    }

    /**
     * This function get's called by `run` when a new message arrives over the connection.
     */
    abstract protected void handleReceivedMessage(String message);

    /**
     * Sends a message to the peer. Does not fail when not connected,
     * but `isPeerconnected()` will return false afterwards.
     *
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        if (peerConnected) {
            try {
                connection.sendMessage(message);
            } catch (DeadConnectionException e) {
                peerConnected = false;
            }
        }
    }

    public void sendInvalidCommandError(InvalidCommandException e) {
        System.out.println("Invalid command: \'" + e.getMessage() + "\'.");

        // TODO: send the message along with the error?
        //   This has to be added to the protocol in that case.
        sendMessage(INVALID_COMMAND_ERROR_MESSAGE);
    }

    String convertTileToProtocol(Tile tile) {
        return "" +
                convertColorToProtocol(tile.getFlatSide()) +
                convertColorToProtocol(tile.getClockwise1()) +
                convertColorToProtocol(tile.getClockwise2()) +
                tile.getPoints();
    }

    private Character convertColorToProtocol(Color color) {
        Character result = null;

        switch (color) {
            case RED:
                result = 'R';
                break;
            case BLUE:
                result = 'B';
                break;
            case GREEN:
                result = 'G';
                break;
            case YELLOW:
                result = 'Y';
                break;
            case PURPLE:
                result = 'P';
                break;
            case WHITE:
                result = 'W';
                break;
        }

        return result;
    }
}
