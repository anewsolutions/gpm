/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gpm.manager.ConfigurationManager;
import com.gpm.manager.CustomerOrderManager;
import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.ConfigurationException;
import com.gpm.manager.exception.CustomerOrderException;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.CustomerOrder;
import com.gpm.model.CustomerOrderItem;
import com.gpm.model.UserAccount;
import com.gpm.model.UserIssue;
import com.gpm.model.enums.OrderStatus;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;

@WebServlet(name = "PayPal Callback Servlet", value = { PayPalCallbackServlet.PAYPAL_PATH + "*" })
public class PayPalCallbackServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  public static final String PAYPAL_PATH = "/paypal/";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Delegate to POST handler
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Get parameters
    String orderUuid = request.getParameter("trans_id");
    String payerId = request.getParameter("PayerID");

    // Get oauth token for PayPal API access
    String token = "";
    try {
      Properties props = ConfigurationManager.findPropsByKey("paypal");
      String key = props.getProperty("paypal.key");
      String secret = props.getProperty("paypal.secret");
      token = new OAuthTokenCredential(key, secret).getAccessToken();
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (PayPalRESTException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    boolean valid = false;

    // Update customer order
    CustomerOrder order = null;
    try {
      order = CustomerOrderManager.findByUuid(orderUuid);
      if (order == null) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown transaction ID");
        return;
      }

      // Execute payment
      Payment payment = Payment.get(token, order.getPaymentId());
      PaymentExecution paymentExecution = new PaymentExecution();
      paymentExecution.setPayerId(payerId);
      payment = payment.execute(token, paymentExecution);

      valid = "approved".equals(payment.getState()) || "pending".equals(payment.getState());

      // Save the order
      if (valid) {
        order.setOrderStatus(OrderStatus.AUTHORISED);
      } else {
        order.setOrderStatus(OrderStatus.DECLINED);
      }
      CustomerOrderManager.storeCustomerOrder(order);
    } catch (CustomerOrderException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to find or store customer order");
      return;
    } catch (PayPalRESTException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to execute payment");
      return;
    }

    // Create user issues
    try {
      if (valid) {
        UserAccount account = UserAccountManager.findByUuid(order.getUser().getUuid().toString());
        for (CustomerOrderItem item : order.getItemsAsList()) {
          for (int issueNumber = item.getStartIssue(); issueNumber < item.getStartIssue() + item.getNumIssues(); issueNumber++) {
            UserIssue issue = new UserIssue();
            issue.setIssueNumber(issueNumber);
            issue.setFormat(item.getFormat());
            account.addMagazine(issue);
          }
        }
        UserAccountManager.save(account);
      }
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to find or update user account");
      return;
    }

    // Send basic response page
    StringBuilder page = new StringBuilder();
    String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    if (valid) {
      page.append("<html>");
      page.append("<head>");
      page.append("<title>");
      page.append("Payment Authorised");
      page.append("</title>");
      page.append("</head>");
      page.append("<body>");
      page.append("<h2>");
      page.append("Your payment has been authorised!");
      page.append("</h2>");
      page.append("<p>Thanks for your custom. Please <a href='" + returnUrl
          + "'>click here</a> to return to the <a href='" + returnUrl + "'>Guinea Pig Magazine</a> website.</p>");
      page.append("</body>");
      page.append("</html>");
    } else {
      page.append("<html>");
      page.append("<head>");
      page.append("<title>");
      page.append("Payment NOT Authorised");
      page.append("</title>");
      page.append("</head>");
      page.append("<body>");
      page.append("<h2>");
      page.append("We're sorry, your payment has not been authorised.");
      page.append("</h2>");
      page.append("<p>Please <a href='" + returnUrl + "'>click here</a> to return to the <a href='" + returnUrl
          + "'>Guinea Pig Magazine</a> website where you may try again.</p>");
      page.append("</body>");
      page.append("</html>");
    }
    response.reset();
    response.setContentType("text/html");
    response.setContentLength(page.length());
    PrintWriter out = response.getWriter();
    out.write(page.toString());
    out.flush();
    out.close();
  }
}
