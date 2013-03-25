/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.jsf.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.gpm.i18n.MessageProvider;

@FacesValidator("EmailValidator")
public class EmailValidator implements Validator {

  private static final Pattern PATTERN = Pattern.compile("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)");

  @Override
  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    Matcher matcher = PATTERN.matcher(value.toString());
    if (!matcher.matches()) {
      FacesMessage message = new FacesMessage(MessageProvider.getMessage(component.getId() + "Required"));
      message.setSeverity(FacesMessage.SEVERITY_ERROR);
      throw new ValidatorException(message);
    }
  }
}
