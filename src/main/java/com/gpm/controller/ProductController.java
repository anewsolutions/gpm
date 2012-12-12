/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.controller;

import com.gpm.model.Product;

/**
 * An entity controller for products.
 * 
 * @author mbooth
 * 
 * @see Product
 */
public class ProductController extends GenericController<Product> {

  /**
   * The singleton instance.
   */
  private static ProductController _instance;

  /**
   * Protected constructor for singleton pattern.
   */
  protected ProductController() {
    super(Product.class);
  }

  /**
   * Get the singleton instance.
   * 
   * @return the instance of the controller
   */
  public static ProductController getInstance() {
    if (_instance == null) {
      _instance = new ProductController();
    }
    return _instance;
  }
}
