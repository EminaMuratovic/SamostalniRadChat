package Client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class that is in charge of sending the file.
 * Constuctor creates path of the file we want to send.
 * When one client sends a file, his socket becomes the server.
 * When the other client accepts the file, socket of the first client is not server anymore.
 * Then, we create file  with already created path.
 * Buffered Input Stream is in charge of reading the file.
 * And, int the end, file is being sent from array of bytes from file with method os.write
 * @author eminamuratovic
 *
 */
public class SendFile extends Thread {
	private static final int port = 1919; //port for files		
	private String path; //path of the file
	private OutputStream os; 
	
	public SendFile(String path) {
		this.path = path;
	}
	
	@Override
	public void run() {
		ServerSocket connectionS; // connection during file transfer
		Socket connection; // connection after file transfer
		
		try {
			connectionS = new ServerSocket(port);
			connection = connectionS.accept();
			
			File file = new File(path);
			byte[] array = new byte[(int)file.length()];
			
			BufferedInputStream bs = new BufferedInputStream(new FileInputStream(file));
			bs.read(array);
			
			os = connection.getOutputStream();
			System.out.println("Sending");
			os.write(array);
			os.flush();
			System.out.println("Sent!");
			os.close();
			bs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
