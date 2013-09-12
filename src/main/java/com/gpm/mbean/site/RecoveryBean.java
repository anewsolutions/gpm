/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.codec.binary.Hex;

import com.gpm.manager.EmailManager;
import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.EmailException;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.UserAccount;

@ManagedBean
@ViewScoped
public class RecoveryBean implements Serializable {
  private static final long serialVersionUID = 1L;

  // Details for account recovery
  private String recipient;
  private boolean sent;

  public void recover() {
    // Try looking up account by email
    UserAccount account = null;
    try {
      account = UserAccountManager.findByEmail(recipient);
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // Store and return
    try {
      if (account == null) {
        EmailManager.createResetAccountNotFoundEmail(recipient);
      } else {
        // Generate a random key
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[32];
        random.nextBytes(key);
        String resetToken = Hex.encodeHexString(key);
        EmailManager.createResetAccountEmail(recipient, resetToken);
        account.setResetToken(resetToken);
        account.setResetTokenExpiry(new Date (System.currentTimeMillis() + 86400000l));
        UserAccountManager.save(account);
      }
      setSent(true);
    } catch (EmailException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(final String recipient) {
    this.recipient = recipient;
  }

  public boolean isSent() {
    return sent;
  }

  public void setSent(final boolean sent) {
    this.sent = sent;
  }
}
