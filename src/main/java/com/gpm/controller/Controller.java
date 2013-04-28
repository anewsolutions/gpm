/**
 * Copyright 2012-2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

import com.gpm.model.Base;

/**
 * A generic JPA entity controller that handles creation of entity managers and implements
 * basic persistence operations for entities of type T. Entity type specific instances of
 * this class should be gotten using the {@link ControllerFactory} methods.
 * 
 * @author mbooth
 * 
 * @param T
 *          a type that is an annotated JPA entity
 * @see ControllerFactory
 */
public class Controller<T extends Base> {

  /**
   * The class of type T.
   */
  private final Class<T> cls;

  /**
   * The entity manager factory.
   */
  private static EntityManagerFactory emf;

  /**
   * Constructor that receives the class of type T to store for later use. Due to Java's
   * type erasure we can't just do "T.class".
   * 
   * @param cls
   *          the class of type T
   * @throws IllegalArgumentException
   *           if the specified class is null or not a valid annotated JPA entity
   */
  Controller(final Class<T> cls) throws IllegalArgumentException {
    this.cls = cls;
    if (cls == null) {
      throw new IllegalArgumentException("Specified class must not be null");
    }
    if (cls.getAnnotation(Entity.class) == null) {
      throw new IllegalArgumentException("Specified class must be an annotated JPA entity");
    }
  }

  /**
   * Creates and returns an entity manager generated from the entity manager factory. If
   * the factory has not yet been created, the entity manager factory is created first.
   * 
   * @return an entity manager that can be used for database persistence
   */
  private EntityManager getEntityManager() {
    if (emf == null) {
      emf = Persistence.createEntityManagerFactory("gpm");
    }
    return emf.createEntityManager();
  }

