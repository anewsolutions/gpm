/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import com.gpm.controller.ControllerException;
import com.gpm.controller.VariantController;
import com.gpm.manager.exception.VariantException;
import com.gpm.model.Variant;

public class VariantManager {
  public static Variant find(int id) throws VariantException {
    try {
      return VariantController.getInstance().get(id);
    } catch (ControllerException e) {
      throw new VariantException(e);
    }
  }
}
