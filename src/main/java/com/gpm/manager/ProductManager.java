/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.util.List;
import java.util.UUID;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ControllerFactory;
import com.gpm.manager.exception.ProductException;
import com.gpm.model.Product;

public class ProductManager {
  /**
   * Get the product with the given UUID.
   * 
   * @param uuid
   *          the UUID of the product requested
   * @return a product with the given UUID
   * @throws ProductException
   *           if there was a problem fetching the product
   */
  public static Product findProduct(final String uuid) throws ProductException {
    try {
      return ControllerFactory.getProductController().get(UUID.fromString(uuid));
    } catch (IllegalArgumentException e) {
      throw new ProductException(e);
    } catch (ControllerException e) {
      throw new ProductException(e);
    }
  }

  public static List<Product> findAll() throws ProductException {
    try {
      return ControllerFactory.getProductController().getAll(null, true);
    } catch (ControllerException e) {
      throw new ProductException(e);
    }
  }

  public static void save(Product product) throws ProductException {
    try {
      ControllerFactory.getProductController().save(product);
    } catch (ControllerException e) {
      throw new ProductException(e);
    }
  }

  public static void delete(Product product) throws ProductException {
    try {
      ControllerFactory.getProductController().delete(product.getUuid());
    } catch (ControllerException e) {
      throw new ProductException(e);
    }
  }
}
