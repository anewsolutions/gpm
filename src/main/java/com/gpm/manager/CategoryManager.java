/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.util.ArrayList;
import java.util.List;

import com.gpm.controller.CategoryController;
import com.gpm.controller.ControllerException;
import com.gpm.manager.exception.CategoryException;
import com.gpm.model.Category;
import com.gpm.model.Product;

public class CategoryManager {
  public static Category find(int id) throws CategoryException {
    try {
      return CategoryController.getInstance().get(id);
    } catch (ControllerException e) {
      throw new CategoryException(e);
    }
  }

  public static List<Category> findAllCategories() throws CategoryException {
    try {
      return CategoryController.getInstance().getAll(null, true);
    } catch (ControllerException e) {
      throw new CategoryException(e);
    }
  }

  public static List<Category> findCategoriesInProduct(Product product) throws CategoryException {
    return product.getCategoriesAsList();
  }

  public static List<Category> findCategoriesNotInProduct(Product product) throws CategoryException {
    List<Category> categories = new ArrayList<Category>();
    for (Category c : findAllCategories()) {
      if (!product.getCategories().contains(c)) {
        categories.add(c);
      }
    }
    return categories;
  }

  public static void save(Category category) throws CategoryException {
    try {
      CategoryController.getInstance().save(category);
    } catch (ControllerException e) {
      throw new CategoryException(e);
    }
  }

  public static void delete(Category category) throws CategoryException {
    try {
      CategoryController.getInstance().delete(category.getId());
    } catch (ControllerException e) {
      throw new CategoryException(e);
    }
  }
}
