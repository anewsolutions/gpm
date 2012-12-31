/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

/**
 * A JPA entity for a product.
 * 
 * @author mbooth
 */
@Entity
public class Product extends Base {
  private static final long serialVersionUID = 1L;

  private String name;
  private String code;
  private String description;
  // private String image;
  // private Float price;
  // private boolean customisable;
  // private boolean subscribable;
  private Set<Category> categories = new HashSet<Category>(0);

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

  @ManyToMany(fetch = FetchType.EAGER)
  public Set<Category> getCategories() {
    return categories;
  }

  @Transient
  public List<Category> getCategoriesAsList() {
    return new ArrayList<Category>(getCategories());
  }

  public void setCategories(Set<Category> categories) {
    this.categories = categories;
  }

  // public String getImage() {
  // return image;
  // }
  //
  // public void setImage(String image) {
  // this.image = image;
  // }
  //
  // public Float getPrice() {
  // return price;
  // }
  //
  // public void setPrice(Float price) {
  // this.price = price;
  // }
  //
  // public boolean isCustomisable() {
  // return customisable;
  // }
  //
  // public void setCustomisable(boolean customisable) {
  // this.customisable = customisable;
  // }
  //
  // public boolean isSubscribable() {
  // return subscribable;
  // }
  //
  // public void setSubscribable(boolean subscribable) {
  // this.subscribable = subscribable;
  // }
}
