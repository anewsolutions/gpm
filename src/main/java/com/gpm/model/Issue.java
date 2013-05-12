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

import com.gpm.UploadsServlet;

/**
 * JPA entity for an issue of the magazine.
 * 
 * @author mbooth
 */
@Entity
public class Issue extends Base {
  private static final long serialVersionUID = 1L;

  private Integer issueNumber;
  private Date publishedDate;
  private String inThisIssue;
  private String coverImage;
  private String ezineLink;
  private Integer stockLevel;

  // TODO have a magazine object to keep this stuff in
  public static final int weight = 185;
  public static final int currentPrice = 395;
  public static final int backIssuePrice = 330;

  public Issue() {
    super();
  }

  @Column(nullable = false, unique = true)
  public Integer getIssueNumber() {
    return issueNumber;
  }

  public void setIssueNumber(final Integer issueNumber) {
    this.issueNumber = issueNumber;
  }

  @Column(nullable = false)
  @Temporal(TemporalType.DATE)
  public Date getPublishedDate() {
    return publishedDate;
  }

  public void setPublishedDate(final Date publishedDate) {
    this.publishedDate = publishedDate;
  }

  @Column(nullable = false)
  @Lob
  public String getInThisIssue() {
    return inThisIssue;
  }

  @Transient
  public List<String> getInThisIssueLines() {
    final String inThisIssueLines[] = getInThisIssue().split("\\r?\\n");
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

  @Column(nullable = false)
  public String getCoverImage() {
    return coverImage;
  }

  @Transient
  public String getCoverImageUri() {
    String ci = getCoverImage();
    if (ci != null && !ci.isEmpty()) {
      return UploadsServlet.UPLOADS_PATH + getCoverImage();
    } else {
      return "http://placehold.it/300x419&text=No%20Image";
    }
  }

  public void setCoverImage(final String coverImage) {
    this.coverImage = coverImage;
  }

  @Column(nullable = false)
  public String getEzineLink() {
    return ezineLink;
  }

  public void setEzineLink(final String ezineLink) {
    this.ezineLink = ezineLink;
  }

  /**
   * Utility to determine if there is an online edition of this issue.
   * 
   * @return true if an online edition exists
   */
  @Transient
  public boolean isEzineAvailable() {
    String link = getEzineLink();
    if (link != null && !link.isEmpty()) {
      return true;
    } else {
      return false;
    }
  }

  @Column(nullable = false)
  public Integer getStockLevel() {
    return stockLevel;
  }

  public void setStockLevel(final Integer stockLevel) {
    this.stockLevel = stockLevel;
  }

  /**
   * Utility to determine if physical copies of this issue are available.
   * 
   * @return true if there is a stock available of physical copies
   */
  @Transient
  public boolean isStockAvailable() {
    Integer stock = getStockLevel();
    if (stock != null && stock > 0) {
      return true;
    } else {
      return false;
    }
  }
}
