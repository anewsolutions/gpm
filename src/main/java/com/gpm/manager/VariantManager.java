/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.util.UUID;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ControllerFactory;
import com.gpm.manager.exception.VariantException;
import com.gpm.model.Variant;

public class VariantManager {
  /**
   * Get the product variant with the given UUID.
   * 
   * @param uuid
   *          the UUID of the product variant requested
   * @return a product variant with the given UUID
   * @throws VariantException
   *           if there was a problem fetching the product variant
   */
  public static Variant findVariant(final String uuid) throws VariantException {
    try {
      return ControllerFactory.getVariantController().get(UUID.fromString(uuid));
    } catch (IllegalArgumentException e) {
      throw new VariantException(e);
    } catch (ControllerException e) {
      throw new VariantException(e);
    }
  }
}
