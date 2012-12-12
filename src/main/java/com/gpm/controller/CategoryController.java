/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.controller;

import com.gpm.model.Category;

/**
 * An entity controller for categories.
 * 
 * @author mbooth
 * 
 * @see Category
 */
public class CategoryController extends GenericController<Category> {

  /**
   * The singleton instance.
   */
  private static CategoryController _instance;

  /**
   * Protected constructor for singleton pattern.
   */
  protected CategoryController() {
    super(Category.class);
  }

  /**
   * Get the singleton instance.
   * 
   * @return the instance of the controller
   */
  public static CategoryController getInstance() {
    if (_instance == null) {
      _instance = new CategoryController();
    }
    return _instance;
  }
}
