package com.dev.patterns;

public class SMSNotification implements Notification {

	@Override
	public void notifySubscriber(String subscriberId) {
		// TODO Auto-generated method stub
		System.out.println("Sending SMS notification. Msg :  "+subscriberId);
	}
	
	
}
