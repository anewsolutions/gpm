/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Item extends Base {
  private static final long serialVersionUID = 1L;

  private String name;
  private Integer price;
  private Integer quantity;

  public Item() {
    super();
  }

  @Column(nullable = false)
  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Column(nullable = false)
  public Integer getPrice() {
    return price;
  }

  public void setPrice(final Integer price) {
    this.price = price;
  }

  @Column(nullable = false)
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(final Integer quantity) {
    this.quantity = quantity;
  }
}
