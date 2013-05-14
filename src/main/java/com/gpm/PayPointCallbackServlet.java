/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gpm.manager.CustomerOrderManager;
import com.gpm.manager.exception.CustomerOrderException;
import com.gpm.model.CustomerOrder;
import com.gpm.model.enums.OrderStatus;

@WebServlet(name = "PayPoint Callback Servlet", value = { PayPointCallbackServlet.PAYPOINT_PATH + "*" })
public class PayPointCallbackServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  public static final String PAYPOINT_PATH = "/paypoint/";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Delegate to POST handler
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Valid will always be passed
    boolean valid = new Boolean(request.getParameter("valid"));

    // Get other parameters
    String transId = request.getParameter("trans_id");
    String code = request.getParameter("code");
    String authCode = request.getParameter("auth_code");
    String message = request.getParameter("message");

    // Update customer order
    try {
      CustomerOrder order = CustomerOrderManager.findCustomerOrder(transId);
      if (order == null) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown transaction ID");
        return;
      }
      order.setAuthCode(authCode);
      order.setErrorCode(code);
      order.setErrorMessage(message);
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
    }

    // Send basic response page back to PayPoint
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
