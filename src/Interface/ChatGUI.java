package Interface;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.FileHandler;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Client.RecieveFile;

/**
 * Creates interface for chat
 * @author eminamuratovic
 *
 */
public class ChatGUI extends JFrame implements Runnable {
	private JTextArea display;
	private JTextField messageInp;
	private JButton send = new JButton("Send");
	private Socket connection;
	private InputStream is;
	private OutputStream os;
	private JFileChooser choose = new JFileChooser();
	
	public ChatGUI(Socket connection) throws IOException {
		this.connection = connection;
		is = connection.getInputStream();
		os = connection.getOutputStream();

		JPanel panel = new JPanel();
		display = new JTextArea();
		messageInp = new JTextField();
		display.setEditable(false);
		
		choose.addActionListener((ActionListener) new FileHandler());

		messageInp.setPreferredSize(new Dimension(200, 20));

		ButtonListener listener = new ButtonListener();

		KeyAdapter keyAdapter = new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyPressed(e);
				if (e.getKeyCode() == 10) {
					String str = messageInp.getText();
					if (!str.equals("")) {
						str += "\n";
						display.append("Me: " + str);
						try {
							os.write(str.getBytes());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						messageInp.setText("");
					}
				}
			}
		};
		send.addActionListener(listener);

		send.setPreferredSize(new Dimension(100, 100));

		messageInp.addKeyListener(keyAdapter);
		display.setLineWrap(true);
		JScrollPane scroll = new JScrollPane(display);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(390, 220));

		panel.add(scroll);
		panel.add(messageInp);
		panel.add(send);
		panel.add(choose);

		panel.setVisible(true);
		this.setContentPane(panel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					ChatGUI.this.connection.shutdownOutput();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					ChatGUI.this.connection.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		this.setSize(500, 400);
		this.setVisible(true);
	}

	public void listenForNetwork() throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(is));

		String line = null;
		while ((line = input.readLine()) != null) {
			if (!line.equals("")) {
				if(line.equals("Server: Sending File")) {
					RecieveFile r = new RecieveFile();
					r.start();
					
				}
				String[] newLine = line.split(":");
				if (newLine[0].equals("%server%")) {
					String[] newNewLine = newLine[1].split("%");
					if(newNewLine[0].equals(" join")) {
						display.append(newNewLine[1] + " just join the chat!");
						
					}
					else if(newNewLine[0].equals(" left")) {
						display.append(newNewLine[1] + " just left the chat!");
					}

				} else {
					display.append(line + "\n");
				}
				line = null;
			}
		}

	}

	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == send) {
				if (!messageInp.getText().equals(""))
					display.append("\nMe: " + messageInp.getText());
				messageInp.setText("");
			}

		}

	}

	@Override
	public void run() {
		try {
			listenForNetwork();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
