package com.dev.patterns;

public class EmailNotification implements Notification{

	@Override
	public void notifySubscriber(String subscriberId) {
		// TODO Auto-generated method stub
		System.out.println("Sending Email notification to "+subscriberId);
	}

}
