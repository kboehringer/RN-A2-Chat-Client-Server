package src.main.java.de.haw_hamburg.messagingComponent;

import java.util.Date;

import javax.naming.directory.InvalidAttributesException;

import src.main.java.de.haw_hamburg.Contract;

public class Message {
	
	private String messageText;
	private String userId;
	private String chatroomName;
	private String year;
	private String month;
	private String day;
	private String hour;
	private String minute;
	private final String seperator = "\t";
	
	@SuppressWarnings("deprecation")
	public Message(String text, String chatroomName, String userId) {
		Contract.checkNull(text);
		Contract.checkNull(chatroomName);
		Contract.checkNull(userId);
		if (text.isEmpty()) {
			Contract.logException(new InvalidAttributesException("Messagetext sshould not be empty!"));
		}
		if (chatroomName.isEmpty()) {
			Contract.logException(new InvalidAttributesException("ChatroomName should not be empty!"));
		}
		if (userId.isEmpty()) {
			Contract.logException(new InvalidAttributesException("UserId should not be empty!"));
		}
		this.messageText = text;
		this.userId = userId;
		this.chatroomName = chatroomName;
		Date date = new Date();
		this.year = "" + date.getYear();
		this.month = "" + date.getMonth();
		this.day = "" + date.getDay();
		this.hour = "" + date.getHours();
		this.minute = "" + date.getMinutes();
	}
	
	public Message(String messageValues) {
		Contract.checkNull(messageValues);
		if (messageValues.isEmpty()) {
			Contract.logException(new InvalidAttributesException("MessageValues should not be empty!"));
		}
		String[] parts = messageValues.split(seperator);
		this.year = parts[0];
		this.month = parts[1];
		this.day = parts[2];
		this.hour = parts[3];
		this.minute = parts[4];
		this.chatroomName = parts[5];
		this.userId = parts[6];
		this.messageText = parts[7];
	}
	
	@Override
	public String toString() {
		return year + seperator + month + seperator + day + seperator +
				hour + seperator + minute + seperator + chatroomName + seperator + userId + seperator + messageText;
	}

	public String getMessageText() {
		return messageText;
	}

	public String getUserId() {
		return userId;
	}
	
	public String getDate() {
		return year + "-" + month + "-" + day + " " + hour + ":" + minute;
	}
}
