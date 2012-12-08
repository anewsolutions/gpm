/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.dao;

import com.gpm.model.Product;

/**
 * A data access object for products.
 * 
 * @author mbooth
 * 
 * @see Product
 */
public class ProductDAO extends GenericDAO<Product> {

  /**
   * The singleton instance.
   */
  private static ProductDAO _instance;

  /**
   * Protected constructor for singleton pattern.
   */
  protected ProductDAO() {
    super(Product.class);
  }

  /**
   * Get the singleton instance.
   * 
   * @return the instance of the data access object
   */
  public static ProductDAO getInstance() {
    if (_instance == null) {
      _instance = new ProductDAO();
    }
    return _instance;
  }
}
