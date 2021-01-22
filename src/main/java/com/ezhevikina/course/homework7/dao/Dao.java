package com.ezhevikina.course.homework7.dao;

import com.ezhevikina.course.homework7.dao.exceptions.DaoException;
import com.ezhevikina.course.homework7.dao.exceptions.NotFoundByIdException;

public interface Dao<T> {
  void create(T entity) throws DaoException;

  T getById(int id) throws NotFoundByIdException, DaoException;

  void update(T entity) throws DaoException, NotFoundByIdException;
}
