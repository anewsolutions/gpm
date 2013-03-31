/**
 * Copyright 2012-2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

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
  private Class<T> cls;

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
  Controller(Class<T> cls) throws IllegalArgumentException {
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
  public void save(T ent) throws ControllerException {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      em.merge(ent);
      em.getTransaction().commit();
    } catch (PersistenceException e) {
      em.getTransaction().rollback();
      throw new ControllerException("Error updating " + cls.getSimpleName() + ": " + e.getMessage(), e);
    } finally {
      em.close();
    }
  }

  /**
   * Delete an entity of type T by its primary key.
   * 
   * @param id
   *          the id of the entity to delete
   * @throws ControllerException
   *           if there was a problem deleting the entity
   */
  public void delete(int id) throws ControllerException {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      T ent = em.find(cls, id);
      if (ent != null) {
        em.remove(ent);
      }
      em.getTransaction().commit();
    } catch (PersistenceException e) {
      em.getTransaction().rollback();
      throw new ControllerException("Error deleting " + cls.getSimpleName() + " with id " + id + ": " + e.getMessage(),
          e);
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
  protected void initialiseCollections(T ent) throws ControllerException {
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
   * Gets an entity of type T by its primary key.
   * 
   * @param id
   *          the id of the entity to find
   * @return the found entity or null if the entity does not exist
   * @throws ControllerException
   *           if there was a problem getting the entity
   */
  public T get(int id) throws ControllerException {
    EntityManager em = getEntityManager();
    T ent = null;
    try {
      ent = em.find(cls, id);
      initialiseCollections(ent);
    } catch (PersistenceException e) {
      throw new ControllerException("Error getting " + cls.getSimpleName() + " with id " + id + ": " + e.getMessage(),
          e);
    } finally {
      em.close();
    }
    return ent;
  }

  /**
   * Gets all entities of type T.
   * 
   * @return a list of entities or an empty list if none exist
   * @throws ControllerException
   *           if there was a problem getting the entities
   */
  public List<T> getAll() throws ControllerException {
    return getAll("id", true, null);
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
  public List<T> getAll(String orderBy, boolean ascending) throws ControllerException {
    return getAll(orderBy, ascending, null);
  }

  /**
   * Get all entities of type T filtered by the specified entity attribute values.
   * 
   * @param attributes
   *          a map of attribute/value pairs with which to filter the entities, passing an
   *          empty map is equivalent to calling {@link #getAll()}
   * @return a list of entities or an empty list if none exist whose attributes match the
   *         specified values
   * @throws ControllerException
   *           if there was a problem getting the entities or any of the given attributes
   *           are invalid for entity type T
   */
  public List<T> getAll(Map<String, Object> attributes) throws ControllerException {
    return getAll("id", true, attributes);
  }

  /**
   * Get all entities of type T filtered by the specified entity attribute values, ordered
   * by the specified field.
   * 
   * @param orderBy
   *          the field name to order the results by
   * @param ascending
   *          true specifies ascending order, false specifies descending order
   * @param attributes
   *          a map of attribute/value pairs with which to filter the entities, passing an
   *          empty map is equivalent to calling {@link #getAll()}
   * @return a list of entities or an empty list if none exist whose attributes match the
   *         specified values
   * @throws ControllerException
   *           if there was a problem getting the entities or any of the given attributes
   *           are invalid for entity type T
   */
  public List<T> getAll(String orderBy, boolean ascending, Map<String, Object> attributes) throws ControllerException {
    if (orderBy == null || orderBy.isEmpty()) {
      orderBy = "id";
    }
    EntityManager em = getEntityManager();
    List<T> ents = new ArrayList<T>();
    try {
      String query = "select a from " + cls.getSimpleName() + " a";
      String where = "";
      if (attributes != null) {
        for (String att : attributes.keySet()) {
          if (where.isEmpty()) {
            where += " where a." + att + " = :" + att;
          } else {
            where += " and a." + att + " = :" + att;
          }
        }
      }
      String order = " order by a." + orderBy;
      if (ascending) {
        order += " asc";
      } else {
        order += " desc";
      }
      Query q = em.createQuery(query + where + order);
      if (attributes != null) {
        for (String att : attributes.keySet()) {
          q.setParameter(att, attributes.get(att));
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