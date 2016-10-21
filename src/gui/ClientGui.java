package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientGui {

	public JFrame frmClient;
	public JTextField radiusJtf;
	public JLabel radiusLabel;
	public JTextField accJtf;
	public JLabel accLabel;
	private JScrollPane scrollPane;
	public JTextArea radiusTextArea;
	public JButton submitBtn;
	/**
	 * Create the application.
	 */
	public ClientGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmClient = new JFrame();
		frmClient.setTitle("Client");
		frmClient.setBounds(100, 100, 450, 300);
		frmClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmClient.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(41, 18, 362, 76);
		frmClient.getContentPane().add(panel);

		accLabel = new JLabel("Enter Account no:");
		panel.add(accLabel);

		accJtf = new JTextField();
		panel.add(accJtf);
		accJtf.setColumns(15);
		
				radiusLabel = new JLabel("Enter Radius:       ");
				panel.add(radiusLabel);
				
						radiusJtf = new JTextField();
						panel.add(radiusJtf);
						radiusJtf.setColumns(15);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(41, 166, 367, 95);
		frmClient.getContentPane().add(scrollPane);
		
		radiusTextArea = new JTextArea();
		radiusTextArea.setBounds(40, 170, 363, 91);
		frmClient.getContentPane().add(radiusTextArea);
		
		submitBtn = new JButton("Submit");
		
		submitBtn.setBounds(164, 99, 117, 29);
		frmClient.getContentPane().add(submitBtn);
		this.frmClient.setVisible(true);

	}
}
