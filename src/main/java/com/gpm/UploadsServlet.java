/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Noddy servlet to dish up file that were previously uploaded by the user.
 * 
 * @author mbooth
 */
@WebServlet(name = "Uploads Servlet", value = { "/uploads/*" })
public class UploadsServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private static final int BUFFER_SIZE = 10240;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Check requested path
    if (request.getPathInfo() == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    // Use current working directory if we are not running on OpenShift
    String dataDir = System.getenv("OPENSHIFT_DATA_DIR");
    if (dataDir == null || dataDir.length() == 0) {
      dataDir = ".";
    }

    // Get path to requested file on disk
    File uploadsDir = new File(dataDir, request.getServletPath());
    File path = new File(uploadsDir.getCanonicalFile(), request.getPathInfo());
    if (!path.isFile() || !path.canRead()) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    // Detect MIME type
    String mime = getServletContext().getMimeType(path.getName());
    if (mime == null) {
      mime = "application/octet-stream";
    }

    // Set up response
    response.reset();
    response.setBufferSize(BUFFER_SIZE);
    response.setContentType(mime);
    response.setContentLength((int) path.length());

    // Write file contents to response
    BufferedInputStream in = null;
    BufferedOutputStream out = null;
    try {
      in = new BufferedInputStream(new FileInputStream(path), BUFFER_SIZE);
      out = new BufferedOutputStream(response.getOutputStream(), BUFFER_SIZE);
      byte[] buffer = new byte[BUFFER_SIZE];
      int length = -1;
      while ((length = in.read(buffer)) >= 0) {
        out.write(buffer, 0, length);
      }
    } finally {
      // Close streams
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.flush();
        out.close();
      }
    }
  }
}
