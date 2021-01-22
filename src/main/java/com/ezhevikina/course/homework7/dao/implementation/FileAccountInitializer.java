package com.ezhevikina.course.homework7.dao.implementation;

import com.ezhevikina.course.homework7.dao.exceptions.DaoException;
import com.ezhevikina.course.homework7.domain.Account;
import com.ezhevikina.course.homework7.dao.Dao;

import java.io.File;

public class FileAccountInitializer {
  private final static String DIR = "./src/main/resources/accounts";

  public void initialize(Dao<Account> dao) throws DaoException {
    File accounts = new File(DIR);

    if (!accounts.exists()) {
      accounts.mkdir();
      System.out.println("accounts dir created");

      for (int i = 0; i < 10; i++) {
        dao.create(new Account(
            1000 + i, "HolderName" + i, 100 * i));
      }
    }
  }

  public static String getDIR() {
    return DIR;
  }
}
