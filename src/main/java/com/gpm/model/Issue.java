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
  private Integer weight;
  private Integer ezinePrice;
  private Integer hcopyPrice;

  public Issue() {
    super();
  }

  /**
   * The sequential issue number of this issue of the magazine
   * 
   * @return an issue number
   */
  @Column(nullable = false, unique = true)
  public Integer getIssueNumber() {
    return issueNumber;
  }

  public void setIssueNumber(final Integer issueNumber) {
    this.issueNumber = issueNumber;
  }

  /**
   * The date on which this issue was published.
   * 
   * @return a date, to the nearest day
   */
  @Column(nullable = false)
  @Temporal(TemporalType.DATE)
  public Date getPublishedDate() {
    return publishedDate;
  }

  public void setPublishedDate(final Date publishedDate) {
    this.publishedDate = publishedDate;
  }

  /**
   * Description of what's in the issue.
   * 
   * @return a long piece of text, probably containing multiple line breaks
   */
  @Column(nullable = false)
  @Lob
  public String getInThisIssue() {
    return inThisIssue;
  }

  public void setInThisIssue(final String inThisIssue) {
    this.inThisIssue = inThisIssue;
  }

  /**
   * Utility to get the description of what's in the issue as a list of paragraphs for
   * ease of formatting. Paragraphs are defined as the text that is delimited by newline
   * characters.
   * 
   * @return a list of paragraphs of text
   */
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

  /**
   * The name on disk of the issue's cover image.
   * 
   * @return a file name
   */
  @Column(nullable = false)
  public String getCoverImage() {
    return coverImage;
  }

  public void setCoverImage(final String coverImage) {
    this.coverImage = coverImage;
  }

  /**
   * Utility to get the URI of the cover image or a place holder image if no cover image
   * is set.
   * 
   * @return a link to an image
   */
  @Transient
  public String getCoverImageUri() {
    String ci = getCoverImage();
    if (ci != null && !ci.isEmpty()) {
      return UploadsServlet.UPLOADS_PATH + ci;
    } else {
      return "http://placehold.it/300x419";
    }
  }

  /**
   * The ID of the online edition, as it appears in Calaméo.
   * 
   * @return a Caleméo book ID
   */
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

  /**
   * Number of physical copies available in stock.
   * 
   * @return a number greater than or equal to zero
   */
  @Column(nullable = false, columnDefinition = "int default 100")
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

  /**
   * Weight of this edition of the magazine.
   * 
   * @return a weight in grammes
   */
  @Column(nullable = false, columnDefinition = "int default 185")
  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  /**
   * Price of the electronic version of the magazine.
   * 
   * @return price in pence
   */
  @Column(nullable = false, columnDefinition = "int default 350")
  public Integer getEzinePrice() {
    return ezinePrice;
  }

  public void setEzinePrice(Integer ezinePrice) {
    this.ezinePrice = ezinePrice;
  }

  /**
   * Price of the hard copy version of the magazine.
   * 
   * @return price in pence
   */
  @Column(nullable = false, columnDefinition = "int default 400")
  public Integer getHcopyPrice() {
    return hcopyPrice;
  }

  public void setHcopyPrice(Integer hcopyPrice) {
    this.hcopyPrice = hcopyPrice;
  }
}
