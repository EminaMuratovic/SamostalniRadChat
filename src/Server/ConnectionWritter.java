package Server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Keeps all messages (their content and usernames of the clients) in the chat window.
 * @author eminamuratovic
 *
 */
public class ConnectionWritter extends Thread {
	public static HashMap<String, OutputStream> connections = new HashMap<String, OutputStream>();
	private Set<String> set = connections.keySet();
	private OutputStream os;

	@Override
	public void run() {
		while (true) {
			if (Message.hasNext()) {
				Message msg = Message.next();
				byte[] msgByte = (msg.getSender() + ": " + msg.getContent())
						.getBytes();
				Iterator<String> iterator = set.iterator();
				String it = iterator.next();
				while (iterator.hasNext()) {
					while (!msg.getSender().equals(it)) {
						os = connections.get(it);
						try {
							os.write(msgByte);
							os.flush();
							it = iterator.next();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
