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

  private Integer number;
  private Date published;
  private Integer stock;
  private String inThisIssue;

  // TODO have a magazine object to keep this stuff in
  public static final float currentPrice = 3.95f;
  public static final float backIssuePrice = 3.30f;

  public Issue() {
    super();
  }

  @Column(nullable = false)
  public Integer getNumber() {
    return number;
  }

  public void setNumber(final Integer number) {
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
  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
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
