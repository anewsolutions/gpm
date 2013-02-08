/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * A JPA entity for a product image.
 * 
 * @author mbooth
 */
@Entity
public class Image extends Base {
  private static final long serialVersionUID = 1L;

  private String name;
  private String extension;

  public Image() {
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
  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  /**
   * File name of the image on disk.
   * 
   * @return file name computed from the object business ID and the image type extension.
   */
  @Transient
  public String getFilename() {
    return getUuid() + "." + getExtension();
  }
}
