/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.net.URI;
import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.gpm.login.ThirdPartyLoginServlet;
import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.UserAccount;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
  private static final long serialVersionUID = 1L;

  // URI to redirect to after successfully logging in
  private URI redirect;

  // Details for a user logging in with GPM account
  private String email;

  // UUID of currently logged in user
  private UUID user;

  public String login() {
    try {
      UserAccount account = UserAccountManager.findByEmail(email);
      if (account != null) {
        user = account.getUuid();
      }
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
      UserAccount account = UserAccountManager.findByFacebookIdent(facebookIdent);
      if (account != null) {
        user = account.getUuid();
      }
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

  public boolean isAdministrator() {
    if (isLoggedIn()) {
      UserAccount account = null;
      try {
        account = UserAccountManager.findByUuid(user.toString());
      } catch (UserAccountException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return account != null && account.isAdministrator();
    }
    return false;
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

  public UUID getUserUuid() {
    return user;
  }

  public String getFacebookLoginUrl() {
    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    return ThirdPartyLoginServlet.getFacebookLoginUrl((HttpServletRequest) context.getRequest());
  }

  public String getGoogleLoginUrl() {
    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    return ThirdPartyLoginServlet.getGoogleLoginUrl((HttpServletRequest) context.getRequest());
  }
}
