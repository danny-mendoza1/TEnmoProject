package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.AccountUser;

@Component
public class AccountUserDAOJDBC implements AccountUserDAO {
	private JdbcTemplate jdbcTemplate;
	
	public AccountUserDAOJDBC (JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		
	}
	
	@Override
	public List<AccountUser> listUsers(){
		List<AccountUser> listOfUsers = new ArrayList<AccountUser>();		// Instantiate the List of users to be returned
		String getListOfUsers = "SELECT user_id, username FROM users";		// Prepare SQL query statement
		SqlRowSet userList = jdbcTemplate.queryForRowSet(getListOfUsers);	// pass the jdbcTemplate method our prepared string return the data as an SQLRowSet
		
		while (userList.next()) {											// While we have data keep looping
			AccountUser aUser = new AccountUser();							// Instantiate a new User object that will be held in the list
			aUser = mapRowToAccountUser(userList);							// use our helper method to change the data from the SQLRowSet to the data we need
			listOfUsers.add(aUser);											// add the User object to the list of users
			}																// Loop until our SQLRowSet contains no more data
		return listOfUsers;													// Finally return the list of users
		
	}
	
	private AccountUser mapRowToAccountUser(SqlRowSet results) {			// This helper method returns an Account user and takes an SQLRowSet as a parameter
		AccountUser theUser = new AccountUser();							// First instantiate the user object to be returned				
		theUser.setUser_id(results.getLong("user_id"));						// Using our public methods from the model set the appropriate user id
		theUser.setUsername(results.getString("username"));					// Same as above but now set the userName
		return theUser;														// Finally return the user object with the data we want
	}

}
