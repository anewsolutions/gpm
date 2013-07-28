/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.jsf.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.gpm.i18n.MessageProvider;
import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.UserAccount;

/**
 * Simple JSF validator to check that email addresses are unique. Validation fails if a
 * given email address is already known to the database.
 * <p>
 * If another existing email address is passed in the "old" parameter, then validation
 * does not fail when the given email address is the same as the old one, even if it is
 * known to the database. This is useful when we are modifying a pre-existing email
 * address.
 * 
 * @author mbooth
 */
@FacesValidator("EmailUniquenessValidator")
public class EmailUniquenessValidator implements Validator {

  @Override
  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    String email = value.toString();
    // Succeed early if the given email address is the same as the old one
    String old = (String) component.getAttributes().get("old");
    if (old != null && !old.isEmpty() && old.equals(email)) {
      return;
    }
    // Query database for an account with the given email address
    try {
      UserAccount account = UserAccountManager.findByEmail(email);
      if (account != null) {
        // Given email address is already in the database
        FacesMessage message = new FacesMessage(MessageProvider.getMessage("validatorEmailUniqueness"));
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        throw new ValidatorException(message);
      }
    } catch (UserAccountException e) {
      FacesMessage message = new FacesMessage(e.getMessage());
      message.setSeverity(FacesMessage.SEVERITY_ERROR);
      throw new ValidatorException(message);
    }
  }
}
