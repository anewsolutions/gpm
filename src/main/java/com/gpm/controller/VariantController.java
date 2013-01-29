/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.controller;

import com.gpm.model.Variant;

/**
 * An entity controller for variants.
 * 
 * @author mbooth
 * 
 * @see Variant
 */
public class VariantController extends GenericController<Variant> {

  /**
   * The singleton instance.
   */
  private static VariantController _instance;

  /**
   * Protected constructor for singleton pattern.
   */
  protected VariantController() {
    super(Variant.class);
  }

  /**
   * Get the singleton instance.
   * 
   * @return the instance of the controller
   */
  public static VariantController getInstance() {
    if (_instance == null) {
      _instance = new VariantController();
    }
    return _instance;
  }
}
