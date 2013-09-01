/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.gpm.i18n.MessageProvider;
import com.gpm.manager.PostageManager;
import com.gpm.manager.exception.PostageException;
import com.gpm.mbean.BeanUtils;
import com.gpm.model.Postage;
import com.gpm.model.enums.OrderType;

@ManagedBean
@ViewScoped
public class PostageAdminBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private boolean editing;
  private Postage selected;

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
        selected = PostageManager.findByUuid(uuid);
        editing = true;
      } else {
        selected = new Postage();
      }
    } catch (PostageException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * JSF method to determine whether we are editing a postage or adding a new postage.
   * 
   * @return true if editing an existing postage, false if adding a new postage
   */
  public boolean isEditing() {
    return editing;
  }

  /**
   * JSF method to provide access to the currently selected postage.
   * 
   * @return the selected postage
   */
  public Postage getSelected() {
    return selected;
  }

  /**
   * JSF method to provide access to all postage costs for magazines only orders.
   * 
   * @return a list of postage costs
   */
  public List<Postage> getAllMagazinesOnly() {
    List<Postage> all = new ArrayList<Postage>();
    try {
      all.addAll(PostageManager.findAllByOrderType(OrderType.MAGAZINES_ONLY));
    } catch (PostageException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return all;
  }

  /**
   * JSF method to provide access to all postage costs for orders with merchandise.
   * 
   * @return a list of postage costs
   */
  public List<Postage> getAllWithMerchandise() {
    List<Postage> all = new ArrayList<Postage>();
    try {
      all.addAll(PostageManager.findAllByOrderType(OrderType.WITH_PRODUCTS));
    } catch (PostageException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return all;
  }

  /**
   * JSF method to get a human-friendly description of the shipping category.
   */
  public String getShippingCategory() {
    Postage postage;
    if (editing) {
      postage = selected;
    } else {
      FacesContext context = FacesContext.getCurrentInstance();
      postage = context.getApplication().evaluateExpressionGet(context, "#{postage}", Postage.class);
    }
    return MessageProvider.getMessage("adminPostageTableShippingColumn" + postage.getShippingCategory());
  }

  /**
   * JSF method to get a human-friendly description of the order type category.
   */
  public String getOrderTypeCategory() {
    Postage postage;
    if (editing) {
      postage = selected;
    } else {
      FacesContext context = FacesContext.getCurrentInstance();
      postage = context.getApplication().evaluateExpressionGet(context, "#{postage}", Postage.class);
    }
    return MessageProvider.getMessage("adminPostageTableOrderTypeColumn" + postage.getOrderTypeCategory());
  }

  /**
   * JSF method to get a human-friendly representation of a shipping cost.
   */
  public String getShippingCost() {
    FacesContext context = FacesContext.getCurrentInstance();
    Integer cost = context.getApplication().evaluateExpressionGet(context, "#{bandCost.weightCost}", Integer.class);
    return BeanUtils.formatPrice(cost);
  }

  /**
   * JSF method to save the selected postage cost.
   */
  public String save() {
    try {
      PostageManager.save(selected);
    } catch (PostageException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/postage_list?faces-redirect=true";
  }
}
