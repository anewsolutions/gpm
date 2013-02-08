/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.admin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.gpm.UploadsServlet;
import com.gpm.manager.ImageManager;
import com.gpm.manager.exception.ImageException;
import com.gpm.manager.exception.ManagerException;
import com.gpm.model.Image;

/**
 * Managed bean for adding, editing or deleting an image.
 * 
 * @author mbooth
 */
@ManagedBean
@ViewScoped
public class ImageAdminBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private static final int BUFFER_SIZE = 1024;

  private boolean editing;
  private Image selected;

  /**
   * Bean initialisation.
   */
  @PostConstruct
  public void init() {
    try {
      // Load the selected image, if an ID is passed as a query parameter
      FacesContext fc = FacesContext.getCurrentInstance();
      String id = fc.getExternalContext().getRequestParameterMap().get("id");
      try {
        int idValue = Integer.parseInt(id);
        selected = ImageManager.find(idValue);
        editing = true;
      } catch (NumberFormatException e) {
        selected = new Image();
      }
    } catch (ImageException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * JSF method to determine whether we are editing an image or adding a new image.
   * 
   * @return true if editing an existing image, false is adding a new image
   */
  public boolean isEditing() {
    return editing;
  }

  /**
   * JSF method to provide access to the currently selected image.
   * 
   * @return the selected image
   */
  public Image getSelected() {
    return selected;
  }

  /**
   * JSF method to fetch all the images.
   * 
   * @return a list of images
   */
  public List<Image> getAll() {
    List<Image> all = null;
    try {
      all = ImageManager.findAll();
    } catch (ImageException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return all;
  }

  public void handleFileUpload(FileUploadEvent event) {
    BufferedInputStream in = null;
    BufferedOutputStream out = null;
    try {
      UploadedFile file = event.getFile();
      selected.setExtension(FilenameUtils.getExtension(file.getFileName()));
      if (selected.getName() == null || selected.getName().isEmpty()) {
        selected.setName(file.getFileName());
      }

      // Write file contents to disk
      File path = new File(UploadsServlet.getUploadsDirectory(), selected.getFilename());
      in = new BufferedInputStream(file.getInputstream(), BUFFER_SIZE);
      out = new BufferedOutputStream(new FileOutputStream(path), BUFFER_SIZE);
      byte[] buffer = new byte[BUFFER_SIZE];
      int length = -1;
      while ((length = in.read(buffer)) >= 0) {
        out.write(buffer, 0, length);
      }

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      try {
        // Close streams
        if (in != null) {
          in.close();
        }
        if (out != null) {
          out.flush();
          out.close();
        }
      } catch (IOException e) {
        // Ignore, only closing streams
      }
    }
  }

  public boolean isFileUploaded() {
    return selected.getExtension() != null && !selected.getExtension().isEmpty();
  }

  /**
   * JSF method to save the currently selected image.
   * 
   * @return redirects back to the images index
   */
  public String save() {
    try {
      ImageManager.save(selected);
    } catch (ManagerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/admin/images/index?faces-redirect=true";
  }

  /**
   * JSF method to delete the currently selected image.
   * 
   * @return redirects back to the images index
   */
  public String delete() {
    try {
      ImageManager.delete(selected);
    } catch (ImageException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/admin/images/index?faces-redirect=true";
  }
}
