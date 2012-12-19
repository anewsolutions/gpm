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

import org.primefaces.model.DualListModel;

import com.gpm.controller.CategoryController;
import com.gpm.controller.ControllerException;
import com.gpm.controller.ProductController;
import com.gpm.i18n.MessageProvider;
import com.gpm.manager.CategoryManager;
import com.gpm.manager.exception.CategoryException;
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
  private Category selected = new Category();

  @PostConstruct
  public void init() {
    try {
      products = ProductController.getInstance().getAll(null, true);
    } catch (ControllerException e) {
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

  public void saveAdd() {
    try {
      System.out.println("saveAdd");
      CategoryManager.create(selected);
      selected = new Category();
    } catch (CategoryException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void saveEdit() {
    try {
      System.out.println("saveEdit");
      CategoryManager.update(selected);
      selected = new Category();
    } catch (CategoryException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void delete() {
    try {
      CategoryManager.delete(selected);
      selected = new Category();
    } catch (CategoryException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String getDeleteText() {
    return MessageProvider.getMessage("adminDialogDeleteCategoryText", selected.getName());
  }
}
