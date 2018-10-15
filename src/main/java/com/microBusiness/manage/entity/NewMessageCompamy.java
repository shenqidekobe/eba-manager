/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_message_compamy")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_message")
public class NewMessageCompamy extends BaseEntity<Long> {

	private static final long serialVersionUID = -8301524499873181426L;

	private String senderIp;
	
	private String receiverIp;

	private Boolean receiverRead;

	private Boolean senderDelete;

	private Boolean receiverDelete;

	private Admin sender;

	private Admin receiver;

	private NewMessage newMessage;
	
	public Boolean getReceiverRead() {
		return receiverRead;
	}

	public void setReceiverRead(Boolean receiverRead) {
		this.receiverRead = receiverRead;
	}

	public Boolean getSenderDelete() {
		return senderDelete;
	}

	public void setSenderDelete(Boolean senderDelete) {
		this.senderDelete = senderDelete;
	}

	public Boolean getReceiverDelete() {
		return receiverDelete;
	}

	public void setReceiverDelete(Boolean receiverDelete) {
		this.receiverDelete = receiverDelete;
	}

	public String getSenderIp() {
		return senderIp;
	}

	public void setSenderIp(String senderIp) {
		this.senderIp = senderIp;
	}

	public String getReceiverIp() {
		return receiverIp;
	}

	public void setReceiverIp(String receiverIp) {
		this.receiverIp = receiverIp;
	}

	@ManyToOne
	public Admin getSender() {
		return sender;
	}

	public void setSender(Admin sender) {
		this.sender = sender;
	}

	@ManyToOne
	public Admin getReceiver() {
		return receiver;
	}

	public void setReceiver(Admin receiver) {
		this.receiver = receiver;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public NewMessage getNewMessage() {
		return newMessage;
	}

	public void setNewMessage(NewMessage newMessage) {
		this.newMessage = newMessage;
	}
}
