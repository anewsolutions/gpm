/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DualListModel;

import com.gpm.controller.CategoryController;
import com.gpm.controller.ControllerException;
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
   * Pre-loaded product list.
   */
  private List<Product> products;

  /**
   * The currently selected category.
   */
  private Category selected;

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
    try {
      products = ProductManager.findAll();
    } catch (ProductException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public DualListModel<Product> getProducts() {
    DualListModel<Product> model = new DualListModel<Product>();
    model.setSource(products);
    return model;
  }

  public List<Category> getAll() {
    List<Category> all = null;
    try {
      all = CategoryController.getInstance().getAll(null, true);
    } catch (ControllerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return all;
  }

  public List<Product> getAllProductsInCategory(Category category) {
    List<Product> products = new ArrayList<Product>(category.getProducts());
    return products;
  }

  public Category getSelected() {
    return selected;
  }

  public void setSelected(Category selected) {
    this.selected = selected;
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
