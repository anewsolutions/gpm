/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 * Utility to get translated messages from the application's resource bundle.
 * 
 * @author mbooth
 */
public class MessageProvider {

  /**
   * Get a translated message with the specified key.
   * 
   * @param key
   *          the key into the resource bundle
   * @return a message appropriate for the current locale
   */
  public static String getMessage(String key) {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ResourceBundle rb = ctx.getApplication().getResourceBundle(ctx, "msg");
    return rb.getString(key);
  }

  /**
   * Get a translated message with the specified key, substituting the given parameters
   * into the message.
   * 
   * @param key
   *          the key into the resource bundle
   * @param params
   *          values to substitute into the translated string
   * @return a message appropriate for the current locale
   */
  public static String getMessage(String key, Object... params) {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ResourceBundle rb = ctx.getApplication().getResourceBundle(ctx, "msg");
    return MessageFormat.format(rb.getString(key), params);
  }
}
