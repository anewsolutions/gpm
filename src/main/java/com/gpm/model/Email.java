/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * A JPA entity for storing a pending email in the database.
 * 
 * @author mbooth
 */
@Entity
public class Email extends Base {
  private static final long serialVersionUID = 1L;

  private String recipientAddress;
  private String replyAddress;
  private String subject;
  private String body;
  private Integer failures = 0;

  public Email() {
    super();
  }

  @Column(length = 500, nullable = false)
  public String getRecipientAddress() {
    return recipientAddress;
  }

  public void setRecipientAddress(final String recipientAddress) {
    this.recipientAddress = recipientAddress;
  }

  @Column(length = 500, nullable = false)
  public String getReplyAddress() {
    return replyAddress;
  }

  public void setReplyAddress(final String replyAddress) {
    this.replyAddress = replyAddress;
  }

  @Column(nullable = false)
  public String getSubject() {
    return subject;
  }

  public void setSubject(final String subject) {
    this.subject = subject;
  }

  @Lob
  @Column(nullable = false)
  public String getBody() {
    return body;
  }

  public void setBody(final String body) {
    this.body = body;
  }

  @Column(nullable = false)
  public Integer getFailures() {
    return failures;
  }

  public void setFailures(final Integer failures) {
    this.failures = failures;
  }
}
