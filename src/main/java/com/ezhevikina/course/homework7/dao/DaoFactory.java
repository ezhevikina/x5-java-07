package com.ezhevikina.course.homework7.dao;

import com.ezhevikina.course.homework7.dao.exceptions.DaoException;
import com.ezhevikina.course.homework7.domain.StorageType;

public interface DaoFactory<T> {
  Dao<T> getDao(StorageType type) throws DaoException;
}
