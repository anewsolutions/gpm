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
 * JPA entity for a purchase of magazine subscriptions.
 * 
 * @author mbooth
 */
@Entity
public class SubscriptionItem extends Item {
  private static final long serialVersionUID = 1L;

  private UUID first;
  private Integer length;
  private Format format;

  public SubscriptionItem() {
    super();
  }

  @Column(length = 36, nullable = false)
  @Type(type = "uuid-char")
  public UUID getFirst() {
    return first;
  }

  public void setFirst(final UUID first) {
    this.first = first;
  }

  @Column(nullable = false)
  public Integer getLength() {
    return length;
  }

  public void setLength(final Integer length) {
    this.length = length;
  }

  @Column(nullable = false)
  public Format getFormat() {
    return format;
  }

  public void setFormat(final Format format) {
    this.format = format;
  }
}
