/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class RecoveryBean implements Serializable {
  private static final long serialVersionUID = 1L;

  // Details for account recovery
  private String email;
  private boolean sent;

  public void recover() {
    //TODO: send email
    setSent(true);
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public boolean isSent() {
    return sent;
  }

  public void setSent(final boolean sent) {
    this.sent = sent;
  }
}
