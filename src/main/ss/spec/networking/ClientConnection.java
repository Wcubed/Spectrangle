package ss.spec.networking;

import java.net.Socket;
import java.util.Scanner;

public class ClientConnection extends AbstractConnection {

    private String name;

    public ClientConnection(Socket socket) {
        super(socket);

        name = null;
    }

    @Override
    // TODO:
    //  Do we want to throw the DeadConnectionException all the way up here? Or handle it lower.
    public void handleMessage(String message) throws DeadConnectionException {
        Scanner scanner = new Scanner(message);

        if (scanner.hasNext()) {
            String command = scanner.next();

            if (getName() == null) {
                // They have not sent the `connect <name> [extensions] message yet.`
                try {
                    parseNameMessage(command, scanner);

                    // All set, send welcome message.
                    sendWelcomeMessage();
                } catch (InvalidCommandException e) {
                    sendInvalidCommandError();
                }
            }
        }
    }

    /**
     * Parses the connect message.
     * If the message parses, it sets the name value and the extensions.
     *
     * @param message The scanner with the message. The name should be next.
     * @param command The command of this message, if it is not `connect` this will throw an
     *                invalidCommandException.
     */
    private void parseNameMessage(String command, Scanner message) throws InvalidCommandException {
        if (command.equals("connect")) {
            if (message.hasNext()) {
                String newName = message.next();
                // TODO: Check for duplicate names. Keep a list of names in the lobby class.

                // We cannot check for spaces in the name, because a space means we start
                // with the list of extensions.

                // TODO: Proper logging.
                System.out.println("Client connected with name: " + newName);

                this.name = newName;
            } else {
                // Whoops, `connect` message with no name.
                // TODO: Proper logging.
                throw new InvalidCommandException();
            }

            // TODO: Proper logging.
            // TODO: parse [extensions]

        } else {
            // We are expecting a connect message here.
            // TODO: Proper logging.
            throw new InvalidCommandException();
        }

    }

    public String getName() {
        return name;
    }

    private void sendWelcomeMessage() throws DeadConnectionException {
        // TODO: Send the supported extensions (if any).
        sendMessage("welcome");
    }
}
