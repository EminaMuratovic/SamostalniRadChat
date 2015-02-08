package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Listens to clients, if someone enters the port 2000 they immediately connect to port 1919.
 * Port 1919 is in charge of sending files.
 * @author eminamuratovic
 *
 */
public class FileListener extends Thread {
	private int port;
	
	public FileListener(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		
		try {
			ServerSocket s = new ServerSocket(port);
			Socket client = s.accept();
			new Message("Sending File", "Server");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
