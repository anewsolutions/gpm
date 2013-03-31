/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ControllerFactory;
import com.gpm.manager.exception.VariantException;
import com.gpm.model.Variant;

public class VariantManager {
  public static Variant find(int id) throws VariantException {
    try {
      return ControllerFactory.getVariantController().get(id);
    } catch (ControllerException e) {
      throw new VariantException(e);
    }
  }
}
