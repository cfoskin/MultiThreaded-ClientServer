package clientAndServer;

/*
 Multithreaded version of Area of Circle Client/Server programme
 */
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.awt.*;
import javax.swing.*;

import gui.ServerGui;

import java.util.Date;

public class MultiThreadedServerA2 {
	// Text area for displaying contents
	private Connection con;
	private Statement st;
	private ResultSet rs;
	ServerGui serverGui;
	private String firstName;
	private String lastName;
	private String accountNum;

	public static void main(String[] args) {
		new MultiThreadedServerA2();
	}

	public MultiThreadedServerA2() {
		serverGui = new ServerGui();
		connectToDb();
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
	 * connect to the database
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

	private boolean checkIfApplicantRegistered(int accountNumber) {
		boolean result = false;
		try {
			st = con.createStatement();
			String query = "select * from RegisteredApplicants where AccountNum = " + accountNumber + ";";
			rs = st.executeQuery(query);
			if (rs.first()) {
				firstName = rs.getString("FirstName");
				lastName = rs.getString("LastName");
				accountNum = rs.getString("AccountNum");
				result = true;
			}
		} catch (SQLException e) {
			infoBox("Error querying database", "Warning!");
		}
		return result;
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
			Double accountNumberAsDouble = inputFromClient.readDouble();
			int accountNumber = accountNumberAsDouble.intValue();

			if (checkIfApplicantRegistered(accountNumber)) {
				serverGui.jta.append("Welcome " + firstName + " " + lastName + "\n");
				outputToClient = new DataOutputStream(socket.getOutputStream());
				inetAddress = socket.getInetAddress();
				serverGui.jta.append("Client's host name is " + inetAddress.getHostName() + "\n");
				serverGui.jta.append("Client's IP Address is " + inetAddress.getHostAddress() + "\n");
			} else {
				infoBox("Sorry you are not registered!", "Warning");
			}
		}

		/*
		 * The method that runs when the thread starts
		 */
		public void run() {
			try {
				while (true) {
					// Receive radius from the client
					double radius = inputFromClient.readDouble();
					// Compute area
					double area = radius * radius * Math.PI;
					// Send area back to the client
					outputToClient.writeDouble(area);
					serverGui.jta.append("Radius received from client: " + radius + '\n');
					serverGui.jta.append("Area found: " + area + '\n');
				}
			} catch (Exception e) {
				System.err.println(e + " on " + socket);
			}
		}
	}
}