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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
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
  private Map<Variant, UploadedFile> uploadedImages = new HashMap<Variant, UploadedFile>(0);

  private boolean editing;
  private Product selected;
  private Variant currentVariant;

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
   * JSF method to set the currently selected variant.
   * 
   * @param currentVariant
   *          the variant for selection
   */
  public void setCurrentVariant(final Variant currentVariant) {
    this.currentVariant = currentVariant;
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
    try {
      deleteTempUploads();
    } catch (IOException e) {
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

  /**
   * JSF method to cancel editing the selected product.
   */
  public String cancel() {
    try {
      deleteTempUploads();
    } catch (IOException e) {
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
        v.setDefaultChoice(true);
      }
    }
  }

  /**
   * JSF method to provide access to the last uploaded image file.
   * 
   * @return a PrimeFaces upload
   */
  public UploadedFile getUploadedImage() {
    return uploadedImages.get(currentVariant);
  }

  /**
   * JSF method to upload a new image file.
   * 
   * @param uploadedImage
   *          a PrimeFaces upload
   */
  public void setUploadedImage(final UploadedFile uploadedImage) {
    BufferedInputStream in = null;
    BufferedOutputStream out = null;
    try {
      // Determine if upload has a valid MIME type
      if (!uploadedImage.getContentType().startsWith("image")) {
        throw new IOException("Not a valid image type");
      }
      // Determine temporary image name
      String ext = FilenameUtils.getExtension(uploadedImage.getFileName());
      currentVariant.setItemImage("tmp-" + currentVariant.getUuid() + "-" + new Date().getTime() + "." + ext);
      File path = new File(UploadsServlet.getUploadsDirectory(), currentVariant.getItemImage());
      // Write file contents to disk
      in = new BufferedInputStream(uploadedImage.getInputstream(), BUFFER_SIZE);
      out = new BufferedOutputStream(new FileOutputStream(path), BUFFER_SIZE);
      byte[] buffer = new byte[BUFFER_SIZE];
      int length = -1;
      while ((length = in.read(buffer)) >= 0) {
        out.write(buffer, 0, length);
      }
      uploadedImages.put(currentVariant, uploadedImage);
    } catch (IOException e) {
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

  /**
   * Utility that deletes any temporary uploads that were performed during editing, but
   * were not saved.
   * 
   * @throws IOException
   *           if there was a problem reading the uploads directory or deleting any
   *           temporary uploads
   */
  private void deleteTempUploads() throws IOException {
    if (selected != null) {
      for (Variant variant : selected.getVariants()) {
        Collection<File> tmpFiles = FileUtils.listFiles(UploadsServlet.getUploadsDirectory(),
            FileFilterUtils.prefixFileFilter("tmp-" + variant.getUuid()), null);
        for (File tmpFile : tmpFiles) {
          FileUtils.forceDelete(tmpFile);
        }
      }
    }
  }
}
