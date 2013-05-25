/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * A JPA entity for storing a link to a friend or advertiser in the database.
 * 
 * @author mbooth
 */
@Entity
public class Link extends Base {
  private static final long serialVersionUID = 1L;

  private String linkUrl;
  private String linkText;
  private String linkDescription;

  public Link() {
    super();
  }

  @Column(nullable = false)
  public String getLinkUrl() {
    return linkUrl;
  }

  public void setLinkUrl(final String linkUrl) {
    this.linkUrl = linkUrl;
  }

  @Column(nullable = false)
  public String getLinkText() {
    return linkText;
  }

  public void setLinkText(final String linkText) {
    this.linkText = linkText;
  }

  @Column(nullable = false)
  public String getLinkDescription() {
    return linkDescription;
  }

  public void setLinkDescription(final String linkDescription) {
    this.linkDescription = linkDescription;
  }
}
