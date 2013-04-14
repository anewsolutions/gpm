/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.net.URI;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.gpm.ThirdPartyLoginServlet;
import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.UserAccount;

@ManagedBean
@SessionScoped
public class LogInBean implements Serializable {
  private static final long serialVersionUID = 1L;

  // URI to redirect to after successfully logging in
  private URI redirect;

  // Details for a user logging in with GPM account
  private String email;

  // Currently logged in user
  private UserAccount user;

  public String login() {
    try {
      user = UserAccountManager.findByEmail(email);
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (redirect != null) {
      return redirect.getPath() + "?faces-redirect=true";
    } else {
      return "/index.xhtml?faces-redirect=true";
    }
  }

  public void loginFacebook(final String facebookIdent) {
    try {
      user = UserAccountManager.findByFacebookIdent(facebookIdent);
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String logout() {
    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    redirect = null;
    email = null;
    user = null;
    return "/index.xhtml?faces-redirect=true";
  }

  public boolean isLoggedIn() {
    return user != null;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public URI getRedirect() {
    return redirect;
  }

  public void setRedirect(final URI redirect) {
    this.redirect = redirect;
  }

  public UserAccount getUser() {
    return user;
  }

  public String getFacebookLoginUrl() {
    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    return ThirdPartyLoginServlet.getFacebookLoginUrl((HttpServletRequest) context.getRequest());
  }
}
