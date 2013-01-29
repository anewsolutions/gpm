/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.gpm.manager.VariantManager;
import com.gpm.manager.exception.VariantException;
import com.gpm.model.Variant;

@FacesConverter("VariantConverter")
public class VariantConverter implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    try {
      return VariantManager.find(Integer.parseInt(value));
    } catch (NumberFormatException e) {
      throw new ConverterException(e);
    } catch (VariantException e) {
      throw new ConverterException(e);
    }
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    return Integer.toString(((Variant) value).getId());
  }
}
