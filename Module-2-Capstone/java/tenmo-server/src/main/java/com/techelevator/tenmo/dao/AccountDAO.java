package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {

	//this method is not used
	//void createAccount(Account accounts);

	double getAccountBalance(long User_id);

	// This method is not used
	//List<Account> list();

	void updateAccount(long transferTo, long transferFrom, double amount) throws Exception;

	// This method is not used
	//void deleteAccount (long User_id);
}
