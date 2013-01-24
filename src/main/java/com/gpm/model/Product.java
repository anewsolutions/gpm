/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
  private Set<Variant> variants = new HashSet<Variant>(0);
  private String description;
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
    this.code = code.toUpperCase();
  }

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  public Set<Variant> getVariants() {
    return variants;
  }

  @Transient
  public int getNumVariants() {
    return getVariants().size();
  }

  @Transient
  public List<Variant> getVariantsAsList() {
    List<Variant> vars = new ArrayList<Variant>(getVariants());
    Collections.sort(vars);
    return vars;
  }

  @Transient
  public Variant getDefaultVariant() {
    Variant var = null;
    for (Variant v : variants) {
      if (var == null || v.isDefaultChoice()) {
        var = v;
      }
    }
    return var;
  }

  public void setVariants(Set<Variant> variants) {
    this.variants = variants;
  }

  @Column(length = 1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  public Set<Category> getCategories() {
    return categories;
  }

  @Transient
  public List<Category> getCategoriesAsList() {
    List<Category> cats = new ArrayList<Category>(getCategories());
    Collections.sort(cats);
    return cats;
  }

  public void setCategories(Set<Category> categories) {
    this.categories = categories;
  }
}
