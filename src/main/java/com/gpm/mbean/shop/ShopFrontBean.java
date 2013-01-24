/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.shop;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gpm.manager.ProductManager;
import com.gpm.manager.exception.ProductException;
import com.gpm.model.Product;

@ManagedBean
@ViewScoped
public class ShopFrontBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private List<Product> products;

  @PostConstruct
  public void init() {
    // Pre-load products
    try {
      products = ProductManager.findAll();
    } catch (ProductException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public List<Product> getProducts() {
    return products;
  }
}
