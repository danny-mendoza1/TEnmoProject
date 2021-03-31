package com.techelevator.tenmo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TenmoApplicationServices;
import com.techelevator.view.ConsoleService;
import com.techelevator.tenmo.models.AccountUser;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private TenmoApplicationServices applicationService = new TenmoApplicationServices();

    public static void main(String[] args) throws Exception {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() throws Exception {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() throws Exception {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}
	

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
		double balance = applicationService.getAccountBalance(currentUser.getToken());
		System.out.println("Your current balance is: " + balance);
		
		
	}

	private void viewTransferHistory() {
		System.out.println("Press 1 to veiw of all your transfers or press 2 to view a specific transfer");
		Scanner newScanner = new Scanner(System.in);
		String userInput = newScanner.nextLine();
		if(userInput.equals("1")) {
			Transfer [] transfers = applicationService.listTransfers(currentUser.getToken());
			for (Transfer theTransfers : transfers) {
				System.out.println(theTransfers.toString());
			}
		}else {
			System.out.println("Enter the Id of the transfer you would like to see");
			Scanner newScanner2 = new Scanner(System.in);
			String userInput2 = newScanner2.nextLine();
			long Id = Long.parseLong(userInput2);
			Transfer aTransfer = new Transfer();
			aTransfer = applicationService.getTransferById(currentUser.getToken(), Id);
			System.out.println(aTransfer.toString());
		}

	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() throws Exception {
		
		List<AccountUser> userList = new ArrayList<AccountUser>();
		
		userList = applicationService.listUsers(currentUser.getToken());
		
		for(int i=0; i < userList.size(); i++) {
			System.out.println(userList.get(i));
		}
		
		
		System.out.println("-------------------------------------");
		System.out.print("Please select the ID of the user you would like to send TE bucks to: ");
		
		
		Scanner newScanner = new Scanner(System.in);
		String userInput = newScanner.nextLine();
		
		
		Long accountTo = 0L;
		
		Transfer aTransfer = new Transfer();
		accountTo = Long.parseLong(userInput);
		aTransfer.setTransfer_to(accountTo);

	
		System.out.print("Please enter how much would you like to send: $");
		userInput = newScanner.nextLine();
			double amount = Double.parseDouble(userInput);
		aTransfer.setAmount(amount);
	
		
		 applicationService.makeTransfer(currentUser.getToken(),aTransfer);
			System.out.println("Transfer completed succesfully!");
				
			
		}
	
	
	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.out.println("Thank you for using Tenmo!");
		System.exit(0);
		
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}
	

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
