package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

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
		frmS.setBounds(100, 100, 450, 271);
		frmS.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmS.getContentPane().setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(63, 30, 323, 156);
		frmS.getContentPane().add(scrollPane);
		
		jta = new JTextArea();
		scrollPane.setViewportView(jta);
		frmS.setVisible(true);
	}
}
