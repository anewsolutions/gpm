/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.io.FilenameUtils;

/**
 * A JPA entity for a product variant.
 * 
 * @author mbooth
 */
@Entity
public class Variant extends Base {
  private static final long serialVersionUID = 1L;

  private String name;
  private String code;
  private boolean hasImage;
  private String imageName;
  private String imageType;
  private Float price;
  private Integer weight;
  private Integer stock;
  private boolean defaultChoice;

  public Variant() {
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
  public String getCode() {
    return code;
  }

  public void setCode(final String code) {
    this.code = code.toUpperCase();
  }

  public boolean isHasImage() {
    return hasImage;
  }

  public void setHasImage(final boolean hasImage) {
    this.hasImage = hasImage;
  }

  public String getImageName() {
    return imageName;
  }

  public void setImageName(final String imageName) {
    this.imageName = imageName;
  }

  public String getImageType() {
    return imageType;
  }

  public void setImageType(final String imageType) {
    this.imageType = imageType;
  }

  @Transient
  public String getImageFilename() {
    return getUuid() + "." + FilenameUtils.getExtension(getImageName());
  }

  @Column(nullable = false)
  public Float getPrice() {
    return price;
  }

  public void setPrice(final Float price) {
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
