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

import com.gpm.manager.LinkManager;
import com.gpm.manager.exception.LinkException;
import com.gpm.model.Link;

@ManagedBean
@ViewScoped
public class LinkAdminBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private boolean editing;
  private Link selected;

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
        selected = LinkManager.findByUuid(uuid);
        editing = true;
      } else {
        selected = new Link();
      }
    } catch (LinkException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * JSF method to determine whether we are editing a link or adding a new link.
   * 
   * @return true if editing an existing link, false if adding a new link
   */
  public boolean isEditing() {
    return editing;
  }

  /**
   * JSF method to provide access to the currently selected link.
   * 
   * @return the selected link
   */
  public Link getSelected() {
    return selected;
  }

  /**
   * JSF method to provide access to all links.
   * 
   * @return a list of links
   */
  public List<Link> getAll() {
    List<Link> all = null;
    try {
      all = LinkManager.findAllLinks();
    } catch (LinkException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return all;
  }

  /**
   * JSF method to save the selected link.
   */
  public String save() {
    try {
      LinkManager.save(selected);
    } catch (LinkException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/link_list?faces-redirect=true";
  }

  /**
   * JSF method to delete the selected link.
   */
  public String delete() {
    try {
      LinkManager.delete(selected);
    } catch (LinkException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/link_list?faces-redirect=true";
  }
}
