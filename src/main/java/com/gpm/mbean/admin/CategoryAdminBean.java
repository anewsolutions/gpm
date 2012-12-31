/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DualListModel;

import com.gpm.manager.CategoryManager;
import com.gpm.manager.ProductManager;
import com.gpm.manager.exception.CategoryException;
import com.gpm.manager.exception.ProductException;
import com.gpm.model.Category;
import com.gpm.model.Product;

@ManagedBean
@ViewScoped
public class CategoryAdminBean implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * The currently selected category.
   */
  private Category selected;

  private DualListModel<Product> productsPickList;

  @PostConstruct
  public void init() {
    try {
      // Load the selected category, if an ID is passed as a query parameter
      FacesContext fc = FacesContext.getCurrentInstance();
      String id = fc.getExternalContext().getRequestParameterMap().get("id");
      try {
        int idValue = Integer.parseInt(id);
        selected = CategoryManager.find(idValue);
      } catch (NumberFormatException e) {
        selected = new Category();
      }
    } catch (CategoryException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public List<Category> getAll() {
    List<Category> all = null;
    try {
      all = CategoryManager.findAll();
    } catch (CategoryException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return all;
  }

  public Category getSelected() {
    return selected;
  }

  public void setSelected(Category selected) {
    this.selected = selected;
  }

  public DualListModel<Product> getProductsPickList() {
    if (productsPickList == null) {
      productsPickList = new DualListModel<Product>();
      try {
        productsPickList.setSource(ProductManager.findAvailable(selected));
        productsPickList.setTarget(selected.getProductsAsList());
      } catch (ProductException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return productsPickList;
  }

  public void setProductsPickList(DualListModel<Product> productsPickList) {
    this.productsPickList = productsPickList;
  }

  public String save() {
    try {
      CategoryManager.save(selected);
    } catch (CategoryException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/admin/categories/index?faces-redirect=true";
  }

  public String delete() {
    try {
      CategoryManager.delete(selected);
    } catch (CategoryException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/admin/categories/index?faces-redirect=true";
  }
}
