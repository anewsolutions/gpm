/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.hibernate.annotations.Type;

/**
 * This is the base class for all JPA entities. It contains the database ID, business ID,
 * and entity creation time. All entities in the model should extend this class.
 * 
 * @author mbooth
 */
@MappedSuperclass
public abstract class Base implements Serializable {
  private static final long serialVersionUID = 1L;

  private int id;
  private UUID uuid;
  private Date created;

  public Base() {
  }

  /**
   * Database primary key of the entity. This is a database table row identifier that is
   * auto-incremented by the persistence provider when new entities are created, there is
   * normally no need to set it manually.
   * 
   * @return the primary key as an integer
   */
  @Id
  @GeneratedValue
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Business key of the entity. A universally unique and immutable identifier used for
   * comparing entities that may or may not point to the same row in the database. This is
   * set before new entities are persisted for the first time.
   * 
   * @return the business key as a universally unique identifier
   */
  @Column(length = 36, nullable = false)
  @Type(type = "uuid-char")
  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  /**
   * Time stamp of when the entity was created. This is set before new entities are
   * persisted for the first time.
   * 
   * @return a date/time that identifies when the entity was created
   */
  @Column(nullable = false)
  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  /**
   * Called by the persistence provider before the entity is saved to make sure the entity
   * has all of it's auto-generated and read-only fields populated. This should not be
   * called by the application.
   */
  @PrePersist
  public void ensureAutoFields() {
    if (getUuid() == null) {
      setUuid(UUID.randomUUID());
    }
    if (getCreated() == null) {
      setCreated(new Date());
    }
  }

  @Override
  public int hashCode() {
    // Delegate to the UUID
    return getUuid().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!this.getClass().isInstance(obj)) {
      return false;
    }
    // Delegate to the UUID
    return getUuid().equals(((Base) obj).getUuid());
  }
}