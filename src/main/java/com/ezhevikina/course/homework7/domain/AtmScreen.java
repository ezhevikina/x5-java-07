package com.ezhevikina.course.homework7.domain;

import com.ezhevikina.course.homework7.dao.exceptions.DaoException;
import com.ezhevikina.course.homework7.service.AccountManager;
import com.ezhevikina.course.homework7.service.exceptions.NotEnoughMoneyException;
import com.ezhevikina.course.homework7.service.exceptions.UnknownAccountException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class AtmScreen {
  private final AccountManager manager;

  public AtmScreen(AccountManager manager) {
    this.manager = manager;
  }

  public void start() {
    System.out.println("Available operations:\n"
        + "* balance [id]\n"
        + "* withdraw [id] [amount]\n"
        + "* deposit [id] [amount]\n"
        + "* transfer [from] [to] [amount]\n"
        + "E to exit");

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    String message = "";
    while (!message.equalsIgnoreCase("Exit")) {
      message = commandExecutor(reader);
      System.out.println(message);
    }
  }

  public String commandExecutor(BufferedReader reader) {
    try {
      String[] command = (reader.readLine()).split(" ");

      switch (command[0].toLowerCase()) {
        case "balance": {
          int accountId = Integer.parseInt(command[1]);
          return String.valueOf(manager.balance(accountId));
        }
        case "withdraw": {
          int accountId = Integer.parseInt(command[1]);
          int amount = Integer.parseInt(command[2]);

          if (amount < 0) {
            throw new IllegalArgumentException("Amount should be positive");
          }

          manager.withdraw(accountId, amount);
          return "Successful operation";
        }
        case "deposit": {
          int accountId = Integer.parseInt(command[1]);
          int amount = Integer.parseInt(command[2]);

          if (amount < 0) {
            throw new IllegalArgumentException("Amount should be positive");
          }

          manager.deposit(accountId, amount);
          return "Successful operation";
        }
        case "transfer": {
          int accountFrom = Integer.parseInt(command[1]);
          int accountTo = Integer.parseInt(command[2]);
          int amount = Integer.parseInt(command[3]);

          if (amount < 0) {
            throw new IllegalArgumentException("Amount should be positive");
          }

          manager.transfer(accountFrom, accountTo, amount);
          return "Successful operation";
        }
        case "e": {
          reader.close();
          return "Exit";
        }
        default: {
          return "Invalid command";
        }
      }
    } catch (IOException e) {
      return Arrays.toString(e.getStackTrace());

    } catch (DaoException e) {
      return "Not sure what to do with DAO exceptions\n\n" + Arrays.toString(e.getStackTrace());

    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      return "Invalid command";

    } catch (UnknownAccountException | NotEnoughMoneyException | IllegalArgumentException e) {
      return e.getMessage();
    }
  }
}
