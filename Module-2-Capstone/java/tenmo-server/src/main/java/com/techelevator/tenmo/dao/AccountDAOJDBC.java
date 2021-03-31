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


	public double getAccountBalance(long user_id) {						// this method will return a numeric value based upon the user_id passed
		Account anAccount = new Account();								// First instantiate anAccount object that will contain all the data of the account
		String SqlGetAccountBalance = "SELECT * FROM accounts WHERE user_id = ?";	//Set up the SQL string to do so
		SqlRowSet accountBalance = jdbcTemplate.queryForRowSet(SqlGetAccountBalance, user_id);	// queryForRowSet passing in the SQL string and user_id passed in
		if (accountBalance.next()) {									// if we have data
			anAccount = mapRowToAccount(accountBalance);				// Use our helper method to set up the Account object with the appropriate data
		}
		return anAccount.getBalance();									// Finally return the account balance using our public getter from the model
	}


	private Account mapRowToAccount(SqlRowSet results) {				// This helper method will receive SQLRowSet data and return an Account object
		Account theAccount = new Account();								// First instantiate the Account object to be returned
		theAccount.setAccount_id(results.getLong("account_id"));		// set the data using our public setters and obtain the data from our SQLRowSet
		theAccount.setUser_id(results.getLong("user_id"));				// since the id's are both serial values in the database we use .getLong
		theAccount.setBalance(results.getDouble("balance"));			// balance is just a numeric data type and so we use .getDouble
		return theAccount;												// Finally return the Account object with all the data properly formatted
	}
}
