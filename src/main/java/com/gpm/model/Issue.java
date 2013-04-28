/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
}
