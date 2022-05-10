package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    UserService userService = new UserService("http://localhost:8080/user/");
    AccountService accountService = new AccountService("http://localhost:8080/account/");
    TransferService transferService = new TransferService("http://localhost:8080/transfer/");
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private ServiceBase serviceBase;

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
        else {
            ServiceBase.setAuthToken(currentUser.getToken());
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        BigDecimal balance = accountService.getBalance(currentUser);
        System.out.println("Your current account balance is: $" + balance);
        //make UI option ^^
    }

	private void viewTransferHistory() {
        List<Transfer> transfers = transferService.getHistory(currentUser);
        Boolean isInList = false;
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID \t \t \t From/To \t \t \t Amount");
        System.out.println("-------------------------------------------");
        for (Transfer transfer : transfers) {
            if (!accountService.getUsername(transfer.getAccountFrom()).equals(currentUser.getUser().getUsername())) {
                if (transfer.getTransferStatusId().equals(2L)) {
                    System.out.println(transfer.getTransferId() + " \t \t From: " + accountService.getUsername(transfer.getAccountFrom()) + " \t \t $ " + transfer.getAmount());
                }
            } else {
                if (transfer.getTransferStatusId().equals(2L)) {
                    System.out.println(transfer.getTransferId() + "\t \t To: " + accountService.getUsername(transfer.getAccountTo()) + " \t \t \t $ " + transfer.getAmount());
                }
            }
        }
        System.out.println("-------------------------------------------");
        long input = (long) consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
        //make UI option ^^
        for (Transfer transfer : transfers) {
            if (transfer.getTransferId() == input) {
                isInList = true;
                break;
            }
        }
        if (input == 0) {
           return;
        }
        else if (transferService.getTransfer(input) != null && isInList)
        {
            viewTransferDetails(input);
        } else {
            System.out.println("Not a valid Transfer Id");
        }
    }

    private void viewTransferDetails(Long transferId)
    {
        Transfer transfer = transferService.getTransfer(transferId);
        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");
        System.out.println("Id: " + transfer.getTransferId());
        System.out.println("From: " + accountService.getUsername(transfer.getAccountFrom()));
        System.out.println("To: " + accountService.getUsername(transfer.getAccountTo()));
        System.out.println("Type: " + transfer.transferType());
        System.out.println("Status: " + transfer.transferStatus());
        System.out.println("Amount: $" + transfer.getAmount());
    }


	private void viewPendingRequests() {
        Boolean isInList = false;

        List<Transfer> requests = new ArrayList<>();
        requests = transferService.getRelevantPendingRequests(currentUser);

        if(requests.size() == 0)
        {
            System.out.println("No pending requests...");
            return;
        }

        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID \t \t \t From/To \t \t \t Amount");
        System.out.println("-------------------------------------------");
        for (Transfer transfer : requests)
        {
            if (!accountService.getUsername(transfer.getAccountFrom()).equals(currentUser.getUser().getUsername()))
            {
                System.out.println(transfer.getTransferId() + " \t \t From: " + accountService.getUsername(transfer.getAccountFrom()) + " \t \t $ " + transfer.getAmount());
            }
        }
        System.out.println("-------------------------------------------");
        long input = (long) consoleService.promptForInt("Please enter transfer ID to accept or deny request: ");


        for (Transfer transfer : requests)
        {
            if (transfer.getTransferId() == input)
            {
                isInList = true;
                break;
            }
        }
        if (input == 0)
        {
            return;
        }
        else if (transferService.getTransfer(input) != null && isInList)
        {
            manageRequest(input);
        }
        else
        {
            System.out.println("Not a valid Transfer Id");
        }
	}

    private void manageRequest(Long transferId)
    {
        viewTransferDetails(transferId);

        String userDecision = consoleService.promptForString("Would you like to (a)ccept, (d)eny, or (i)gnore this request? ");

        Transfer transfer = transferService.getTransfer(transferId);

        switch (userDecision.toLowerCase())
        {
            case "a":
            case "accept":
                transferService.confirmRequest
                (
                        transfer
                );
                break;
            case "d":
            case "deny":
                transferService.denyRequest(transferId);
                break;
            case "i":
            case "ignore":
                break;
            default:
                System.out.println("Error - please enter a valid answer");
                manageRequest(transferId);
        }
    }


	private void sendBucks() {
        BigDecimal amount = null;

        List<String> usernames = userService.getAllUsernames();

        System.out.println("-------------------------------------------");
        System.out.println("Make a transfer");
        String usernameInput = consoleService.promptForString("Username of recipient (enter (s)how for available users): ");



       if(usernameInput.toLowerCase().equals("s")|| usernameInput.toLowerCase().equals("show")) //display list of all users but logged in user
        {
            System.out.println("-------------------------------------------");
            System.out.println("\nList of available users:");
            for (String username : usernames)
            {
                if (!username.equals(currentUser.getUser().getUsername())) // need to exclude current user
                {
                    System.out.println(username);
                }
            }
            System.out.println("");
            sendBucks();
        }
       else if (!usernames.contains(usernameInput) || usernameInput.equals(currentUser.getUser().getUsername())) {
           System.out.println("Not a valid user, unable to process request");
       }

        else
        {
            amount = consoleService.promptForBigDecimal("Amount to send (in decimal format): ");
            System.out.println("-------------------------------------------");
            Long accountTo = userService.getAccountId(usernameInput.trim());
            Long accountFrom = accountService.getAccountId(currentUser);
            if (amount.compareTo(accountService.getBalance(currentUser)) <= 0) {
                transferService.makeTransfer(2L,2L, accountFrom, accountTo, amount);
                viewCurrentBalance();
            }
            else {
                System.out.println("Invalid transfer amount, not enough money");
            }
        }
        return;
	}

	private void requestBucks() {
        BigDecimal amount = null;

        List<String> usernames = userService.getAllUsernames();

        System.out.println("-------------------------------------------");
        System.out.println("Make a request");
        String usernameInput = consoleService.promptForString("Username of recipient (enter (s)how for available users): ");

        if(usernameInput.toLowerCase().equals("s") || usernameInput.toLowerCase().equals("show")) //display list of all users but logged in user
        {
            System.out.println("-------------------------------------------");
            System.out.println("\nList of available users:");
            for (String username : usernames)
            {
                if (!username.equals(currentUser.getUser().getUsername())) // need to exclude current user
                {
                    System.out.println(username);
                }
            }
            System.out.println("");
            requestBucks();
        }

        else
        {
            amount = consoleService.promptForBigDecimal("Amount to send (in decimal format): ");
            System.out.println("-------------------------------------------");
            Long accountTo = userService.getAccountId(usernameInput.trim());
            Long accountFrom = accountService.getAccountId(currentUser);
            transferService.makeTransfer(1L,1L, accountFrom, accountTo, amount);
            System.out.println("Your transfer request is pending.");
            viewCurrentBalance();
        }
        return;
    }

}
