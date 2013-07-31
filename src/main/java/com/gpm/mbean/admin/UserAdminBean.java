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

import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.UserAccount;

@ManagedBean
@ViewScoped
public class UserAdminBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private boolean editing;
  private UserAccount selected;
  private String password;

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
        selected = UserAccountManager.findByUuid(uuid);
        editing = true;
      } else {
        selected = new UserAccount();
      }
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * JSF method to determine whether we are editing a user or adding a new user.
   * 
   * @return true if editing an existing user, false if adding a new user
   */
  public boolean isEditing() {
    return editing;
  }

  /**
   * JSF method to provide access to the currently selected user.
   * 
   * @return the selected user
   */
  public UserAccount getSelected() {
    return selected;
  }

  /**
   * JSF method for accessing the new account password.
   * 
   * @return the new plain text password
   */
  public String getPassword() {
    return password;
  }

  /**
   * JSF method for setting a new account password.
   * 
   * @param password
   *          a new plain text password
   */
  public void setPassword(final String password) {
    this.password = password;
  }

  /**
   * JSF method to provide access to all users.
   * 
   * @return a list of users
   */
  public List<UserAccount> getAll() {
    List<UserAccount> all = null;
    try {
      all = UserAccountManager.findAllUserAccounts();
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return all;
  }

  /**
   * JSF method to save the selected user.
   */
  public String save() {
    try {
      UserAccountManager.save(selected);
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/user_list?faces-redirect=true";
  }

  /**
   * JSF method to delete the selected user.
   */
  public String delete() {
    try {
      UserAccountManager.delete(selected);
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/user_list?faces-redirect=true";
  }

  /**
   * JSF method to reset the password of the selected user.
   */
  public String resetPassword() {
    try {
      UserAccountManager.save(selected, password, true);
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/user_list?faces-redirect=true";
  }
}
