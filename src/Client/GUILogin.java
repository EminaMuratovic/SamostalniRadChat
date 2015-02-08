package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Interface.ChatGUI;

/**
 * Makes interface for Log in window
 * @author eminamuratovic
 *
 */
public class GUILogin {
	private JTextField userName;
	private JPasswordField pass;
	private String host;
	private int port;

	public GUILogin(String host, int port) {
		this.host = host;
		this.port = port;
		JFrame frame = new JFrame("Login");
		frame.setSize(300, 300);
		JPanel panel = new JPanel();
		JLabel name = new JLabel("Username : ");
		userName = new JTextField(10);
		JLabel password = new JLabel("		  Password: ");
		pass = new JPasswordField(10);
		JButton login = new JButton("Login");
		JButton quit = new JButton("Quit");
		ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				System.exit(0); 
			}

		};
		ButtonListener listener1 = new ButtonListener();
		login.addActionListener(listener1);
		quit.addActionListener(listener);
		panel.add(name);
		panel.add(userName);
		panel.add(password);
		panel.add(pass);
		panel.add(login);
		panel.add(quit);
		frame.add(panel);
		frame.setVisible(true);
	}

	private class ButtonListener extends KeyAdapter implements ActionListener {

		@Override
		/**
		 * gets the username and password
		 * if the client enters space in username it instantly gets replaced with an empty string
		 * If the client haven't entered username or password then new window appears with error message
		 * 
		 */
		public void actionPerformed(ActionEvent e) {

			String username = userName.getText();
			String passw = new String(pass.getPassword());
			username = username.replaceAll(" ", "");
			passw = passw.replaceAll(" ", "");
			System.out.println(username);
			System.out.println(passw);

			if (username.equals("") || passw.equals("")) {
				showError("You must enter username AND password!");
				return;
			}
			
			try {
				Socket client = new Socket(host, port);
				OutputStream os = client.getOutputStream();
				InputStream is = client.getInputStream();
				os.write((username + "\n").getBytes());
				os.write((passw + "\n").getBytes());
				int result = is.read();
				if(result == 0) {
					ChatGUI gui = new ChatGUI(client);
					new Thread(gui).start();
				}
				else
					showError("Username/Password is not correct!");
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Makes new window with error message
	 * @param text String error message
	 */
	private void showError(String text) {
		JOptionPane.showMessageDialog(null, text, "ERROR",
				JOptionPane.WARNING_MESSAGE);
	}

}
