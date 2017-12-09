package src.main.java.de.haw_hamburg.clientComponent;

import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.xml.ws.handler.MessageContext;

import src.main.java.de.haw_hamburg.Contract;
import src.main.java.de.haw_hamburg.messagingComponent.MessageHandler;
import src.main.java.de.haw_hamburg.messagingComponent.RechnernetzMessageProtocol;

public class ClientGUIController implements MessageHandler {
	private ClientGUI gui;
	private RechnernetzMessageProtocol protocol;
	private boolean readyToLogIn = true;
	private ArrayList<String> messageHistory;
	
	public ClientGUIController() {
		gui = new ClientGUI();
		protocol = new RechnernetzMessageProtocol();
		messageHistory = new ArrayList<>();
		handleControlls();
	}

	private void handleControlls() {
		gui.getLoginButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				gui.getUserNameTextField().setEnabled(readyToLogIn);
//				gui.getAddressTextField().setEnabled(readyToLogIn);
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
				enterChatroom(chatroomTable.getValueAt(chatroomTable.getSelectedRow(),
						chatroomTable.getSelectedColumn()).toString());
			}
		});
		
		gui.getNewChatroomTextField().addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					enterChatroom(gui.getNewChatroomTextField().getText());
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		
		gui.getMessageInputTextField().addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage(gui.getMessageInputTextField().getText());
					gui.getMessageInputTextField().setText("");
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
	}
	
	private void loginToServer(String username, String address) {
		protocol.addConnection(address, username, this);
		
	}
	
	private void logoutFromServer() {
		//TODO protocol disconnect
		readyToLogIn = true;
		gui.getLoginButton().setText("login");
		gui.getNewChatroomTextField().setEnabled(false);
		gui.getMessageInputTextField().setEnabled(false);
	}
	
	private void enterChatroom(String chatroomName) {
		gui.getMessageInputTextField().setEnabled(true);
		
	}
	
	private void sendMessage(String text) {
		
	}

	@Override
	public void handleIncommingMessage(List<String> messages) {
		messageHistory.addAll(messages);
	}
}
