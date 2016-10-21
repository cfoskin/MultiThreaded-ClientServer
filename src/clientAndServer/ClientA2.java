package clientAndServer;

import java.io.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;
import gui.ClientGui;

public class ClientA2 {
  // Text field for receiving radius
  // IO streams
  private DataOutputStream toServer;
  private DataInputStream fromServer;
  ClientGui clientGui;
	private InetAddress inetAddress;

  public static void main(String[] args) {
    new ClientA2();
  }

  public ClientA2() {
	  clientGui = new ClientGui();
	  clientGui.submitBtn.addActionListener(new Listener());
    try {
      // Create a socket to connect to the server
      Socket socket = new Socket("localhost", 8000);
      inetAddress = socket.getInetAddress();
		clientGui.frmClient.setTitle("My host name is " + inetAddress.getHostName() + "  Ip: " + inetAddress.getHostAddress() );
      // Create an input stream to receive data from the server
      fromServer = new DataInputStream(socket.getInputStream());

      // Create an output stream to send data to the server
      toServer = new DataOutputStream(socket.getOutputStream());
    }
    catch (IOException ex) {
    	clientGui.radiusTextArea.append(ex.toString() + '\n');
    }
  }

  private class Listener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
    	  double accountNumber = Double.parseDouble(clientGui.accJtf.getText().trim());
          toServer.writeDouble(accountNumber);
        // Get the radius from the text field
        double radius = Double.parseDouble(clientGui.radiusJtf.getText().trim());
        // Send the radius to the server
        toServer.writeDouble(radius);
        
        toServer.flush();

        // Get area from the server
        double area = fromServer.readDouble();

        // Display to the text area
        clientGui.radiusTextArea.append("Radius is " + radius + "\n");
        clientGui.radiusTextArea.append("Area received from the server is "
          + area + '\n');
      }
      catch (IOException ex) {
        System.err.println(ex);
      }
    }
  }
}