package com.ezhevikina.course.homework7.service;

import com.ezhevikina.course.homework7.dao.Dao;
import com.ezhevikina.course.homework7.dao.exceptions.DaoException;
import com.ezhevikina.course.homework7.dao.exceptions.NotFoundByIdException;
import com.ezhevikina.course.homework7.domain.Account;
import com.ezhevikina.course.homework7.service.exceptions.UnknownAccountException;
import com.ezhevikina.course.homework7.service.exceptions.NotEnoughMoneyException;

public class AccountManager implements AccountService {
  private Dao<Account> dao;

  public AccountManager(Dao<Account> dao) {
    this.dao = dao;
  }

  @Override
  public void withdraw(int accountId, int amount) throws NotEnoughMoneyException,
          UnknownAccountException, DaoException {
    Account account;

    try {
      account = dao.getById(accountId);

    if (account.getAmount() < amount) {
      throw new NotEnoughMoneyException(String.format(
          "Insufficient funds on the account №%d", accountId));
    }

    account.setAmount(account.getAmount() - amount);
    dao.update(account);
    } catch (NotFoundByIdException e) {
      throw new UnknownAccountException(String.format(
          "Account №%d not found", accountId), e);
    }
  }

  @Override
  public int balance(int accountId) throws UnknownAccountException, DaoException {
    try {
      return dao.getById(accountId).getAmount();
    } catch (NotFoundByIdException e) {
      throw new UnknownAccountException(String.format(
          "Account №%d not found", accountId), e);
    }
  }

  @Override
  public void deposit(int accountId, int amount) throws UnknownAccountException, DaoException {
    try {
      Account account = dao.getById(accountId);
      account.setAmount(account.getAmount() + amount);
      dao.update(account);

    } catch (NotFoundByIdException e) {
      throw new UnknownAccountException(String.format(
          "Account №%d not found", accountId), e);
    }
  }

  @Override
  public void transfer(int from, int to, int amount) throws NotEnoughMoneyException,
      UnknownAccountException, DaoException {
    withdraw(from, amount);
    deposit(to, amount);
  }
}
