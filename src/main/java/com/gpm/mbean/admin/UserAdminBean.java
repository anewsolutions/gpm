/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.admin;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.codec.binary.Hex;

import com.gpm.manager.EmailManager;
import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.EmailException;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.UserAccount;

@ManagedBean
@ViewScoped
public class UserAdminBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private boolean editing;
  private UserAccount selected;

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
   * JSF method to trigger account recovery process for a user.
   */
  public String recoverAccount() {
    try {
      // Generate a random key
      SecureRandom random = new SecureRandom();
      byte[] key = new byte[32];
      random.nextBytes(key);
      String resetToken = Hex.encodeHexString(key);
      EmailManager.createResetAccountEmail(selected.getEmail(), resetToken);
      selected.setResetToken(resetToken);
      selected.setResetTokenExpiry(new Date (System.currentTimeMillis() + 86400000l));
      UserAccountManager.save(selected);
    } catch (EmailException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/user_list?faces-redirect=true";
  }
}
