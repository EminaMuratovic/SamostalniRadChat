package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Makes a server connection with port for writing and sending files
 * This class reads every client that has entered on this server.
 * He reads their usernames and checks if this client have already registered.
 * If the client is in registar, then his password gets checked up.
 * If the password is ok, chat window pops up, but if the password is not correct, he has to enter it again.
 * If the client is not in the register, then he gets added on the list with his username and password.
 * @author eminamuratovic
 *
 */
public class Server {
	private static final int port = 1717;
	private static final int filePort = 2000;

	public static void serverStart() throws IOException {
		ServerSocket server = new ServerSocket(port);
		ServerGUI sg = new ServerGUI();
		ConnectionWritter cw = new ConnectionWritter();
		cw.start();
		
		FileListener fl = new FileListener(filePort);
		fl.start();

		while (true) {
			String str = "Waiting";
			System.out.println(str);
			Socket client;

			try {
				client = server.accept();

				String clientName = handShake(client.getInputStream());
				System.out.println(clientName);
				if (clientName != null) {
					while (ConnectionWritter.connections
							.containsKey(clientName)) {
						clientName += new Random().nextInt(1000);
					}
					ConnectionWritter.connections.put(clientName,
							client.getOutputStream());
					ConnectionListener cl = new ConnectionListener(
							client.getInputStream(), clientName);
					cl.start();
					new Message("%server%","join%" + clientName);
					client.getOutputStream().write(0);
				}
				else {
					client.getOutputStream().write(-1);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) {
		try {
			new XMLConnection();
		} catch (ParserConfigurationException | SAXException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			serverStart();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String handShake(InputStream is) throws IOException {
		Reader r = new InputStreamReader(is);
		BufferedReader bf = new BufferedReader(r);
		String line = null;
		line = bf.readLine();
		line = line.replaceAll("%", ""); 
		String password = bf.readLine();
		int i = XMLConnection.userLogin(line, password);
		if(i == 0)
			return line;
		return null;
	}

}
