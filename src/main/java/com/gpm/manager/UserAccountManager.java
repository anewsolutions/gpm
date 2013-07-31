/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ControllerFactory;
import com.gpm.controller.ControllerFilter;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.mbean.site.LoginBean;
import com.gpm.model.UserAccount;

public class UserAccountManager {
  /**
   * Get the user account with the given UUID.
   * 
   * @param uuid
   *          the UUID of the user account requested
   * @return a user account or null if no account exists for the given UUID
   * @throws UserAccountException
   *           if there was a problem fetching the user account
   */
  public static UserAccount findByUuid(final String uuid) throws UserAccountException {
    try {
      return ControllerFactory.getUserAccountController().get(UUID.fromString(uuid));
    } catch (IllegalArgumentException e) {
      throw new UserAccountException(e);
    } catch (ControllerException e) {
      throw new UserAccountException(e);
    }
  }

  /**
   * Get the user account that is currently logged into the session.
   * 
   * @return the user account of the user currently logged in, null if no user is logged
   *         in
   * @throws UserAccountException
   *           there was a problem fetching the user account
   */
  public static UserAccount findCurrentlyLoggedIn() throws UserAccountException {
    try {
      HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
      LoginBean bean = (LoginBean) req.getSession().getAttribute("loginBean");
      if (bean != null && bean.getUserUuid() != null) {
        return ControllerFactory.getUserAccountController().get(bean.getUserUuid());
      } else {
        return null;
      }
    } catch (IllegalArgumentException e) {
      throw new UserAccountException(e);
    } catch (ControllerException e) {
      throw new UserAccountException(e);
    }
  }

  public static UserAccount findByEmail(final String email) throws UserAccountException {
    try {
      List<ControllerFilter> filters = new ArrayList<ControllerFilter>();
      filters.add(new ControllerFilter("email", "=", email));
      List<UserAccount> accounts = ControllerFactory.getUserAccountController().getAll(filters);
      // Email addresses are unique, so should be safe to return first result only
      if (!accounts.isEmpty()) {
        return accounts.get(0);
      } else {
        return null;
      }
    } catch (ControllerException e) {
      throw new UserAccountException(e);
    }
  }

  public static UserAccount findByFacebookIdent(final String facebookIdent) throws UserAccountException {
    try {
      List<ControllerFilter> filters = new ArrayList<ControllerFilter>();
      filters.add(new ControllerFilter("facebookIdent", "=", facebookIdent));
      List<UserAccount> accounts = ControllerFactory.getUserAccountController().getAll(filters);
      // Facebook identities are unique, so should be safe to return first result only
      if (!accounts.isEmpty()) {
        return accounts.get(0);
      } else {
        return null;
      }
    } catch (ControllerException e) {
      throw new UserAccountException(e);
    }
  }

  public static UserAccount findByCredentials(final String email, final String password) throws UserAccountException {
    UserAccount account = findByEmail(email);
    if (account != null) {
      try {
        // Get the stored salt from the user account
        byte[] salt = Hex.decodeHex(account.getPasswordSalt().toCharArray());

        // Hash supplied password with the user's salt
        byte[] hash = hashPassword(salt, password.getBytes());

        // Return the account if the computed hash matches the stored hash
        if (Hex.encodeHexString(hash).equalsIgnoreCase(account.getPasswordHash())) {
          return account;
        }
      } catch (DecoderException e) {
        throw new UserAccountException(e);
      }
    }
    return null;
  }

  /**
   * Get all user accounts.
   * 
   * @return the list of user accounts
   * @throws UserAccountException
   *           if there was a problem fetching the list of user accounts
   */
  public static List<UserAccount> findAllUserAccounts() throws UserAccountException {
    try {
      return ControllerFactory.getUserAccountController().getAll();
    } catch (ControllerException e) {
      throw new UserAccountException(e);
    }
  }

  public static void createNew(final String email, final String name, final String password)
      throws UserAccountException {
    // Store account
    UserAccount account = new UserAccount();
    account.setEmail(email);
    account.setName(name);
    resetPassword(account, password);
    save(account);
  }

  public static void createNewFacebook(final String email, final String name, final String facebookIdent,
      final String facebookToken) throws UserAccountException {
    // Store account
    UserAccount account = findByFacebookIdent(facebookIdent);
    if (account == null) {
      account = new UserAccount();
    }
    account.setEmail(email);
    account.setName(name);
    account.setFacebookIdent(facebookIdent);
    account.setFacebookToken(facebookToken);
    save(account);
  }

  /**
   * Utility to generate a hash sum of salt and password.
   * 
   * @param salt
   *          random data to prepend to the password before hashing
   * @param password
   *          password to be hashed
   * @return the computed hash sum of the specified salt and password
   */
  private static byte[] hashPassword(final byte[] salt, final byte[] password) {
    byte[] data = new byte[salt.length + password.length];
    System.arraycopy(salt, 0, data, 0, salt.length);
    System.arraycopy(password, 0, data, salt.length, password.length);
    return DigestUtils.sha512(data);
  }

  /**
   * Utility to reset the password on an account using the given password and a new random
   * salt.
   * 
   * @param account
   *          the account on which the password will be changed
   * @param password
   *          the plain text to use as the new account password
   */
  private static void resetPassword(final UserAccount account, final String password) {
    // Generate a random salt
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[32];
    random.nextBytes(salt);

    // Hash password with salt
    byte[] hash = hashPassword(salt, password.getBytes());

    // Store new password and salt in the account
    account.setPasswordSalt(Hex.encodeHexString(salt));
    account.setPasswordHash(Hex.encodeHexString(hash));
  }

  /**
   * Persist the given user account to the data store.
   * 
   * @param account
   *          the user account to be saved
   * @throws UserAccountException
   *           if there was a problem saving the user account
   */
  public static void save(final UserAccount account) throws UserAccountException {
    save(account, null);
  }

  /**
   * Persist the given user account to the data store with a new password. If null or the
   * empty string is passed in the password parameter then the password will not be
   * changed.
   * 
   * @param account
   *          the user account to be saved
   * @param password
   *          the plain text to use as the new account password
   * @throws UserAccountException
   *           if there was a problem saving the user account
   */
  public static void save(final UserAccount account, final String password) throws UserAccountException {
    try {
      if (password != null && !password.isEmpty()) {
        resetPassword(account, password);
      }
      ControllerFactory.getUserAccountController().save(account);
    } catch (ControllerException e) {
      throw new UserAccountException(e);
    }
  }

  /**
   * Delete the given user account from the data store.
   * 
   * @param account
   *          the user account to be deleted
   * @throws UserAccountException
   *           if there was a problem deleting the user account
   */
  public static void delete(final UserAccount account) throws UserAccountException {
    try {
      ControllerFactory.getUserAccountController().delete(account.getUuid());
    } catch (ControllerException e) {
      throw new UserAccountException(e);
    }
  }
}
