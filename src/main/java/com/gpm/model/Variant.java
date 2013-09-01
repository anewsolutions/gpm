/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.gpm.UploadsServlet;

/**
 * A JPA entity for a product variant.
 * 
 * @author mbooth
 */
@Entity
public class Variant extends Base {
  private static final long serialVersionUID = 1L;

  private String name;
  private String itemImage;
  private Integer price;
  private Integer weight;
  private Integer stock;
  private boolean defaultChoice;

  public Variant() {
    super();
  }

  /**
   * The name of the product variant.
   * 
   * @return a human friendly name
   */
  @Column(nullable = false)
  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  /**
   * The name on disk of an image of the product variant.
   * 
   * @return a file name
   */
  public String getItemImage() {
    return itemImage;
  }

  public void setItemImage(final String itemImage) {
    this.itemImage = itemImage;
  }

  /**
   * Utility to get the URI of the image or a place holder image if no image of the
   * product variant is set.
   * 
   * @return a link to an image
   */
  @Transient
  public String getItemImageUri() {
    String ci = getItemImage();
    if (ci != null && !ci.isEmpty()) {
      return UploadsServlet.UPLOADS_PATH + ci;
    } else {
      return "http://placehold.it/100x100";
    }
  }

  @Column(nullable = false)
  public Integer getPrice() {
    return price;
  }

  public void setPrice(final Integer price) {
    this.price = price;
  }

  @Column(nullable = false)
  public Integer getWeight() {
    return weight;
  }

  public void setWeight(final Integer weight) {
    this.weight = weight;
  }

  @Column(nullable = false)
  public Integer getStock() {
    return stock;
  }

  public void setStock(final Integer stock) {
    this.stock = stock;
  }

  public boolean isDefaultChoice() {
    return defaultChoice;
  }

  public void setDefaultChoice(final boolean defaultChoice) {
    this.defaultChoice = defaultChoice;
  }
}
