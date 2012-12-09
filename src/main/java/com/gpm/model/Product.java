/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * A JPA entity for a product.
 * 
 * @author mbooth
 */
@Entity
public class Product extends Base {

  private String name;
  private String variant;
  private String sku;
  private String description;
  private String image;
  private boolean customisable;
  private boolean subscribable;

  public Product() {
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
  public String getVariant() {
    return variant;
  }

  public void setVariant(String variant) {
    this.variant = variant;
  }

  @Column(nullable = false)
  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  @Column(length = 1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isCustomisable() {
    return customisable;
  }

  public void setCustomisable(boolean customisable) {
    this.customisable = customisable;
  }

  public boolean isSubscribable() {
    return subscribable;
  }

  public void setSubscribable(boolean subscribable) {
    this.subscribable = subscribable;
  }
}
