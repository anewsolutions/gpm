/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.jsf.validator;

import java.util.ArrayList;
import java.util.List;

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
 * Simple JSF validator to check if given email addresses are already known to the
 * database.
 * 
 * @author mbooth
 */
@FacesValidator("EmailUniquenessValidator")
public class EmailUniquenessValidator implements Validator {

  @Override
  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    List<UserAccount> accounts = new ArrayList<UserAccount>();
    try {
      accounts = UserAccountManager.findByEmail(value.toString());
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (accounts.size() > 0) {
      // Given email address is already in the database
      FacesMessage message = new FacesMessage(MessageProvider.getMessage(component.getId() + "Unique"));
      message.setSeverity(FacesMessage.SEVERITY_ERROR);
      throw new ValidatorException(message);
    }
  }
}
