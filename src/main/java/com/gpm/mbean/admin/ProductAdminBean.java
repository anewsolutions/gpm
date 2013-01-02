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

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import com.gpm.manager.CategoryManager;
import com.gpm.manager.ProductManager;
import com.gpm.manager.exception.CategoryException;
import com.gpm.manager.exception.ProductException;
import com.gpm.model.Category;
import com.gpm.model.Product;

@ManagedBean
@ViewScoped
public class ProductAdminBean implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * The currently selected product.
   */
  private Product selected;

  private DualListModel<Category> categoriesPickList;
  private List<Category> categoriesAdded;
  private List<Category> categoriesRemoved;

  @PostConstruct
  public void init() {
    try {
      // Load the selected product, if an ID is passed as a query parameter
      FacesContext fc = FacesContext.getCurrentInstance();
      String id = fc.getExternalContext().getRequestParameterMap().get("id");
      try {
        int idValue = Integer.parseInt(id);
        selected = ProductManager.find(idValue);
      } catch (NumberFormatException e) {
        selected = new Product();
      }
    } catch (ProductException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public List<Product> getAll() {
    List<Product> all = null;
    try {
      all = ProductManager.findAll();
    } catch (ProductException e) {
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

  public DualListModel<Category> getCategoriesPickList() {
    if (categoriesPickList == null) {
      categoriesPickList = new DualListModel<Category>();
      try {
        categoriesPickList.setSource(CategoryManager.findAvailable(selected));
        categoriesPickList.setTarget(selected.getCategoriesAsList());
      } catch (CategoryException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return categoriesPickList;
  }

  public void setCategoriesPickList(DualListModel<Category> categoriesPickList) {
    this.categoriesPickList = categoriesPickList;
  }

  public void onTransferPickList(TransferEvent event) {
    List<Category> target;
    if (event.isAdd()) {
      if (categoriesAdded == null) {
        categoriesAdded = new ArrayList<Category>(event.getItems().size());
      }
      target = categoriesAdded;
    } else {
      if (categoriesRemoved == null) {
        categoriesRemoved = new ArrayList<Category>(event.getItems().size());
      }
      target = categoriesRemoved;
    }
    for(Object item : event.getItems()) {
      target.add((Category) item);
    }
  }

  public String save() {
    try {
      ProductManager.save(selected);
      ProductManager.addCategoriesToProduct(selected, categoriesAdded, categoriesRemoved);
    } catch (ProductException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/admin/products/index?faces-redirect=true";
  }

  public String delete() {
    try {
      ProductManager.delete(selected);
    } catch (ProductException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/admin/products/index?faces-redirect=true";
  }
}
