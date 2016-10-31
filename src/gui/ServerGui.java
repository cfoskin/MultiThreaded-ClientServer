package gui;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

/**
 * 
 * @author colum Foskin - 20062042 - assignment 02. GUI for the server
 */
public class ServerGui {

	private JFrame frmS;
	private JScrollPane scrollPane;
	public JTextArea jta;

	/**
	 * Create the application.
	 */
	public ServerGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmS = new JFrame();
		frmS.setTitle("Server");
		frmS.setBounds(100, 100, 450, 299);
		frmS.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmS.getContentPane().setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(63, 30, 323, 202);
		frmS.getContentPane().add(scrollPane);

		jta = new JTextArea();
		scrollPane.setViewportView(jta);
		frmS.setVisible(true);
	}
}
