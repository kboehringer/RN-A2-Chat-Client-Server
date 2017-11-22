package src.main.java.de.haw_hamburg.clientComponent;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ClientGUI /**extends JFrame**/ {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JPanel contentPane;
	private JTextField userNameTextField;
	private JTextField addressTextField;
	private JTable chatroomTable;
	private JTextField newChatroomTextField;
	private JLabel lblErstelleEinen;
	private JLabel lblNeuenChatroom;
	private JScrollPane messageViewScrollPane;
	private JTextField messageInputTextField;
	private JLabel lblAddress;
	private JLabel lblYourUsername;
	private JLabel lblTypeMessage;
	private JButton loginButton;
	private JTextArea messageOutputTextArea;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ClientGUI frame = new ClientGUI();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public ClientGUI() {
		frame = new JFrame("Chat-Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 800, 650);
		frame.setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		userNameTextField = new JTextField();
		userNameTextField.setBounds(12, 42, 150, 22);
		contentPane.add(userNameTextField);
		userNameTextField.setColumns(10);
		
		addressTextField = new JTextField();
		addressTextField.setBounds(12, 98, 150, 22);
		contentPane.add(addressTextField);
		addressTextField.setColumns(10);
		
		loginButton = new JButton("login");
		loginButton.setBounds(38, 133, 97, 25);
		contentPane.add(loginButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 184, 150, 300);
		contentPane.add(scrollPane);
		
		chatroomTable = new JTable();
		chatroomTable.setRowSelectionAllowed(true);
		scrollPane.setViewportView(chatroomTable);
		
		newChatroomTextField = new JTextField();
		newChatroomTextField.setBounds(12, 546, 150, 22);
		contentPane.add(newChatroomTextField);
		newChatroomTextField.setColumns(10);
		
		lblErstelleEinen = new JLabel("Erstelle einen");
		lblErstelleEinen.setBounds(12, 501, 110, 16);
		contentPane.add(lblErstelleEinen);
		
		lblNeuenChatroom = new JLabel("neuen Chatroom");
		lblNeuenChatroom.setBounds(12, 517, 110, 16);
		contentPane.add(lblNeuenChatroom);
		
		messageViewScrollPane = new JScrollPane();
		messageViewScrollPane.setBounds(237, 28, 487, 464);
		contentPane.add(messageViewScrollPane);
		
		messageOutputTextArea = new JTextArea();
		messageOutputTextArea.setEditable(false);
		messageOutputTextArea.setBackground(Color.WHITE);
		messageViewScrollPane.setViewportView(messageOutputTextArea);
		
		messageInputTextField = new JTextField();
		messageInputTextField.setBounds(237, 528, 487, 22);
		contentPane.add(messageInputTextField);
		messageInputTextField.setColumns(10);
		
		lblAddress = new JLabel("Address:");
		lblAddress.setBounds(12, 72, 76, 16);
		contentPane.add(lblAddress);
		
		lblYourUsername = new JLabel("your username:");
		lblYourUsername.setBounds(12, 13, 110, 16);
		contentPane.add(lblYourUsername);
		
		lblTypeMessage = new JLabel("Type message:");
		lblTypeMessage.setBounds(237, 501, 102, 16);
		contentPane.add(lblTypeMessage);
		
		frame.setVisible(true);
	}

	public JTextField getUserNameTextField() {
		return userNameTextField;
	}

	public JTextField getAddressTextField() {
		return addressTextField;
	}

	public JTable getChatroomTable() {
		return chatroomTable;
	}

	public JTextField getNewChatroomTextField() {
		return newChatroomTextField;
	}

	public JScrollPane getMessageViewScrollPane() {
		return messageViewScrollPane;
	}

	public JTextField getMessageInputTextField() {
		return messageInputTextField;
	}

	public JButton getLoginButton() {
		return loginButton;
	}

	public JTextArea getMessageOutputTextArea() {
		return messageOutputTextArea;
	}
}
