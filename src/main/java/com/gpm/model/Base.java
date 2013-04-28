/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

/**
 * This is the base class for all JPA entities. It contains the entity UUID, and creation
 * time. It is ordered naturally by the entity creation time. All entities in the model
 * should extend this class.
 * 
 * @author mbooth
 */
@MappedSuperclass
public abstract class Base implements Comparable<Base>, Serializable {
  private static final long serialVersionUID = 1L;

  private UUID uuid;
  private Date created;

  /**
   * Default constructor to populate generated/read-only fields. All extending classes
   * should call super().
   */
  public Base() {
    setUuid(UUID.randomUUID());
    setCreated(new Date());
  }

  /**
   * Primary key of the entity. A universally unique and immutable identifier used for
   * comparing entities that may or may not point to the same row in the database. This is
   * set when new entities are created, there is no need to set it manually.
   * 
   * @return the primary key as a universally unique identifier
   */
  @Id
  @Column(length = 36, nullable = false)
  @Type(type = "uuid-char")
  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(final UUID uuid) {
    this.uuid = uuid;
  }

  /**
   * Time stamp of when the entity was created. This is set when new entities are created,
   * there is no need to set it manually.
   * 
   * @return a date/time that identifies when the entity was created
   */
  @Column(nullable = false)
  public Date getCreated() {
    return created;
  }

  public void setCreated(final Date created) {
    this.created = created;
  }

  @Override
  public int compareTo(final Base o) {
    return getCreated().compareTo(o.getCreated());
  }

  @Override
  public int hashCode() {
    // Delegate to the UUID
    return getUuid().hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
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