package Client;

import java.io.IOException;

/**
 * Makes a client and opens login window.
 * @author eminamuratovic
 *
 */
public class Client {
	private static final int port = 1717;
	private static final String host = "localhost";

	public static void main(String[] args) throws IOException {
		GUILogin GUI = new GUILogin(host, port);

	}

}
