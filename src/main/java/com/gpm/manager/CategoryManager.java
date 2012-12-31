/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

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

  public static List<Category> findAll() throws CategoryException {
    try {
      return CategoryController.getInstance().getAll(null, true);
    } catch (ControllerException e) {
      throw new CategoryException(e);
    }
  }

  public static List<Category> findAvailable(Product product) throws CategoryException {
    List<Category> categories = findAll();
    for (Category c : product.getCategories()) {
      categories.remove(c);
    }
    return categories;
  }

  public static void save(Category category) throws CategoryException {
    try {
      if (category.getUuid() == null) {
        CategoryController.getInstance().create(category);
      } else {
        CategoryController.getInstance().update(category);
      }
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
