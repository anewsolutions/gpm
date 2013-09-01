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
import javax.persistence.Lob;
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
  private Set<Variant> variants = new HashSet<Variant>(0);
  private String description;

  public Product() {
    super();
  }

  @Column(nullable = false)
  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
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

  public void setVariants(final Set<Variant> variants) {
    this.variants = variants;
  }

  @Column(nullable = false)
  @Lob
  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }
}
