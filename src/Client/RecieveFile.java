package Client;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Date;

/**
 * Class that recieves file.
 * Creates new connection with port for recieving files.
 * Then, creates byte array from file and get input stream from new connection.
 * We save file by reading bytes from already made array and putting them in the file with buffered output stream.
 * @author eminamuratovic
 *
 */
public class RecieveFile extends Thread {
	private static final int port = 1919;		//file port
	private static String host = "localhost";
	private InputStream is;
	private Socket s;
	
	@Override
	public void run() {
		try {
			s = new Socket(host, port);
			byte[] array = new byte[2048];
			is = s.getInputStream();
			
			FileOutputStream fs = new FileOutputStream("ID" + new Date().getTime() + ".txt");
			BufferedOutputStream bs = new BufferedOutputStream(fs);
			int bytes;
			
			while((bytes = is.read(array)) > 0) {
				bs.write(array, 0, bytes);
			}
			
			System.out.println("You have recieved a file.");
			bs.flush();
			bs.close();
			is.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
