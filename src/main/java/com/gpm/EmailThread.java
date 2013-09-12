/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gpm.manager.ConfigurationManager;
import com.gpm.manager.EmailManager;
import com.gpm.manager.exception.ConfigurationException;
import com.gpm.manager.exception.EmailException;
import com.gpm.model.Email;

/**
 * Simple email delivery thread.
 * 
 * @author mbooth
 */
public class EmailThread implements Runnable {

  private Log log = LogFactory.getLog(EmailThread.class);

  private volatile Thread thread;

  /**
   * Start the thread. This method is a no-op if the thread is already running.
   */
  void start() {
    if (thread == null) {
      thread = new Thread(new EmailThread(), "Email-Thread");
      thread.setDaemon(true);
      thread.start();
    }
  }

  /**
   * Stop the thread. Waits until the thread exits before returning. This method is a
   * no-op if the thread was not running.
   */
  void stop() throws InterruptedException {
    if (thread != null) {
      Thread t = thread;
      thread = null;
      t.interrupt();
      t.join();
    }
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      // Get smtp server configuration
      final Properties props = new Properties();
      try {
        props.putAll(ConfigurationManager.findPropsByKey("mail.smtp"));
      } catch (ConfigurationException e) {
        log.error("Error fetching SMTP server configuration: " + e.getMessage());
      }

      // Get pending emails to send
      final List<Email> emails = new ArrayList<Email>();
      try {
        emails.addAll(EmailManager.findAllEmails());
      } catch (EmailException e) {
        log.error("Error fetching list of pending emails: " + e.getMessage());
      }

      if (!props.isEmpty() && !emails.isEmpty()) {
        // Build mail session
        Authenticator authenticator = null;
        if (Boolean.parseBoolean(props.getProperty("mail.smtp.auth"))) {
          authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
              return new PasswordAuthentication(props.getProperty("mail.smtp.user"), props.getProperty("mail.smtp.pass"));
            }
          };
        }
        Session session = Session.getInstance(props, authenticator);
  
        Iterator<Email> i = emails.iterator();
        while (i.hasNext()) {
          Email email = i.next();
          // Attempt to send the email if it hasn't already failed lots of times
          if (email.getFailures() < 5) {
            boolean failed = false;
            try {
              MimeMessage msg = new MimeMessage(session);
              InternetAddress from = new InternetAddress(props.getProperty("mail.smtp.from"));
              InternetAddress[] recipients = InternetAddress.parse(email.getRecipientAddress());
              InternetAddress[] replyTo;
              if (email.getReplyAddress() == null || email.getReplyAddress().isEmpty()){
                replyTo = InternetAddress.parse(props.getProperty("mail.smtp.reply"));
              } else {
                replyTo = InternetAddress.parse(email.getReplyAddress());
              }
              msg.setFrom(from);
              msg.setReplyTo(replyTo);
              msg.setRecipients(MimeMessage.RecipientType.TO, recipients);
              msg.setSubject(email.getSubject(), "utf-8");
              msg.setText(email.getBody(), "utf-8", "html");
              Transport.send(msg);
            } catch (MessagingException e) {
              log.error("Failed to send email to " + email.getRecipientAddress() + ": " + e.getMessage());
              failed = true;
            }
            if (failed) {
              // Increment tries if failed
              try {
                email.setFailures(email.getFailures() + 1);
                EmailManager.save(email);
              } catch (EmailException e) {
                log.error("Failed to update delivery failure count on email that failed to send: " + e.getMessage());
              }
            } else {
              // Remove if sent successfully
              try {
                EmailManager.delete(email);
              } catch (EmailException e) {
                log.error("Failed to delete email that was sent successfully: " + e.getMessage());
              }
            }
          } else {
            log.warn("Not attempting to send email with ID " + email.getUuid() + " due to having failed to send " + email.getFailures() + " times");
          }
        }
      }

      try {
        Thread.sleep(30000);
      } catch (InterruptedException e) {
        // Allow thread to exit if interrupted by stop() method
        if (thread == null) {
          Thread.currentThread().interrupt();
        }
      }
    }
  }
}
