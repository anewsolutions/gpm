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
 * A JPA entity for a product category.
 * 
 * @author mbooth
 */
@Entity
public class Category extends Base {
  private static final long serialVersionUID = 1L;

  private String name;
  private Set<Product> products = new HashSet<Product>(0);

  public Category() {
    super();
  }

  @Column(nullable = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    System.out.println("Set Name " + name);
    this.name = name;
  }

  @ManyToMany(fetch = FetchType.EAGER, mappedBy = "categories")
  public Set<Product> getProducts() {
    return products;
  }

  @Transient
  public List<Product> getProductsAsList() {
    return new ArrayList<Product>(getProducts());
  }

  public void setProducts(Set<Product> products) {
    this.products = products;
  }
}
