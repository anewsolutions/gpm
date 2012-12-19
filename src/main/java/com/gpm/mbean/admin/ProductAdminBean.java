/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.admin;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ProductController;
import com.gpm.model.Product;

@ManagedBean
@ViewScoped
public class ProductAdminBean implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * The currently selected product.
   */
  private Product selected;

  public List<Product> getAll() {
    List<Product> all = null;
    try {
      all = ProductController.getInstance().getAll(null, true);
    } catch (ControllerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return all;
  }

  public Product getSelected() {
    return selected;
  }

  public void setSelected(Product selected) {
    this.selected = selected;
  }

  public void delete() {
    if (selected != null) {
      try {
        ProductController.getInstance().delete(selected.getId());
      } catch (ControllerException e) {
        e.printStackTrace();
      }
    }
    selected = null;
  }
}
