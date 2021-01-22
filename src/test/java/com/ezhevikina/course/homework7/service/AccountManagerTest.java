package com.ezhevikina.course.homework7.service;

import com.ezhevikina.course.homework7.dao.exceptions.DaoException;
import com.ezhevikina.course.homework7.dao.exceptions.NotFoundByIdException;
import com.ezhevikina.course.homework7.dao.implementation.FileAccountDao;
import com.ezhevikina.course.homework7.domain.Account;
import com.ezhevikina.course.homework7.service.exceptions.NotEnoughMoneyException;
import com.ezhevikina.course.homework7.service.exceptions.UnknownAccountException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountManagerTest {
  private AccountManager manager;
  @Mock
  private FileAccountDao dao;
  private Account account;

  @Before
  public void initAccountManager() throws DaoException, NotFoundByIdException {
    manager = new AccountManager(dao);
    account = new Account(1, "Holder", 100);
    when(dao.getById(1)).thenReturn(account);
  }

  @Test
  public void testAccountBalance() throws DaoException, UnknownAccountException {
    assertEquals(manager.balance(1), 100);
  }

  @Test(expected = UnknownAccountException.class)
  public void testAccountBalanceThrowsUnknownAccountException() throws DaoException,
      UnknownAccountException, NotFoundByIdException {
    doThrow(new NotFoundByIdException()).when(dao).getById(1);
    manager.balance(1);
  }

  @Test(expected = DaoException.class)
  public void testAccountBalanceThrowsDaoException()
      throws DaoException, NotFoundByIdException, UnknownAccountException {
    doThrow(new DaoException("Exception in DAO layer")).when(dao).getById(1);
    manager.balance(1);
  }

  @Test
  public void testAccountDeposit() throws DaoException, NotFoundByIdException, UnknownAccountException {
    manager.deposit(1, 100);
    verify(dao, times(1)).update(account);
  }

  @Test(expected = UnknownAccountException.class)
  public void testAccountDepositThrowsUnknownAccountException()
      throws DaoException, UnknownAccountException, NotFoundByIdException {
    doThrow(new NotFoundByIdException()).when(dao).getById(1);
    manager.deposit(1, 100);
  }

  @Test(expected = DaoException.class)
  public void testAccountDepositThrowsDaoException_WhenDaoThrowsDaoException()
      throws NotFoundByIdException, DaoException, UnknownAccountException {
    doThrow(new DaoException("Exception in DAO layer")).when(dao).getById(1);
    manager.deposit(1, 100);
  }

  @Test
  public void testAccountWithdrawAll() throws UnknownAccountException,
          DaoException, NotEnoughMoneyException, NotFoundByIdException {
    manager.withdraw(1, 100);
    verify(dao).update(account);
  }

  @Test
  public void testAccountWithdrawLessThanBalance() throws UnknownAccountException,
          DaoException, NotEnoughMoneyException, NotFoundByIdException {
    manager.withdraw(1, 10);
    verify(dao).update(account);
  }

  @Test(expected = NotEnoughMoneyException.class)
  public void testAccountWithdrawMoreThanBalance_NotEnoughMoneyException() throws UnknownAccountException,
          DaoException, NotEnoughMoneyException {
    manager.withdraw(1, 1000);
  }

  @Test(expected = UnknownAccountException.class)
  public void testAccountWithdrawThrowsUnknownAccountException() throws DaoException,
          NotFoundByIdException, UnknownAccountException, NotEnoughMoneyException {
    doThrow(new NotFoundByIdException()).when(dao).getById(1);
    manager.withdraw(1, 100);
  }

  @Test
  public void testTransfer() throws DaoException,
          NotFoundByIdException, UnknownAccountException, NotEnoughMoneyException {
    when(dao.getById(2)).thenReturn(account);
    manager.transfer(1, 2, 100);
    verify(dao, times(2)).getById(anyInt());
    verify(dao, times(2)).update(account);
  }
}