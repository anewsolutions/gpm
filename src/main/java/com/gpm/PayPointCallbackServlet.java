/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    System.out.println("HEADERS");
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      System.out.println(headerName + ":");
      Enumeration<String> headers = request.getHeaders(headerName);
      while (headers.hasMoreElements()) {
        String header = headers.nextElement();
        System.out.println("  " + header);
      }
    }
    System.out.println("PARAMETERS");
    Enumeration<String> paramNames = request.getParameterNames();
    while (paramNames.hasMoreElements()) {
      String paramName = paramNames.nextElement();
      System.out.println(paramName + ":");
      String[] params = request.getParameterValues(paramName);
      for (String param : params) {
        System.out.println("  " + param);
      }
    }
  }
}
