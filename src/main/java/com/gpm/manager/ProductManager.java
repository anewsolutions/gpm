/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.gpm.UploadsServlet;
import com.gpm.controller.ControllerException;
import com.gpm.controller.ControllerFactory;
import com.gpm.manager.exception.ProductException;
import com.gpm.model.Product;
import com.gpm.model.Variant;

public class ProductManager {
  /**
   * Get the product with the given UUID.
   * 
   * @param uuid
   *          the UUID of the product requested
   * @return a product or null if no product was found for the given UUID
   * @throws ProductException
   *           if there was a problem fetching the product
   */
  public static Product findByUuid(final String uuid) throws ProductException {
    try {
      return ControllerFactory.getProductController().get(UUID.fromString(uuid));
    } catch (IllegalArgumentException e) {
      throw new ProductException(e);
    } catch (ControllerException e) {
      throw new ProductException(e);
    }
  }

  /**
   * Get all products.
   * 
   * @return the list of products
   * @throws ProductException
   *           if there was a problem fetching the list of products
   */
  public static List<Product> findAllProducts() throws ProductException {
    try {
      return ControllerFactory.getProductController().getAll();
    } catch (ControllerException e) {
      throw new ProductException(e);
    }
  }

  /**
   * Persist the given product to the data store.
   * 
   * @param product
   *          the product to be saved
   * @throws ProductException
   *           if there was a problem saving the product
   */
  public static void save(final Product product) throws ProductException {
    try {
      for (Variant variant : product.getVariants()) {
        String itemImage = variant.getItemImage();
        if (itemImage != null && itemImage.startsWith("tmp-")) {
          // Variant item image has temporary name, so we need to move it
          variant.setItemImage(itemImage.substring(4));
          File oldImage = new File(UploadsServlet.getUploadsDirectory(), itemImage);
          File newImage = new File(UploadsServlet.getUploadsDirectory(), variant.getItemImage());
          FileUtils.copyFile(oldImage, newImage, false);
          FileUtils.forceDelete(oldImage);
        }
      }
      ControllerFactory.getProductController().save(product);
    } catch (ControllerException e) {
      throw new ProductException(e);
    } catch (IOException e) {
      throw new ProductException(e);
    }
  }

  /**
   * Delete the given product from the data store.
   * 
   * @param product
   *          the product to be deleted
   * @throws ProductException
   *           if there was a problem deleting the product
   */
  public static void delete(final Product product) throws ProductException {
    try {
      ControllerFactory.getProductController().delete(product.getUuid());
    } catch (ControllerException e) {
      throw new ProductException(e);
    }
  }
}
