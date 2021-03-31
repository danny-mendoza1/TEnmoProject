package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import com.techelevator.tenmo.model.Account;

@Component
public class AccountDAOJDBC implements AccountDAO {

	private JdbcTemplate jdbcTemplate;


	public AccountDAOJDBC (JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;

	}


	//	public void createAccount(Account accounts) {
	//		
	//	}
	
	public double getAccountBalance(long user_id) {						// this method will return a numeric value based upon the user_id passed
		Account anAccount = new Account();								// First instantiate anAccount object that will contain all the data of the account
		String SqlGetAccountBalance = "SELECT * FROM accounts WHERE user_id = ?";	//Set up the SQL string to do so
		SqlRowSet accountBalance = jdbcTemplate.queryForRowSet(SqlGetAccountBalance, user_id);	// queryForRowSet passing in the SQL string and user_id passed in
		if (accountBalance.next()) {									// if we have data
			anAccount = mapRowToAccount(accountBalance);				// Use our helper method to set up the Account object with the appropriate data
		}
		return anAccount.getBalance();									// Finally return the account balance using our public getter from the model
	}

	//	public List<Account> list(){
	//		return null;
	//		
	//	}


	// This method is now makeTransfer in the TransferDAOJDBC
	public void updateAccount(long transferTo, long transferFrom, double amount) throws Exception {
		String SqlUpdateTransfers = "INSERT INTO transfers (transfer_status_id, transfer_type_id, account_to, account_from, amount) VALUES(2, 2, ?, ?, ?)";
		if (amount > getAccountBalance(transferFrom)) {
			throw new Exception("You cannot transfer more money than you have!");
		} else
		{ 
			jdbcTemplate.update(SqlUpdateTransfers,transferTo,transferFrom,amount);


			String SqlUpdateFromAccount = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
			jdbcTemplate.update(SqlUpdateFromAccount, amount, transferFrom);


			String SqlUpdateToAccount = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
			jdbcTemplate.update(SqlUpdateToAccount, amount, transferTo);

		}

	}

	//	public void deleteAccount (long User_id) {
	//		
	//	}
	//	

	private Account mapRowToAccount(SqlRowSet results) {
		Account theAccount;
		theAccount =  new Account();
		theAccount.setAccount_id(results.getLong("account_id"));
		theAccount.setUser_id(results.getLong("user_id"));
		theAccount.setBalance(results.getDouble("balance"));
		return theAccount;
	}


	private long getNextAccountId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_account_id')");
		if(nextIdResult.next()) {
			return nextIdResult.getLong(1);
		}
		else {
			throw new RuntimeException("Something went wrong while getting an Id");
		}
	}
}
