/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import com.gpm.controller.CategoryController;
import com.gpm.controller.ControllerException;
import com.gpm.manager.exception.CategoryException;
import com.gpm.model.Category;

public class CategoryManager {
  public static void create(Category category) throws CategoryException {
    try {
      CategoryController.getInstance().create(category);
    } catch (ControllerException e) {
      throw new CategoryException(e);
    }
  }

  public static void update(Category category) throws CategoryException {
    try {
      CategoryController.getInstance().update(category);
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
