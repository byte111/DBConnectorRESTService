package com.webproj.reports.vo;

import java.util.Date;

public class BillingMO implements IPreparedStatementSetterObj{

	@Override
	public String toString() {
		return "BillingMO [billingId=" + billingId + ", senderId=" + senderId + ", receiverId=" + receiverId
				+ ", fileSize=" + fileSize + ", timestamp=" + timestamp + "]";
	}
	private String billingId;
	private String senderId;
	private String receiverId;
	private float fileSize;
	private Long timestamp;
	
	
	public void setFileSize(float fileSize)
	{
		this.fileSize = fileSize;
	}
	
	public float getFileSize()
	{
		return fileSize;
	}
	
	public void setTimestamp(Long timestamp)
	{
		this.timestamp = timestamp;
	}
	public Long getTimestamp()
	{
		return timestamp;
	}
	public void setBillingId(String billingId)
	{
		this.billingId = billingId;
	}
	public void setSenderId(String senderId)
	{
		this.senderId = senderId;
	}
	public void setReceiverId(String receiverId)
	{
		this.receiverId = receiverId;
	}
	public String getSenderid()
	{

		return senderId;
	}
	
	public String getReceiverId()
	{
		return receiverId;
	}
	public String getBillingId()
	{
		return billingId;
	}
}
