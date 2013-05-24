/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.admin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;

import com.gpm.UploadsServlet;
import com.gpm.manager.ProductManager;
import com.gpm.manager.exception.ProductException;
import com.gpm.model.Product;
import com.gpm.model.Variant;

@ManagedBean
@ViewScoped
public class ProductAdminBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private static final int BUFFER_SIZE = 1024;

  private boolean editing;
  private Product selected;

  private Variant variantForImage;

  /**
   * Bean initialisation.
   */
  @PostConstruct
  public void init() {
    try {
      // Load the selected item, if an ID is passed as a query parameter
      FacesContext fc = FacesContext.getCurrentInstance();
      String uuid = fc.getExternalContext().getRequestParameterMap().get("uuid");
      if (uuid != null && !uuid.isEmpty()) {
        selected = ProductManager.findByUuid(uuid);
        editing = true;
      } else {
        selected = new Product();
      }
      // Must have as least one variant
      if (selected.getVariants().size() == 0) {
        addVariant();
      }
    } catch (ProductException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * JSF method to determine whether we are editing a product or adding a new product.
   * 
   * @return true if editing an existing product, false if adding a new product
   */
  public boolean isEditing() {
    return editing;
  }

  /**
   * JSF method to provide access to the currently selected product.
   * 
   * @return the selected product
   */
  public Product getSelected() {
    return selected;
  }

  /**
   * JSF method to provide access to all products.
   * 
   * @return a list of products
   */
  public List<Product> getAll() {
    List<Product> all = null;
    try {
      all = ProductManager.findAllProducts();
    } catch (ProductException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return all;
  }

  /**
   * JSF method to save the selected product.
   */
  public String save() {
    try {
      ProductManager.save(selected);
    } catch (ProductException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/product_list?faces-redirect=true";
  }

  /**
   * JSF method to delete the selected product.
   */
  public String delete() {
    try {
      ProductManager.delete(selected);
    } catch (ProductException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/product_list?faces-redirect=true";
  }

  public void addVariant() {
    Variant v = new Variant();
    // If this is not the first variant, re-use price, weight and stock values
    if (selected.getVariants().size() > 0) {
      Variant prev = selected.getVariantsAsList().get(selected.getVariants().size() - 1);
      v.setPrice(prev.getPrice());
      v.setWeight(prev.getWeight());
      v.setStock(prev.getStock());
      v.setDefaultChoice(false);
    } else {
      v.setName("");
      v.setCode("");
      v.setDefaultChoice(true);
    }
    selected.getVariants().add(v);
  }

  public void removeVariant(Variant variant) {
    if (variant != null) {
      selected.getVariants().remove(variant);
      // If there is only one variant left, reset its name/code and make it the default
      if (selected.getVariants().size() == 1) {
        Variant v = selected.getDefaultVariant();
        v.setName("");
        v.setCode("");
        v.setDefaultChoice(true);
      }
    }
  }

  public void setVariantForImage(Variant variantForImage) {
    this.variantForImage = variantForImage;
  }

  public UploadedFile getUploadedImage() {
    // TODO Can this method be removed?
    return null;
  }

  public void setUploadedImage(UploadedFile uploadedImage) {
    BufferedInputStream in = null;
    BufferedOutputStream out = null;
    try {
      if (!uploadedImage.getContentType().startsWith("image")) {
        throw new IOException("Not a valid image type");
      }
      variantForImage.setHasImage(true);
      variantForImage.setImageName(uploadedImage.getFileName());
      variantForImage.setImageType(uploadedImage.getContentType());

      // Write file contents to disk
      File path = new File(UploadsServlet.getUploadsDirectory(), variantForImage.getImageFilename());
      in = new BufferedInputStream(uploadedImage.getInputstream(), BUFFER_SIZE);
      out = new BufferedOutputStream(new FileOutputStream(path), BUFFER_SIZE);
      byte[] buffer = new byte[BUFFER_SIZE];
      int length = -1;
      while ((length = in.read(buffer)) >= 0) {
        out.write(buffer, 0, length);
      }
    } catch (IOException e) {
      variantForImage.setHasImage(false);
      variantForImage.setImageName(null);
      variantForImage.setImageType(null);
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      try {
        // Close streams
        if (in != null) {
          in.close();
        }
        if (out != null) {
          out.flush();
          out.close();
        }
      } catch (IOException e) {
        // Ignore, only closing streams
      }
    }
  }
}
