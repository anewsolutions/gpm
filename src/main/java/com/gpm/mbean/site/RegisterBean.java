/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.net.URI;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.mbean.BeanUtils;

@ManagedBean
@ViewScoped
public class RegisterBean implements Serializable {
  private static final long serialVersionUID = 1L;

  // Details for new account registration
  private String email;
  private String name;
  private String password;

  public String register() {
    try {
      UserAccountManager.createNew(email, name, password);
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    URI redirect = BeanUtils.fetchLoginBean().getRedirect();
    if (redirect != null) {
      return redirect.getPath() + "?faces-redirect=true";
    } else {
      return "/index.xhtml?faces-redirect=true";
    }
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }
}
