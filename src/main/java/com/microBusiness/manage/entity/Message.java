/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "xx_message")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_message")
public class Message extends BaseEntity<Long> {

	private static final long serialVersionUID = -8301524499873181426L;

	private String title;

	private String content;

	private String ip;

	private Boolean isDraft;

	private Boolean senderRead;

	private Boolean receiverRead;

	private Boolean senderDelete;

	private Boolean receiverDelete;

	private Member sender;

	private Member receiver;

	private Message forMessage;

	private Set<Message> replyMessages = new HashSet<Message>();

	@Column(nullable = false, updatable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotEmpty
	@Length(max = 4000)
	@Column(nullable = false, updatable = false, length = 4000)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(nullable = false, updatable = false)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(nullable = false, updatable = false)
	public Boolean getIsDraft() {
		return isDraft;
	}

	public void setIsDraft(Boolean isDraft) {
		this.isDraft = isDraft;
	}

	@Column(nullable = false)
	public Boolean getSenderRead() {
		return senderRead;
	}

	public void setSenderRead(Boolean senderRead) {
		this.senderRead = senderRead;
	}

	@Column(nullable = false)
	public Boolean getReceiverRead() {
		return receiverRead;
	}

	public void setReceiverRead(Boolean receiverRead) {
		this.receiverRead = receiverRead;
	}

	@Column(nullable = false)
	public Boolean getSenderDelete() {
		return senderDelete;
	}

	public void setSenderDelete(Boolean senderDelete) {
		this.senderDelete = senderDelete;
	}

	@Column(nullable = false)
	public Boolean getReceiverDelete() {
		return receiverDelete;
	}

	public void setReceiverDelete(Boolean receiverDelete) {
		this.receiverDelete = receiverDelete;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	public Member getSender() {
		return sender;
	}

	public void setSender(Member sender) {
		this.sender = sender;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	public Member getReceiver() {
		return receiver;
	}

	public void setReceiver(Member receiver) {
		this.receiver = receiver;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	public Message getForMessage() {
		return forMessage;
	}

	public void setForMessage(Message forMessage) {
		this.forMessage = forMessage;
	}

	@OneToMany(mappedBy = "forMessage", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy(value = "createDate asc")
	public Set<Message> getReplyMessages() {
		return replyMessages;
	}

	public void setReplyMessages(Set<Message> replyMessages) {
		this.replyMessages = replyMessages;
	}

}
