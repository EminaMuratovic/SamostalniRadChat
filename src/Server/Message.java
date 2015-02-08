package Server;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Creates a message with username of the sender and content of the message
 * @author eminamuratovic
 *
 */
public class Message {
	private String sender;
	private String content;
	private static volatile Queue<Message> msgQueue = new LinkedList<Message>();

	public Message(String sender, String content) {
		this.sender = sender;
		this.content = content + "\n";
		msgQueue.add(this);
	}

	public static boolean hasNext() {
		return !msgQueue.isEmpty();
	}

	public static Message next() {
		return msgQueue.poll();
	}

	public String getSender() {
		return sender;
	}

	public String getContent() {
		return content;
	}

}