  /**
   * Creates or updates an entity of type T in the database.
   * 
   * @param ent
   *          the entity to create or update
   * @throws ControllerException
   *           if there was a problem creating or updating the entity
   */
  public void save(final T ent) throws ControllerException {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      em.merge(ent);
      em.getTransaction().commit();
    } catch (PersistenceException e) {
      em.getTransaction().rollback();
      throw new ControllerException("Error updating " + cls.getSimpleName() + " with uuid " + ent.getUuid() + ": "
          + e.getMessage(), e);
    } finally {
      em.close();
    }
  }

  /**
   * Gets an entity of type T by its primary key.
   * 
   * @param uuid
   *          the uuid of the entity to find
   * @return the found entity or null if the entity does not exist
   * @throws ControllerException
   *           if there was a problem getting the entity
   */
  public T get(final UUID uuid) throws ControllerException {
    EntityManager em = getEntityManager();
    T ent = null;
    try {
      ent = em.find(cls, uuid);
      if (ent != null) {
        initialiseCollections(ent);
      }
    } catch (PersistenceException e) {
      throw new ControllerException("Error getting " + cls.getSimpleName() + " with uuid " + uuid + ": "
          + e.getMessage(), e);
    } finally {
      em.close();
    }
    return ent;
  }

  /**
   * Delete an entity of type T by its primary key.
   * 
   * @param uuid
   *          the uuid of the entity to delete
   * @throws ControllerException
   *           if there was a problem deleting the entity
   */
  public void delete(final UUID uuid) throws ControllerException {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      T ent = em.find(cls, uuid);
      if (ent != null) {
        em.remove(ent);
      }
      em.getTransaction().commit();
    } catch (PersistenceException e) {
      em.getTransaction().rollback();
      throw new ControllerException("Error deleting " + cls.getSimpleName() + " with uuid " + uuid + ": "
          + e.getMessage(), e);
    } finally {
      em.close();
    }
  }

  /**
   * Initialise collections in entities of type T by detecting which accessor methods on
   * the given entity have the element collection annotation.
   * 
   * @param ent
   *          the entity whose collections should be initialised
   */
  private void initialiseCollections(final T ent) throws ControllerException {
    Method[] meths = cls.getMethods();
    for (Method meth : meths) {
      if (meth.getAnnotation(ElementCollection.class) != null) {
        try {
          Hibernate.initialize(meth.invoke(ent));
        } catch (HibernateException e) {
          throw new ControllerException("Error initialising collection in " + cls.getSimpleName() + ": "
              + e.getMessage(), e);
        } catch (IllegalAccessException e) {
          throw new ControllerException("Error initialising collection in " + cls.getSimpleName() + ": "
              + e.getMessage(), e);
        } catch (InvocationTargetException e) {
          throw new ControllerException("Error initialising collection in " + cls.getSimpleName() + ": "
              + e.getMessage(), e);
        }
      }
    }
  }

  /**
   * Gets all entities of type T.
   * 
   * @return a list of entities or an empty list if none exist
   * @throws ControllerException
   *           if there was a problem getting the entities
   */
  public List<T> getAll() throws ControllerException {
    return getAll("created", false, null);
  }

  /**
   * Gets all entities of type T, ordered by the specified field.
   * 
   * @param orderBy
   *          the field name to order the results by
   * @param ascending
   *          true specifies ascending order, false specifies descending order
   * @return an ordered list of entities or an empty list if none exist
   * @throws ControllerException
   *           if there was a problem getting the entities
   */
  public List<T> getAll(final String orderBy, final boolean ascending) throws ControllerException {
    return getAll(orderBy, ascending, null);
  }

  /**
   * Get all entities of type T filtered by the specified entity attribute values.
   * 
   * @param filters
   *          a list of criteria with which to filter the entities, passing a null or
   *          empty list is equivalent to calling {@link #getAll()}
   * @return a list of entities or an empty list if none exist whose attributes match the
   *         specified values
   * @throws ControllerException
   *           if there was a problem getting the entities or any of the given attributes
   *           are invalid for entity type T
   */
  public List<T> getAll(final List<ControllerFilter> filters) throws ControllerException {
    return getAll("created", false, filters);
  }

  /**
   * Get all entities of type T filtered by the specified entity attribute values, ordered
   * by the specified field.
   * 
   * @param orderBy
   *          the field name to order the results by
   * @param ascending
   *          true specifies ascending order, false specifies descending order
   * @param filters
   *          a list of criteria with which to filter the entities, passing a null or
   *          empty list is equivalent to calling {@link #getAll(String, boolean))}
   * @return a list of entities or an empty list if none exist whose attributes match the
   *         specified values
   * @throws ControllerException
   *           if there was a problem getting the entities or any of the given attributes
   *           are invalid for entity type T
   */
  public List<T> getAll(final String orderBy, final boolean ascending, final List<ControllerFilter> filters) throws ControllerException {
    EntityManager em = getEntityManager();
    List<T> ents = new ArrayList<T>();
    try {
      String query = "select a from " + cls.getSimpleName() + " a";
      String where = "";
      if (filters != null) {
        for (ControllerFilter filter : filters) {
          if (where.isEmpty()) {
            where += " where a." + filter.field + " " + filter.operator + " :" + filter.field;
          } else {
            where += " and a." + filter.field + " " + filter.operator + " :" + filter.field;
          }
        }
      }
      String order = "";
      if (orderBy != null && !orderBy.isEmpty()) {
        order += " order by a." + orderBy;
        if (ascending) {
          order += " asc";
        } else {
          order += " desc";
        }
      }
      Query q = em.createQuery(query + where + order);
      if (filters != null) {
        for (ControllerFilter filter : filters) {
          if (filter.value instanceof Date) {
            q.setParameter(filter.field, (Date) filter.value, TemporalType.DATE);
          } else {
            q.setParameter(filter.field, filter.value);
          }
        }
      }
      @SuppressWarnings("unchecked")
      List<T> l = q.getResultList();
      ents = new ArrayList<T>(l);
      for (T ent : ents) {
        initialiseCollections(ent);
      }
    } catch (PersistenceException e) {
      throw new ControllerException("Error getting " + cls.getSimpleName() + "s: " + e.getMessage(), e);
    } finally {
      em.close();
    }
    return ents;
  }
}