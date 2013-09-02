/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.mbean.BeanUtils;
import com.gpm.model.UserAccount;

@ManagedBean
@ViewScoped
public class ResetPasswordBean implements Serializable {
  private static final long serialVersionUID = 1L;

  // Account whose password will be reset
  private UserAccount account = null;

  // New password
  private String password;

  @PostConstruct
  public void init() {
    try {
      // Try looking up account by reset token
      FacesContext fc = FacesContext.getCurrentInstance();
      String resetToken = fc.getExternalContext().getRequestParameterMap().get("resetToken");
      if (resetToken != null && !resetToken.isEmpty()) {
        account = UserAccountManager.findByResetToken(resetToken);
      }
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String resetPassword() {
    try {
      // Clear reset token
      account.setResetToken(null);
      account.setResetTokenExpiry(null);
      UserAccountManager.save(account, password);
      // Login through the session bean
      LoginBean login = BeanUtils.fetchLoginBean();
      login.setEmail(getAccountEmail());
      login.login();
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/index.xhtml?faces-redirect=true";
  }

  public String getAccountEmail() {
    return account.getEmail();
  }

  public boolean isTokenValid() {
    return account != null;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }
}
