package com.dev.patterns;

public class Customer {
	private String accountNo;
	private String customerName;
	public String getAccountNo()
	{
		return accountNo;
	}
	public String getCustomerName()
	{
		return customerName;
	}
	public void setAccountNo(String accountNo)
	{
		this.accountNo = accountNo;
	}
	public void setAccountHolderName(String accountHolderName)
	{
		this.customerName = accountHolderName;
	}
	
}
