package com.ezhevikina.course.homework7.domain;

import com.ezhevikina.course.homework7.dao.exceptions.DaoException;
import com.ezhevikina.course.homework7.service.AccountManager;
import com.ezhevikina.course.homework7.service.exceptions.NotEnoughMoneyException;
import com.ezhevikina.course.homework7.service.exceptions.UnknownAccountException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AtmScreenTest {
  private final String atmStartScreen = "Available operations:\n"
      + "* balance [id]\n"
      + "* withdraw [id] [amount]\n"
      + "* deposit [id] [amount]\n"
      + "* transfer [from] [to] [amount]\n"
      + "E to exit";

  @Mock
  private BufferedReader br;
  @Mock
  private AccountManager manager;
  private AtmScreen atm;

  @Before
  public void initAtm() {
    atm = new AtmScreen(manager);
  }

  @Test
  public void testBalance() throws IOException, DaoException, UnknownAccountException {
    when(br.readLine()).thenReturn("balance 1");
    when(manager.balance(1)).thenReturn(100);
    assertEquals("100", atm.commandExecutor(br));
  }

  @Test
  public void testBalanceUnknownAccount() throws IOException, DaoException, UnknownAccountException {
    when(br.readLine()).thenReturn("balance 1");
    when(manager.balance(1)).thenThrow(new UnknownAccountException("Unknown account", new Exception()));
    assertEquals("Unknown account", atm.commandExecutor(br));
  }

  @Test
  public void testBalanceCallGotDaoException() throws IOException, DaoException, UnknownAccountException {
    when(br.readLine()).thenReturn("balance 1");
    when(manager.balance(1)).thenThrow(new DaoException("DAO exception"));
    assertTrue(atm.commandExecutor(br).contains("Not sure what to do with DAO exceptions"));
  }

  @Test
  public void testCommandExecutorGotIOException() throws IOException {
    when(br.readLine()).thenThrow(new IOException());
    assertTrue(atm.commandExecutor(br).contains("IOException"));
  }

  @Test
  public void testBalanceWithInvalidId() throws IOException {
    when(br.readLine()).thenReturn("balance kkk");
    assertEquals("Invalid command", atm.commandExecutor(br));
  }

  @Test
  public void testBalanceWithEmptyId() throws IOException {
    when(br.readLine()).thenReturn("balance");
    assertEquals("Invalid command", atm.commandExecutor(br));
  }

  @Test
  public void testBalanceWithThirdInvalidParameterIgnored() throws IOException, DaoException, UnknownAccountException {
    when(br.readLine()).thenReturn("balance 1 kkk");
    when(manager.balance(1)).thenReturn(100);
    assertEquals("100", atm.commandExecutor(br));
  }

  @Test
  public void testWithdraw() throws IOException {
    when(br.readLine()).thenReturn("withdraw 1 100");
    assertEquals("Successful operation", atm.commandExecutor(br));
  }

  @Test
  public void testWithdrawFromUnknownAccount() throws IOException, UnknownAccountException, DaoException, NotEnoughMoneyException {
    when(br.readLine()).thenReturn("withdraw 1 100");
    doThrow(new UnknownAccountException("Unknown account", new Exception())).when(manager).withdraw(1, 100);
    assertEquals("Unknown account", atm.commandExecutor(br));
  }

  @Test
  public void testWithdrawThrowsNotEnoughMoneyException() throws IOException, UnknownAccountException, DaoException, NotEnoughMoneyException {
    when(br.readLine()).thenReturn("withdraw 1 100");
    doThrow(new NotEnoughMoneyException("Insufficient funds")).when(manager).withdraw(1, 100);
    assertEquals("Insufficient funds", atm.commandExecutor(br));
  }

  @Test
  public void testWithdrawInvalidSum() throws IOException {
    when(br.readLine()).thenReturn("withdraw 1 kkk");
    assertEquals("Invalid command", atm.commandExecutor(br));
  }

  @Test
  public void testWithdrawNegative() throws IOException {
    when(br.readLine()).thenReturn("withdraw 1 -100");
    assertEquals("Amount should be positive", atm.commandExecutor(br));
  }

  @Test
  public void testWithdrawNull() throws IOException {
    when(br.readLine()).thenReturn("withdraw 1");
    assertEquals("Invalid command", atm.commandExecutor(br));
  }

  @Test
  public void testWithdrawInvalidAccountId() throws IOException {
    when(br.readLine()).thenReturn("withdraw kkk");
    assertEquals("Invalid command", atm.commandExecutor(br));
  }

  @Test
  public void testWithdrawNullAccount() throws IOException {
    when(br.readLine()).thenReturn("withdraw");
    assertEquals("Invalid command", atm.commandExecutor(br));
  }

  @Test
  public void testWithdrawThrowsDaoException() throws IOException, UnknownAccountException, DaoException, NotEnoughMoneyException {
    when(br.readLine()).thenReturn("withdraw 1 100");
    doThrow(new DaoException("DAO exception")).when(manager).withdraw(1, 100);
    assertTrue(atm.commandExecutor(br).contains("Not sure what to do with DAO exceptions"));
  }

  @Test
  public void testDeposit() throws IOException {
    when(br.readLine()).thenReturn("deposit 1 100");
    assertEquals("Successful operation", atm.commandExecutor(br));
  }

  @Test
  public void testDepositToUnknownAccount() throws IOException, UnknownAccountException, DaoException, NotEnoughMoneyException {
    when(br.readLine()).thenReturn("deposit 1 100");
    doThrow(new UnknownAccountException("Unknown account", new Exception())).when(manager).deposit(1, 100);
    assertEquals("Unknown account", atm.commandExecutor(br));
  }

  @Test
  public void testDepositInvalidSum() throws IOException {
    when(br.readLine()).thenReturn("deposit 1 kkk");
    assertEquals("Invalid command", atm.commandExecutor(br));
  }

  @Test
  public void testDepositNegative() throws IOException {
    when(br.readLine()).thenReturn("deposit 1 -100");
    assertEquals("Amount should be positive", atm.commandExecutor(br));
  }

  @Test
  public void testDepositNull() throws IOException {
    when(br.readLine()).thenReturn("deposit 1");
    assertEquals("Invalid command", atm.commandExecutor(br));
  }

  @Test
  public void testDepositInvalidAccountId() throws IOException {
    when(br.readLine()).thenReturn("deposit kkk");
    assertEquals("Invalid command", atm.commandExecutor(br));
  }

  @Test
  public void testDepositNullAccount() throws IOException {
    when(br.readLine()).thenReturn("deposit");
    assertEquals("Invalid command", atm.commandExecutor(br));
  }

  @Test
  public void testDepositThrowsDaoException() throws IOException, UnknownAccountException, DaoException, NotEnoughMoneyException {
    when(br.readLine()).thenReturn("deposit 1 100");
    doThrow(new DaoException("DAO exception")).when(manager).deposit(1, 100);
    assertTrue(atm.commandExecutor(br).contains("Not sure what to do with DAO exceptions"));
  }

  @Test
  public void testTransfer() throws IOException {
    when(br.readLine()).thenReturn("transfer 1 2 100");
    assertEquals("Successful operation", atm.commandExecutor(br));
  }

  @Test
  public void testTransferFromToUnknownAccount() throws IOException, UnknownAccountException, DaoException, NotEnoughMoneyException {
    when(br.readLine()).thenReturn("transfer 1 2 100");
    doThrow(new UnknownAccountException("Unknown account", new Exception())).when(manager).transfer(1, 2, 100);
    assertEquals("Unknown account", atm.commandExecutor(br));
  }

  @Test
  public void testTransferThrowsNotEnoughMoneyException() throws IOException, UnknownAccountException, DaoException, NotEnoughMoneyException {
    when(br.readLine()).thenReturn("transfer 1 2 100");
    doThrow(new NotEnoughMoneyException("Insufficient funds")).when(manager).transfer(1, 2, 100);
    assertEquals("Insufficient funds", atm.commandExecutor(br));
  }

  @Test
  public void testTransferInvalidSum() throws IOException {
    when(br.readLine()).thenReturn("transfer 1 2 kkk");
    assertEquals("Invalid command", atm.commandExecutor(br));
  }

  @Test
  public void testTransferNegative() throws IOException {
    when(br.readLine()).thenReturn("transfer 1 2 -100");
    assertEquals("Amount should be positive", atm.commandExecutor(br));
  }

  @Test
  public void testTransferNull() throws IOException {
    when(br.readLine()).thenReturn("transfer 1 2");
    assertEquals("Invalid command", atm.commandExecutor(br));
  }

  @Test
  public void testTransferFromToInvalidAccountId() throws IOException {
    when(br.readLine()).thenReturn("transfer kkk 2");
    assertEquals("Invalid command", atm.commandExecutor(br));
    when(br.readLine()).thenReturn("transfer 1 kkk");
    assertEquals("Invalid command", atm.commandExecutor(br));
  }

  @Test
  public void testTransferFromToNullAccounts() throws IOException {
    when(br.readLine()).thenReturn("transfer");
    assertEquals("Invalid command", atm.commandExecutor(br));
  }

  @Test
  public void testTransferThrowsDaoException() throws IOException, UnknownAccountException, DaoException, NotEnoughMoneyException {
    when(br.readLine()).thenReturn("transfer 1 2 100");
    doThrow(new DaoException("DAO exception")).when(manager).transfer(1, 2, 100);
    assertTrue(atm.commandExecutor(br).contains("Not sure what to do with DAO exceptions"));
  }

  @Test
  public void testInvalidCommand() throws IOException {
    when(br.readLine()).thenReturn("Invalid input");
    assertEquals("Invalid command", atm.commandExecutor(br));
  }

  @Test
  public void testCommandCaseIgnored() throws IOException, DaoException, UnknownAccountException {
    when(br.readLine()).thenReturn("BaLaNCe 1");
    when(manager.balance(1)).thenReturn(100);
    assertEquals("100", atm.commandExecutor(br));
  }

  @Test
  public void testExit() throws IOException {
    when(br.readLine()).thenReturn("e");
    assertEquals("Exit", atm.commandExecutor(br));
    verify(br).close();
  }

  @Rule
  public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

  @Test
  public void startAtmScreen() {
    AtmScreen spyAtm = Mockito.spy(new AtmScreen(manager));
    doReturn("Exit").when(spyAtm).commandExecutor(any(BufferedReader.class));
    spyAtm.start();
    assertTrue(systemOutRule.getLog().contains(atmStartScreen));
  }
}
