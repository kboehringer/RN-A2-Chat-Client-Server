package src.main.java.de.haw_hamburg.server.messagingComponent;

import java.util.Date;

import src.main.java.de.haw_hamburg.server.ApplicationServer;

public class Message {
	
	private String messageText;
	private String userId;
	private String year;
	private String month;
	private String day;
	private String hour;
	private String minute;
	private final String seperator = "\t";
	
	@SuppressWarnings("deprecation")
	public Message(String text, String userId) {
		ApplicationServer.checkNull(text);
		this.messageText = text;
		this.userId = userId;
		Date date = new Date();
		this.year = "" + date.getYear();
		this.month = "" + date.getMonth();
		this.day = "" + date.getDay();
		this.hour = "" + date.getHours();
		this.minute = "" + date.getMinutes();
	}
	
	public Message(String messageValues) {
		String[] parts = new String[7];
		this.year = parts[0];
		this.month = parts[1];
		this.day = parts[2];
		this.hour = parts[3];
		this.minute = parts[4];
		this.userId = parts[5];
		this.messageText = parts[6];
	}
	
	@Override
	public String toString() {
		return year + seperator + month + seperator + day + seperator +
				hour + seperator + minute + seperator + userId + seperator + messageText;
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
