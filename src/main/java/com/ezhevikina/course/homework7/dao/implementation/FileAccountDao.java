package com.ezhevikina.course.homework7.dao.implementation;

import com.ezhevikina.course.homework7.dao.Dao;
import com.ezhevikina.course.homework7.dao.exceptions.DaoException;
import com.ezhevikina.course.homework7.dao.exceptions.NotFoundByIdException;
import com.ezhevikina.course.homework7.domain.Account;

import java.io.*;

public class FileAccountDao implements Dao<Account> {
  private final static String DIR = FileAccountInitializer.getDIR();

  @Override
  public void create(Account account) throws DaoException {
    checkIfNull(account);
    File accountFile = new File(
        DIR
            + File.separator
            + account.getId());
    try {
      accountFile.createNewFile();
      write(account, accountFile);
    } catch (IOException e) {
      throw new DaoException("IOException in DAO layer", e);
    }
  }

  @Override
  public Account getById(int accountId) throws NotFoundByIdException, DaoException {
    File accountFile = new File(DIR + File.separator + accountId);

    if (!accountFile.exists()) {
      throw new NotFoundByIdException();
    }

    try (
        FileReader fr = new FileReader(accountFile);
         BufferedReader br = new BufferedReader(fr)
    ) {
      int id = Integer.parseInt(br.readLine());
      String holder = br.readLine();
      int amount = Integer.parseInt(br.readLine());
      return new Account(id, holder, amount);

    } catch (IOException e) {
      throw new DaoException("IOException in DAO layer", e);
    }
  }

  @Override
  public void update(Account account) throws DaoException, NotFoundByIdException {
    checkIfNull(account);
    File accountFile = new File(
        DIR
            + File.separator
            + account.getId());
    if (!accountFile.exists()) {
      throw new NotFoundByIdException();
    }
    write(account, accountFile);
  }

  private void write (Account account, File accountFile) throws DaoException {
    try (
        FileWriter fw = new FileWriter(accountFile.getAbsoluteFile());
         BufferedWriter bw = new BufferedWriter(fw)
    ) {
      bw.write(String.valueOf(account.getId()));
      bw.newLine();
      bw.write(account.getHolder());
      bw.newLine();
      bw.write(String.valueOf(account.getAmount()));

    } catch (IOException e) {
      throw new DaoException("IOException in DAO layer", e);
    }
  }

  private void checkIfNull(Account account) throws DaoException {
    if (account == null) {
      throw new DaoException("Account can't be null");
    }
  }
}
