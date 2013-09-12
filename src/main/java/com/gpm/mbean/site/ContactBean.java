/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gpm.i18n.MessageProvider;
import com.gpm.manager.ConfigurationManager;
import com.gpm.manager.EmailManager;
import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.ConfigurationException;
import com.gpm.manager.exception.EmailException;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.UserAccount;

@ManagedBean
@ViewScoped
public class ContactBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private String email;
  private String name;
  private String reason;
  private String message;
  private boolean sent;

  // Email recipient lists
  private String editorial;
  private String technical;

  @PostConstruct
  public void init() {
    try {
      UserAccount user = UserAccountManager.findCurrentlyLoggedIn();
      if (user != null) {
        email = user.getEmail();
        name = user.getName();
      }
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      Properties props = ConfigurationManager.findPropsByKey("contact.us");
      editorial = props.getProperty("contact.us.editorial");
      technical = props.getProperty("contact.us.technical");
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void contact() {
    try {
      String subject = MessageProvider.getMessage("contactEmailSubject" + reason);
      String recipientAddress = editorial;
      if ("PROBLEM".equals(reason)) {
        recipientAddress = technical;
      }
      EmailManager.createContactFormEmail(recipientAddress, email, name, subject, message);
      setSent(true);
    } catch (EmailException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
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

  public String getReason() {
    return reason;
  }

  public void setReason(final String reason) {
    this.reason = reason;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public boolean isSent() {
    return sent;
  }

  public void setSent(final boolean sent) {
    this.sent = sent;
  }

  public String[] getEditorialStaff() {
    if (editorial != null) {
      return editorial.split(",");
    } else {
      return new String[0];
    }
  }

  public String[] getTechnicalStaff() {
    if (technical != null) {
      return technical.split(",");
    } else {
      return new String[0];
    }
  }
  
  public String[] getAddress() {
    // TODO get from config
    String address = "Guinea Pig Magazine|PO Box 4915|Chesterfield|S44 9BD|United Kingdom";
    return address.split("\\|");
  }
}
