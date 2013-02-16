/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * A JPA entity for a shopping basket item.
 * 
 * @author mbooth
 */
@Entity
public class BasketItem extends Base {
  private static final long serialVersionUID = 1L;

  private Product product;
  private Variant variant;
  private int quantity;

  public BasketItem() {
    super();
  }

  @Column(nullable = false)
  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  @Column(nullable = false)
  public Variant getVariant() {
    return variant;
  }

  public void setVariant(Variant variant) {
    this.variant = variant;
  }

  public int getQuantity() {
    return quantity;
  }

  @Column(nullable = false)
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Transient
  public String getName() {
    String name = product.getName();
    if (!variant.getName().isEmpty()) {
      name = name + " (" + variant.getName() + ")";
    }
    return name;
  }

  @Transient
  public String getCode() {
    String code = product.getCode();
    if (!variant.getCode().isEmpty()) {
      code = code + "-" + variant.getCode();
    }
    return code;
  }
}
