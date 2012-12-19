/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ProductController;
import com.gpm.manager.exception.ProductException;
import com.gpm.model.Product;

public class ProductManager {
  public static void create(Product product) throws ProductException {
    try {
      ProductController.getInstance().create(product);
    } catch (ControllerException e) {
      throw new ProductException(e);
    }
  }

  public static void update(Product product) throws ProductException {
    try {
      ProductController.getInstance().update(product);
    } catch (ControllerException e) {
      throw new ProductException(e);
    }
  }

  public static void delete(Product product) throws ProductException {
    try {
      ProductController.getInstance().delete(product.getId());
    } catch (ControllerException e) {
      throw new ProductException(e);
    }
  }
}
