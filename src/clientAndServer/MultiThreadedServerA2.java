package clientAndServer;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import gui.ServerGui;
import java.util.Date;

/**
 * 
 * @author colum Foskin - 20062042 - assignment 02 Multithreaded version of Area
 *         of Circle Client/Server programme This class is the server which
 *         recieves the account number and radius from multiple clients, a new
 *         thread is spawned for each client connection.
 */

public class MultiThreadedServerA2 {
	// Text area for displaying contents
	private Connection con;
	private Statement st;
	private ResultSet rs;
	ServerGui serverGui;
	private String firstName;
	private String lastName;
	private String resultForClient = "";

	public static void main(String[] args) {
		new MultiThreadedServerA2();
	}

	public MultiThreadedServerA2() {
		serverGui = new ServerGui();
		connectToDb();// connect to db on start
		try {
			// Create a server socket
			ServerSocket serverSocket = new ServerSocket(8000);
			serverGui.jta.append("Server started at " + new Date() + '\n');

			while (true) {
				Socket s1 = serverSocket.accept();
				myClient c = new myClient(s1);
				c.start();
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	} // End Server Construct

	/**
	 * connect to the database on start up
	 */
	public Connection connectToDb() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/AreaDatabase", "root", "");
			System.out.println("connected to the database successfully");
		} catch (Exception e) {
			infoBox("Error Connecting to the database - Please try again!", "Warning!");
		}
		return con;
	}

	/**
	 * Check if a client is registered on the system, using their supplied
	 * account number
	 * 
	 * @param accountNumber
	 * @return true or false depending on the result of the query
	 */
	private boolean checkIfApplicantRegistered(int accountNumber) {
		boolean result = false;
		try {
			st = con.createStatement();
			String query = "select * from RegisteredApplicants where AccountNum = " + accountNumber + ";";
			rs = st.executeQuery(query);
			if (rs.first()) {
				firstName = rs.getString("FirstName");
				lastName = rs.getString("LastName");
				result = true;
			}
		} catch (SQLException e) {
			infoBox("Error querying database", "Warning!");
		}
		return result;
	}

	private class myClient extends Thread {
		// The socket the client is connected through
		private Socket socket;
		// The ip address of the client
		private InetAddress inetAddress;
		// The input and output streams to the client
		private DataInputStream inputFromClient;
		private DataOutputStream outputToClient;

		// The Constructor for the client
		public myClient(Socket socket) throws IOException {
			// Create data input and output streams
			inputFromClient = new DataInputStream(socket.getInputStream());
			outputToClient = new DataOutputStream(socket.getOutputStream());
			// getting the inet address from the socket
			inetAddress = socket.getInetAddress();
			serverGui.jta.append("Client's host name is " + inetAddress.getHostName() + "\n");
			serverGui.jta.append("Client's IP Address is " + inetAddress.getHostAddress() + "\n");
		}

		/**
		 * The method that runs when the thread starts - reads the input from
		 * the client and checks if they are registered, if so calculates the
		 * area of a circle from the given radius and sends it.
		 */
		public void run() {
			try {
				String threadId = Thread.currentThread().getName();
				serverGui.jta.append("Thread Id: " + threadId + "\n");
				while (true) {
					resultForClient = "";
					int accountNumber = inputFromClient.readInt();
					if (checkIfApplicantRegistered(accountNumber)) {
						double radius = inputFromClient.readDouble();
						double area = radius * radius * Math.PI;
						resultForClient += "Welcome " + firstName + " " + lastName + "\n";
						resultForClient += "Area is:" + String.valueOf(area);
						System.out.println(resultForClient);
					} else {
						resultForClient += "Sorry - Registered Applicants only!";
					}
					sendToCient();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		/**
		 * send the data to the client, as a byte array.
		 * 
		 * @throws UnsupportedEncodingException
		 * @throws IOException
		 */
		private void sendToCient() throws UnsupportedEncodingException, IOException {
			byte[] resultAsByteArray = resultForClient.getBytes("UTF-8");
			this.outputToClient.writeInt(resultAsByteArray.length);
			this.outputToClient.write(resultAsByteArray);
			this.outputToClient.flush();
		}
	}
	/**
	 * Exit the app and close the db connection
	 */
	public void exit() {
		try {
			if (con != null)
				con.close();
			System.exit(0);
		} catch (SQLException e) {
			infoBox("Error Closing! - Please try again!", "Warning!");
		}
	}
	/**
	 * Info box to alert the user to different messages
	 * 
	 * @param infoMessage
	 * @param titleBar
	 */
	private void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

}