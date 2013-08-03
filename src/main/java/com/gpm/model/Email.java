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

  private String recipient;
  private String subject;
  private String body;

  public Email() {
    super();
  }

  @Column(nullable = false)
  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(final String recipient) {
    this.recipient = recipient;
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
}
