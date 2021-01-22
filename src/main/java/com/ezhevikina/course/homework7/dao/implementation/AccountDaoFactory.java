package com.ezhevikina.course.homework7.dao.implementation;

import com.ezhevikina.course.homework7.domain.StorageType;
import com.ezhevikina.course.homework7.dao.Dao;
import com.ezhevikina.course.homework7.dao.DaoFactory;
import com.ezhevikina.course.homework7.domain.Account;

public class AccountDaoFactory implements DaoFactory<Account> {
  @Override
  public Dao<Account> getDao(StorageType type) {
    return new FileAccountDao();

  }
}
