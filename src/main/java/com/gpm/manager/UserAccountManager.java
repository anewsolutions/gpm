/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ControllerFactory;
import com.gpm.controller.ControllerFilter;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.UserAccount;

public class UserAccountManager {
  /**
   * Get the user account with the given UUID.
   * 
   * @param uuid
   *          the UUID of the user account requested
   * @return a user account
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

  public static void createNew(final String email, final String name, final String password)
      throws UserAccountException {
    // Generate a random salt
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[32];
    random.nextBytes(salt);

    // Hash password with salt
    byte[] hash = hashPassword(salt, password.getBytes());

    // Store account
    UserAccount account = new UserAccount();
    account.setEmail(email);
    account.setName(name);
    account.setPasswordSalt(Hex.encodeHexString(salt));
    account.setPasswordHash(Hex.encodeHexString(hash));
    try {
      ControllerFactory.getUserAccountController().save(account);
    } catch (ControllerException e) {
      throw new UserAccountException(e);
    }
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
    try {
      ControllerFactory.getUserAccountController().save(account);
    } catch (ControllerException e) {
      throw new UserAccountException(e);
    }
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
   * Persist the given user account to the data store.
   * 
   * @param account
   *          the user account to be saved
   * @throws UserAccountException
   *           if there was a problem saving the user account
   */
  public static void storeUserAccount(final UserAccount account) throws UserAccountException {
    try {
      ControllerFactory.getUserAccountController().save(account);
    } catch (ControllerException e) {
      throw new UserAccountException(e);
    }
  }
}
