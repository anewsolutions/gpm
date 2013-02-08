/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.controller;

import com.gpm.model.Image;

/**
 * An entity controller for images.
 * 
 * @author mbooth
 * 
 * @see Image
 */
public class ImageController extends GenericController<Image> {

  /**
   * The singleton instance.
   */
  private static ImageController _instance;

  /**
   * Protected constructor for singleton pattern.
   */
  protected ImageController() {
    super(Image.class);
  }

  /**
   * Get the singleton instance.
   * 
   * @return the instance of the controller
   */
  public static ImageController getInstance() {
    if (_instance == null) {
      _instance = new ImageController();
    }
    return _instance;
  }
}
