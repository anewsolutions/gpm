/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.shop;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.gpm.model.Product;
import com.gpm.model.Variant;

@ManagedBean
@SessionScoped
public class BasketBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private Product product;
  private Variant variant;

  public void setProduct(Product product) {
    this.product = product;
  }

  public void setVariant(Variant variant) {
    this.variant = variant;
  }

  public void addItemToBasket() {
    //TODO
  }
}
