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
 * 
 * @author mbooth
 */
@FacesValidator("EmailUniquenessValidator")
public class EmailUniquenessValidator implements Validator {

  @Override
  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    UserAccount account = null;
    try {
      account = UserAccountManager.findByEmail(value.toString());
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (account != null) {
      // Given email address is already in the database
      FacesMessage message = new FacesMessage(MessageProvider.getMessage(component.getId() + "Unique"));
      message.setSeverity(FacesMessage.SEVERITY_ERROR);
      throw new ValidatorException(message);
    }
  }
}
