/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.util.List;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ProductController;
import com.gpm.manager.exception.ProductException;
import com.gpm.model.Category;
import com.gpm.model.Product;

public class ProductManager {
  public static Product find(int id) throws ProductException {
    try {
      return ProductController.getInstance().get(id);
    } catch (ControllerException e) {
      throw new ProductException(e);
    }
  }

  public static List<Product> findAll() throws ProductException {
    try {
      return ProductController.getInstance().getAll(null, true);
    } catch (ControllerException e) {
      throw new ProductException(e);
    }
  }

  public static List<Product> findAvailable(Category category) throws ProductException {
    List<Product> products = findAll();
    for (Product p : category.getProducts()) {
      products.remove(p);
    }
    return products;
  }

  public static void save(Product product) throws ProductException {
    try {
      if (product.getUuid() == null) {
        ProductController.getInstance().create(product);
      } else {
        ProductController.getInstance().update(product);
      }
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
