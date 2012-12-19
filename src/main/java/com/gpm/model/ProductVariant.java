/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;

public class ProductVariant extends Base {
  private static final long serialVersionUID = 1L;

  private String name;
  private String code;
  private String description;
  private String image;
  private Float price;

  public ProductVariant() {
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
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Column(length = 1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }
}
