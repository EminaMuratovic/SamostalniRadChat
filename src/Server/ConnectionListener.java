package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Listens for client usernames and keeps the chat window up until client closes it
 * @author eminamuratovic
 *
 */
public class ConnectionListener extends Thread {
	private InputStream is;
	private String sender;

	public ConnectionListener(InputStream is, String sender) {
		this.is = is;
		this.sender = sender;
	}

	@Override
	public void run() {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String str = "";

		try {
			while ((str = br.readLine()) != null) {
				if (!str.equals("")) {
					new Message(str + "\n", sender);
				}

			}
			ConnectionWritter.connections.remove(sender);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
