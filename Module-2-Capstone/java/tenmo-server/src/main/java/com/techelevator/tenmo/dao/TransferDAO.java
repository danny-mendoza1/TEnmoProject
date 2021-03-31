package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	
	void makeTransfer(long transferTo, long transferFrom, double amount) throws Exception;
	
	List<Transfer> getAllTransfers();
		Transfer getTransferById(long id);
}
