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
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.primefaces.model.UploadedFile;

import com.gpm.UploadsServlet;
import com.gpm.i18n.MessageProvider;
import com.gpm.manager.IssueManager;
import com.gpm.manager.exception.IssueException;
import com.gpm.mbean.BeanUtils;
import com.gpm.model.Issue;

@ManagedBean
@ViewScoped
public class IssueAdminBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private static final int BUFFER_SIZE = 1024;
  private UploadedFile uploadedImage;

  private boolean editing;
  private Issue selected;

  /**
   * Bean initialisation.
   */
  @PostConstruct
  public void init() {
    try {
      // Load the selected item, if an ID is passed as a query parameter
      FacesContext fc = FacesContext.getCurrentInstance();
      String uuid = fc.getExternalContext().getRequestParameterMap().get("uuid");
      if (uuid != null && !uuid.isEmpty()) {
        selected = IssueManager.findByUuid(uuid);
        editing = true;
      } else {
        selected = new Issue();
      }
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * JSF method to determine whether we are editing an issue or adding a new issue.
   * 
   * @return true if editing an existing issue, false if adding a new issue
   */
  public boolean isEditing() {
    return editing;
  }

  /**
   * JSF method to provide access to the currently selected issue.
   * 
   * @return the selected issue
   */
  public Issue getSelected() {
    return selected;
  }

  /**
   * JSF method to provide access to all issues.
   * 
   * @return a list of issues
   */
  public List<Issue> getAll() {
    List<Issue> all = null;
    try {
      all = IssueManager.findAllIssues();
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return all;
  }

  /**
   * JSF method to save the selected issue.
   */
  public String save() {
    try {
      IssueManager.save(selected);
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      deleteTempUploads();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/issue_list?faces-redirect=true";
  }

  /**
   * JSF method to delete the selected issue.
   */
  public String delete() {
    try {
      IssueManager.delete(selected);
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/issue_list?faces-redirect=true";
  }

  /**
   * JSF method to cancel editing the selected issue.
   */
  public String cancel() {
    try {
      deleteTempUploads();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/issue_list?faces-redirect=true";
  }

  /**
   * JSF method to format the edition for an issue.
   * 
   * @return a string containing the issue number and published date formatted for the
   *         user's locale
   */
  public String getEdition() {
    FacesContext context = FacesContext.getCurrentInstance();
    Issue issue = context.getApplication().evaluateExpressionGet(context, "#{issue}", Issue.class);
    String published = BeanUtils.formatPublished(issue.getPublishedDate());
    return MessageProvider.getMessage("adminIssueEdition", issue.getIssueNumber(), published);
  }

  /**
   * JSF method to provide access to the last uploaded image file.
   * 
   * @return a PrimeFaces upload
   */
  public UploadedFile getUploadedImage() {
    return uploadedImage;
  }

  /**
   * JSF method to upload a new image file.
   * 
   * @param uploadedImage
   *          a PrimeFaces upload
   */
  public void setUploadedImage(final UploadedFile uploadedImage) {
    BufferedInputStream in = null;
    BufferedOutputStream out = null;
    try {
      // Determine if upload has a valid MIME type
      if (!uploadedImage.getContentType().startsWith("image")) {
        throw new IOException("Not a valid image type");
      }
      // Determine temporary image name
      String ext = FilenameUtils.getExtension(uploadedImage.getFileName());
      selected.setCoverImage("tmp-" + selected.getUuid() + "-" + new Date().getTime() + "." + ext);
      File path = new File(UploadsServlet.getUploadsDirectory(), selected.getCoverImage());
      // Write file contents to disk
      in = new BufferedInputStream(uploadedImage.getInputstream(), BUFFER_SIZE);
      out = new BufferedOutputStream(new FileOutputStream(path), BUFFER_SIZE);
      byte[] buffer = new byte[BUFFER_SIZE];
      int length = -1;
      while ((length = in.read(buffer)) >= 0) {
        out.write(buffer, 0, length);
      }
      this.uploadedImage = uploadedImage;
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

  /**
   * Utility that deletes any temporary uploads that were performed during editing, but
   * were not saved.
   * 
   * @throws IOException
   *           if there was a problem reading the uploads directory or deleting any
   *           temporary uploads
   */
  private void deleteTempUploads() throws IOException {
    if (selected != null) {
      Collection<File> tmpFiles = FileUtils.listFiles(UploadsServlet.getUploadsDirectory(),
          FileFilterUtils.prefixFileFilter("tmp-" + selected.getUuid()), null);
      for (File tmpFile : tmpFiles) {
        FileUtils.forceDelete(tmpFile);
      }
    }
  }
}
