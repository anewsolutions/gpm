/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.gpm.model.enums.Format;

/**
 * JPA entity for a purchase of magazine back issues or subscriptions.
 * 
 * @author mbooth
 */
@Entity
public class IssueOrderItem extends OrderItem {
  private static final long serialVersionUID = 1L;

  private String name;
  private Integer startIssue;
  private Integer numIssues;
  private Format format;
  private boolean backIssue;

  public IssueOrderItem() {
    super();
  }

  @Column(nullable = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(nullable = false)
  public Integer getStartIssue() {
    return startIssue;
  }

  public void setStartIssue(final Integer startIssue) {
    this.startIssue = startIssue;
  }

  @Column(nullable = false)
  public Integer getNumIssues() {
    return numIssues;
  }

  public void setNumIssues(final Integer numIssues) {
    this.numIssues = numIssues;
  }

  @Column(nullable = false)
  public Format getFormat() {
    return format;
  }

  public void setFormat(final Format format) {
    this.format = format;
  }

  public boolean isBackIssue() {
    return backIssue;
  }

  public void setBackIssue(final boolean backIssue) {
    this.backIssue = backIssue;
  }
}
