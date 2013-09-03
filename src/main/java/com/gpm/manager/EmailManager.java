/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.util.List;
import java.util.UUID;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ControllerFactory;
import com.gpm.i18n.MessageProvider;
import com.gpm.manager.exception.EmailException;
import com.gpm.model.Email;

public class EmailManager {
  /**
   * Get the email with the given UUID.
   * 
   * @param uuid
   *          the UUID of the email requested
   * @return a email or null if no email was found for the given UUID
   * @throws EmailException
   *           if there was a problem fetching the email
   */
  public static Email findByUuid(final String uuid) throws EmailException {
    try {
      return ControllerFactory.getEmailController().get(UUID.fromString(uuid));
    } catch (IllegalArgumentException e) {
      throw new EmailException(e);
    } catch (ControllerException e) {
      throw new EmailException(e);
    }
  }

  /**
   * Get all emails.
   * 
   * @return the list of emails
   * @throws EmailException
   *           if there was a problem fetching the list of emails
   */
  public static List<Email> findAllEmails() throws EmailException {
    try {
      return ControllerFactory.getEmailController().getAll();
    } catch (ControllerException e) {
      throw new EmailException(e);
    }
  }

  public static void createResetAccountNotFoundEmail(final String recipientAddress) throws EmailException {
    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    HttpServletRequest request = (HttpServletRequest) context.getRequest();
    String server = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    String loginLink = server + "/secure/login.xhtml";

    StringBuilder s = new StringBuilder("<html><head></head><body>");
    s.append("<p>" + MessageProvider.getMessage("emailTextRecoveryNotFound1", recipientAddress) + "</p>");
    s.append("<p>" + MessageProvider.getMessage("emailTextRecoveryNotFound2", loginLink) + "</p>");
    s.append("<p>" + MessageProvider.getMessage("emailTextRecoveryNotFound3") + "</p>");
    s.append("<p>");
    s.append(MessageProvider.getMessage("emailTextBestRegards"));
    s.append("</p>");
    s.append("</body></html>");

    Email email = new Email();
    email.setRecipientAddress(recipientAddress);
    email.setSubject(MessageProvider.getMessage("emailTextRecoverySubject"));
    email.setBody(s.toString());
    save(email);
  }

  public static void createResetAccountEmail(final String recipientAddress, final String resetToken) throws EmailException {
    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    HttpServletRequest request = (HttpServletRequest) context.getRequest();
    String server = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    String resetLink = server + "/secure/reset.xhtml?resetToken=" + resetToken;

    StringBuilder s = new StringBuilder("<html><head></head><body>");
    s.append("<p>" + MessageProvider.getMessage("emailTextRecoveryReset1", recipientAddress) + "</p>");
    s.append("<p>" + MessageProvider.getMessage("emailTextRecoveryReset2", resetLink) + "</p>");
    s.append("<p>");
    s.append(MessageProvider.getMessage("emailTextBestRegards"));
    s.append("</p>");
    s.append("</body></html>");

    Email email = new Email();
    email.setRecipientAddress(recipientAddress);
    email.setSubject(MessageProvider.getMessage("emailTextRecoverySubject"));
    email.setBody(s.toString());
    save(email);
  }

  /**
   * Persist the given email to the data store.
   * 
   * @param email
   *          the email to be saved
   * @throws EmailException
   *           if there was a problem saving the email
   */
  public static void save(final Email email) throws EmailException {
    try {
      ControllerFactory.getEmailController().save(email);
    } catch (ControllerException e) {
      throw new EmailException(e);
    }
  }

  /**
   * Delete the given email from the data store.
   * 
   * @param email
   *          the email to be deleted
   * @throws EmailException
   *           if there was a problem deleting the email
   */
  public static void delete(final Email email) throws EmailException {
    try {
      ControllerFactory.getEmailController().delete(email.getUuid());
    } catch (ControllerException e) {
      throw new EmailException(e);
    }
  }
}
