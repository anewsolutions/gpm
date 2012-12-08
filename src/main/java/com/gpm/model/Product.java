/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Entity;

@Entity
public class Product extends Base {

  private String name;

  public Product() {
    super();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
