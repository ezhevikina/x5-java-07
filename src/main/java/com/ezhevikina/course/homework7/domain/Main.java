package com.ezhevikina.course.homework7.domain;

import com.ezhevikina.course.homework7.dao.implementation.AccountDaoFactory;
import com.ezhevikina.course.homework7.dao.Dao;
import com.ezhevikina.course.homework7.dao.exceptions.DaoException;
import com.ezhevikina.course.homework7.dao.implementation.FileAccountInitializer;
import com.ezhevikina.course.homework7.service.AccountManager;

public class Main {
  public static void main(String[] args) {
    StorageType accountsStorageType = StorageType.FILE_SYSTEM;

    try {
      Dao<Account> dao = new AccountDaoFactory().getDao(accountsStorageType);
      new FileAccountInitializer().initialize(dao);

      AtmScreen atm = new AtmScreen(new AccountManager(dao));
      atm.start();

    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
}
