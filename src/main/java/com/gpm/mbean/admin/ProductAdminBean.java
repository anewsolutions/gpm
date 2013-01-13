/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.admin;

import java.io.Serializable;
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
import com.gpm.model.Variant;

@ManagedBean
@ViewScoped
public class ProductAdminBean implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * The currently selected product.
   */
  private Product selected;

  /**
   * The variant to be removed.
   */
  private Variant remove;

  private DualListModel<Category> categoriesPickList;

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
      // Must have as least one variant
      if (selected.getVariants().size() == 0) {
        addVariant();
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

  public DualListModel<Category> getCategoriesPickList() {
    if (categoriesPickList == null) {
      categoriesPickList = new DualListModel<Category>();
      try {
        categoriesPickList.setSource(CategoryManager.findCategoriesNotInProduct(selected));
        categoriesPickList.setTarget(CategoryManager.findCategoriesInProduct(selected));
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
    for(Object item : event.getItems()) {
      Category category = (Category) item;
      if (event.isAdd()) {
        selected.getCategories().add(category);
      } else {
        selected.getCategories().remove(category);
      }
    }
  }

  public String save() {
    try {
      ProductManager.save(selected);
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

  public Variant getRemove() {
    return remove;
  }

  public void setRemove(Variant remove) {
    this.remove = remove;
  }

  public void addVariant() {
    Variant v = new Variant();
    v.setName("");
    v.setCode(Integer.toString(selected.getVariants().size() + 1));
    if (selected.getVariants().size() > 0) {
      Variant prev = selected.getVariantsAsList().get(selected.getVariants().size() - 1);
      v.setPrice(prev.getPrice());
      v.setWeight(prev.getWeight());
      v.setStock(prev.getStock());
    }
    selected.getVariants().add(v);
  }

  public void removeVariant() {
    if (remove != null) {
      selected.getVariants().remove(remove);
    }
    remove = null;
  }
}
