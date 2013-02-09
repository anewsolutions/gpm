/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.io.FilenameUtils;

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

  public void setName(String name) {
    this.name = name;
  }

  @Column(nullable = false)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code.toUpperCase();
  }

  public boolean isHasImage() {
    return hasImage;
  }

  public void setHasImage(boolean hasImage) {
    this.hasImage = hasImage;
  }

  public String getImageName() {
    return imageName;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
  }

  public String getImageType() {
    return imageType;
  }

  public void setImageType(String imageType) {
    this.imageType = imageType;
  }

  @Transient
  public String getImageFilename() {
    return UploadsServlet.UPLOADS_PATH + getUuid() + "." + FilenameUtils.getExtension(getImageName());
  }

  @Column(nullable = false)
  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  @Column(nullable = false)
  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  @Column(nullable = false)
  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }

  public boolean isDefaultChoice() {
    return defaultChoice;
  }

  public void setDefaultChoice(boolean defaultChoice) {
    this.defaultChoice = defaultChoice;
  }
}
