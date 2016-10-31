package clientAndServer;

import java.io.*;
import java.net.*;
import java.awt.event.*;
import gui.ClientGui;

/**
 * 
 * @author colum Foskin - 20062042 - assignment 02 The client class whcih
 *         communicates with the server, sending data over sockets i.e. the
 *         radius and account number
 */
public class ClientA2 {
	// Text field for receiving radius
	// IO streams
	private DataOutputStream toServer;
	private DataInputStream fromServer;
	ClientGui clientGui;
	private InetAddress inetAddress;
	Socket socket;

	public static void main(String[] args) {
		new ClientA2();
	}

	/**
	 * Construct a new client
	 */
	public ClientA2() {
		clientGui = new ClientGui();
		clientGui.submitBtn.addActionListener(new Listener());
		try {
			// Create a socket to connect to the server
			socket = new Socket("localhost", 8000);
			inetAddress = socket.getInetAddress();
			clientGui.frmClient
					.setTitle("My host name is " + inetAddress.getHostName() + "  Ip: " + inetAddress.getHostAddress());
			// Create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());

			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			clientGui.radiusTextArea.append(ex.toString() + '\n');
		}
	}

	/**
	 * the listener for the button to send the data to the server. this sends
	 * the data to the server The server responds with the data, this can be a
	 * result or a message that this clients acc number is not valid.
	 */
	private class Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				// get acc no
				int accountNumber = Integer.parseInt(clientGui.accJtf.getText().trim());
				// send to server
				toServer.writeInt(accountNumber);
				// get radius
				double radius = Double.parseDouble(clientGui.radiusJtf.getText().trim());
				// Send to server
				toServer.writeDouble(radius);
				// flush the stream
				toServer.flush();

				int length = fromServer.readInt();
				byte[] dataFromServer = new byte[length];
				fromServer.readFully(dataFromServer);
				String resultString = new String(dataFromServer, "UTF-8");
				// show result to the client
				clientGui.radiusTextArea.append(resultString + "\n");

			} catch (Exception ex) {
				clientGui.radiusTextArea.append("An error has occured - Socket is closed now \n");
				try {
					socket.close();
				} catch (IOException e1) {
					clientGui.radiusTextArea.append("An error has occured \n");
				}
			}
		}
	}
}