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

/**
 * JSF validator to check if a password and a confirmation password are the same.
 * Validation fails if they are not the same.
 * 
 * @author mbooth
 */
@FacesValidator("ConfirmPasswordValidator")
public class ConfirmPasswordValidator implements Validator {

  @Override
  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    UIInput passwordComponent = (UIInput) component.getAttributes().get("passwordComponent");
    String password = passwordComponent.getValue().toString();
    String passwordConfirm = value.toString();
    if (!passwordConfirm.equals(password)) {
      // Given passwords do not match
      FacesMessage message = new FacesMessage(MessageProvider.getMessage("validatorPasswordsDoNotMatch"));
      message.setSeverity(FacesMessage.SEVERITY_ERROR);
      throw new ValidatorException(message);
    }
  }
}
