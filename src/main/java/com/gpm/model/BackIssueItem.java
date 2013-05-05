/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import com.gpm.model.enums.Format;

/**
 * JPA entity for a purchase of magazine back issues.
 * 
 * @author mbooth
 */
@Entity
public class BackIssueItem extends Item {
  private static final long serialVersionUID = 1L;

  private UUID issue;
  private Format format;

  public BackIssueItem() {
    super();
  }

  @Column(length = 36, nullable = false)
  @Type(type = "uuid-char")
  public UUID getIssue() {
    return issue;
  }

  public void setIssue(final UUID issue) {
    this.issue = issue;
  }

  @Column(nullable = false)
  public Format getFormat() {
    return format;
  }

  public void setFormat(final Format format) {
    this.format = format;
  }
}
