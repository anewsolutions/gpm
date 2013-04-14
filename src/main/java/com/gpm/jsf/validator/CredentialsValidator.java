/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.jsf.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.gpm.i18n.MessageProvider;
import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.UserAccount;

/**
 * JSF validator for authenticating GPM account credentials. Validation fails if there is
 * no account for the email address or if the password is not valid for the account
 * belonging to the email address.
 * 
 * @author mbooth
 */
@FacesValidator("CredentialsValidator")
public class CredentialsValidator implements Validator {

  @Override
  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    UIInput emailComponent = (UIInput) component.getAttributes().get("emailComponent");
    String email = emailComponent.getValue().toString();
    String password = value.toString();
    UserAccount account = null;
    try {
      account = UserAccountManager.findByCredentials(email, password);
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (account == null) {
      // Given email is not known to the database
      FacesMessage message = new FacesMessage(MessageProvider.getMessage(component.getId() + "InvalidCreds"));
      message.setSeverity(FacesMessage.SEVERITY_ERROR);
      throw new ValidatorException(message);
    }
  }
}
