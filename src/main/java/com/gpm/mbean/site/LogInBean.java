/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.gpm.model.UserAccount;

@ManagedBean
@SessionScoped
public class LogInBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private UserAccount user;

  public boolean isLoggedIn() {
    return false;
  }

  public UserAccount getUser() {
    return user;
  }
}
