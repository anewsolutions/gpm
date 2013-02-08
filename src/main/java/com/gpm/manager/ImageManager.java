/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.util.List;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ImageController;
import com.gpm.manager.exception.ImageException;
import com.gpm.model.Image;

public class ImageManager {
  public static Image find(int id) throws ImageException {
    try {
      return ImageController.getInstance().get(id);
    } catch (ControllerException e) {
      throw new ImageException(e);
    }
  }

  public static List<Image> findAll() throws ImageException {
    try {
      return ImageController.getInstance().getAll(null, true);
    } catch (ControllerException e) {
      throw new ImageException(e);
    }
  }

  public static void save(Image image) throws ImageException {
    try {
      ImageController.getInstance().save(image);
    } catch (ControllerException e) {
      throw new ImageException(e);
    }
  }

  public static void delete(Image image) throws ImageException {
    try {
      ImageController.getInstance().delete(image.getId());
    } catch (ControllerException e) {
      throw new ImageException(e);
    }
  }
}
