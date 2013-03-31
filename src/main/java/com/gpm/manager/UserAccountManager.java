/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ControllerFactory;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.UserAccount;

public class UserAccountManager {
  public static List<UserAccount> findByEmail(final String email) throws UserAccountException {
    try {
      Map<String, Object> attributes = new HashMap<String, Object>();
      attributes.put("email", email);
      return ControllerFactory.getUserAccountController().getAll(attributes);
    } catch (ControllerException e) {
      throw new UserAccountException(e);
    }
  }

  public static void createNew(final String email, final String name, final String password) throws UserAccountException {
    // Generate a random salt
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[32];
    random.nextBytes(salt);

    // Hash password with salt
    byte[] data = new byte[salt.length + password.getBytes().length];
    System.arraycopy(salt, 0, data, 0, salt.length);
    System.arraycopy(password.getBytes(), 0, data, salt.length, password.getBytes().length);
    byte[] hash = DigestUtils.sha512(data);

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
}
