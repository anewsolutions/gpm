/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * JPA entity for an issue of the magazine.
 * 
 * @author mbooth
 */
@Entity
public class Issue extends Base {
  private static final long serialVersionUID = 1L;

  private int number;
  private Date published;
  private String inThisIssue;

  public Issue() {
    super();
  }

  @Column(nullable = false)
  public int getNumber() {
    return number;
  }

  public void setNumber(final int number) {
    this.number = number;
  }

  @Column(nullable = false)
  @Temporal(TemporalType.DATE)
  public Date getPublished() {
    return published;
  }

  public void setPublished(final Date published) {
    this.published = published;
  }

  @Column(nullable = false)
  @Lob
  public String getInThisIssue() {
    return inThisIssue;
  }

  @Transient
  public List<String> getInThisIssueLines() {
    String inThisIssueLines[] = getInThisIssue().split("\\r?\\n");
    List<String> lines = new ArrayList<String>(inThisIssueLines.length);
    for (String line : inThisIssueLines) {
      if (!line.isEmpty()) {
        lines.add(line);
      }
    }
    return lines;
  }

  public void setInThisIssue(final String inThisIssue) {
    this.inThisIssue = inThisIssue;
  }
}
