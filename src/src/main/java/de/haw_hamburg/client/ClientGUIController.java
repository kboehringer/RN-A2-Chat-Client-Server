package src.main.java.de.haw_hamburg.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import src.main.java.de.haw_hamburg.Contract;

public class ClientGUIController {
	private ClientGUI gui;
	private boolean readyToLogIn = true;
	private StringBuilder messageHistory;
	private Connection connection;
	private long keyTimer = 0;
	private final long TIMING = 1000;
	
	public ClientGUIController() {
		gui = new ClientGUI();
		messageHistory = new StringBuilder();
		handleControlls();
	}

	private void handleControlls() {
		gui.getLoginButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readyToLogIn) {
					String userName = gui.getUserNameTextField().getText();
					String address = gui.getAddressTextField().getText();
					if (userName == null || userName.isEmpty()) {
						Contract.LogInfo("Invalid Username!");
					} else if (address == null || address.isEmpty()) {
						Contract.LogInfo("Invalid address!");
					} else { //prepare components if connected
						loginToServer(userName, address);
						readyToLogIn = false;
						gui.getLoginButton().setText("logout");
						gui.getNewChatroomTextField().setEnabled(true);
						connection.getChatroomList();
					}
				} else { //prepare conponents if disconnected
					logoutFromServer();
				}
			}
		});
		
		JTable chatroomTable = gui.getChatroomTable();
		chatroomTable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				connection.enterChatroom(chatroomTable.getValueAt(chatroomTable.getSelectedRow(),
						chatroomTable.getSelectedColumn()).toString());
				gui.getMessageInputTextField().setEnabled(true);
			}
		});
		
		gui.getNewChatroomTextField().addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Contract.LogInfo("CR: " + gui.getNewChatroomTextField().getText());
					connection.enterChatroom(gui.getNewChatroomTextField().getText());
					gui.getNewChatroomTextField().setText("");
				}
			}
		});
		
		gui.getMessageInputTextField().addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					connection.sendMessage(gui.getMessageInputTextField().getText());
					gui.getMessageInputTextField().setText("");
				}
			}
		});
		gui.getContentPane().addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F5 &&
						(System.currentTimeMillis() > keyTimer + TIMING)) {
					connection.getChatroomList();
					keyTimer = System.currentTimeMillis();
				}
			}
		});
	}
	
	private void loginToServer(String username, String address) {
		connection = new ServerConnection(address, this);
		connection.sendName(gui.getUserNameTextField().getText());
	}
	
	private void logoutFromServer() {
		connection.disconnect();
		readyToLogIn = true;
		gui.getLoginButton().setText("login");
		gui.getNewChatroomTextField().setEnabled(false);
		gui.getMessageInputTextField().setEnabled(false);
	}

	public void setMessage(String message) {
		messageHistory.append("\n" + message);
		gui.getMessageOutputTextArea().setText(messageHistory.toString());
	}
	
	public void setName(String name) {
		gui.getUserNameTextField().setText(name);
	}
	
	public void setChatroomList(String[] chatrooms) {
		DefaultTableModel model = new DefaultTableModel(new String[] {"Chatraeume"}, 1);
		model.addRow(chatrooms);
		gui.getChatroomTable().setModel(model);
	}
}
