/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.gpm.model.enums.Format;

/**
 * A JPA entity for storing an issue of the magazine that a user has purchased.
 * 
 * @author mbooth
 */
@Entity
public class UserIssue extends Base {
  private static final long serialVersionUID = 1L;

  private Integer issueNumber;
  private Format format;

  public UserIssue() {
    super();
  }

  @Column(nullable = false)
  public Integer getIssueNumber() {
    return issueNumber;
  }

  public void setIssueNumber(final Integer issueNumber) {
    this.issueNumber = issueNumber;
  }

  @Column(nullable = false)
  public Format getFormat() {
    return format;
  }

  public void setFormat(final Format format) {
    this.format = format;
  }
}
