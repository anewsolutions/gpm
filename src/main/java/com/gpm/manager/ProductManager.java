/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.util.ArrayList;
import java.util.List;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ControllerFactory;
import com.gpm.manager.exception.ProductException;
import com.gpm.model.Category;
import com.gpm.model.Product;

public class ProductManager {
  public static Product find(int id) throws ProductException {
    try {
      return ControllerFactory.getProductController().get(id);
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

  public static List<Product> findProductsInCategory(Category category) throws ProductException {
    List<Product> products = new ArrayList<Product>();
    for (Product p : findAll()) {
      if (p.getCategories().contains(category)) {
        products.add(p);
      }
    }
    return products;
  }

  public static List<Product> findProductsNotInCategory(Category category) throws ProductException {
    List<Product> products = new ArrayList<Product>();
    for (Product p : findAll()) {
      if (!p.getCategories().contains(category)) {
        products.add(p);
      }
    }
    return products;
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
      ControllerFactory.getProductController().delete(product.getId());
    } catch (ControllerException e) {
      throw new ProductException(e);
    }
  }

  public static void addProductsToCategory(Category category, List<Product> productsAdded, List<Product> productsRemoved) throws ProductException {
    try {
      if (productsAdded != null) {
        for (Product product : productsAdded) {
          product.getCategories().add(category);
          ControllerFactory.getProductController().save(product);
        }
      }
      if (productsRemoved != null) {
        for (Product product : productsRemoved) {
          product.getCategories().remove(category);
          ControllerFactory.getProductController().save(product);
        }
      }
    } catch (ControllerException e) {
      throw new ProductException(e);
    }
  }
}
