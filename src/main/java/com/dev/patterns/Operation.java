package com.dev.patterns;

public class Operation {

	
	private void credit(BankAccount account,Customer customer,int amt)
	{
		System.out.println("CREDITING OPERATION");
		
		new SMSNotification().notifySubscriber(account.getAccountNo()+" ,"+account.getAccountHolderName()+" has been credited with Rs"+amt);
		
	}
	private void debit(BankAccount account,Customer customer,int amt)
	{
		System.out.println("DEBITING OPERATION");
		new EmailNotification().notifySubscriber(account.getAccountNo()+" ,"+account.getAccountHolderName()+" has been debited with Rs"+amt);
	}
	
	private void doOperation(int option){
		
		BankAccount account = new BankAccount();
		Customer customer = new Customer();
		int amt = 1000;
		account.setAccountHolderName("DEVASHJISH");
		account.setAccountNo("1000000");
		customer.setAccountHolderName("DEVASHJISH");
		customer.setAccountNo("1000000");
		
		switch (option) {
		case 1:
			credit(account, customer, amt);
			break;
		case 2:
			debit(account, customer, amt);
			break;
		default:
			break;
		}
	}
	
	public static void main(String[] args) {
		//new Operation().doOperation(1);
		//new Operation().doOperation(2);
	}
	
}
