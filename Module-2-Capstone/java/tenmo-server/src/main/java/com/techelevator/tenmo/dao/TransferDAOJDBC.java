package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.dao.AccountDAO;

@Component
public class TransferDAOJDBC implements TransferDAO {

	private JdbcTemplate jdbcTemplate;
	private AccountDAO account;							// We want to use a method from the AccountDAO and so it must be referenced

	public TransferDAOJDBC (JdbcTemplate jdbcTemplate, AccountDAO account) {	//Using dependency injection we can access the Account DAO methods
		this.jdbcTemplate = jdbcTemplate;
		this.account = account;

	}

	public void makeTransfer(long transferTo, long transferFrom, double amount) throws Exception {	// Method to do a transfer
		String SqlUpdateTransfers = "INSERT INTO transfers"										// We are inserting data into the transfer table
				+ " (transfer_status_id, transfer_type_id,"										// set up the SQL string to be passed to the jdbcTemplate
				+ " account_to, account_from, amount)"
				+ " VALUES(2, 2, ?, ?, ?)";												// status and type id are both 2 to represent approved transaction
		if (amount > account.getAccountBalance(transferFrom)) {							// if the amount to transfer is greater than the amount in the balance
			throw new Exception("You cannot transfer more money than you have!");		// throw an exception with the appropriate message
		} else																			// otherwise do the transfer
		{ 
			jdbcTemplate.update(SqlUpdateTransfers,transferTo,transferFrom,amount);			// call the update method passing in our string and place holders


			String SqlUpdateFromAccount = "UPDATE accounts"									// update the account where the money was transfered from
					+ " SET balance = balance - ?"											// set up the appropriate SQL string decrease the balance by the amount
					+ " WHERE account_id = ?";												// from the account ID passed in
			jdbcTemplate.update(SqlUpdateFromAccount, amount, transferFrom);				// pass the SQL string and our place holders using the update method


			String SqlUpdateToAccount = "UPDATE accounts"									// update the account where the money was transfered to
					+ " SET balance = balance + ?"											// increase the balance by the passed in amount
					+ " WHERE account_id = ?";												// with the appropriate account ID
			jdbcTemplate.update(SqlUpdateToAccount, amount, transferTo);					// call the update method using the appropriate parameters

		}

	}

	public List<Transfer> getAllTransfers(){									// This method will return a list of Transfer objects and take no parameters
		List<Transfer> listOfTransfers = new ArrayList<>();						// First instantiate the list to be returned
		String sqlGetAllTransfers = "SELECT * FROM transfers";					// SQL string to return all the data in the transfers table

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllTransfers);	// pass the SQL string into the queryForRowSet() which returns data as SQLRowSet
		while (results.next()) {												// While the SQLRowSet contains data
			Transfer aTransfer = mapRowToTransfer(results);						// Use our helper method to instantiate a new Transfer object with appropriate data
			listOfTransfers.add(aTransfer);										// Add the Transfer object with our data to the listOfTransfers
		}

		return listOfTransfers;													// finally return the list of transfer objects
	}

	public Transfer getTransferById(long id) {									// This method will return a Transfer Object and takes a transfer id as a parameter
		Transfer aTransfer = new Transfer();									// First instantiate the transfer object to be returned
		String sqlGetTransferById = "SELECT *"									// Set up our SQL string to return all the data
				+ " FROM transfers WHERE transfer_id = ?";							// where the transfer_id will be the parameter passed in
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransferById, id);	// queryForRowSet passing the SQL string and transfer ID
		if (results.next()) {														// while we have data
			Transfer theTransfer = mapRowToTransfer(results);						// use our helper method to map the SQLRowSet data to a Transfer Object
			aTransfer =  theTransfer;												// This line may be unnecessary 
		}
		return aTransfer;															// Finally return the Transfer object

	}
	private Transfer mapRowToTransfer(SqlRowSet results) {							// This helper method will return a Transfer Object based upon the SQLRowSet data it recieves as a paramter
		Transfer theTransfer = new Transfer();										// First instantiate the transfer object to be returned
		theTransfer.setTransfer_id(results.getLong("transfer_id"));					// Using our public setters set all the data appropriately
		theTransfer.setTransfer_type_id(results.getLong("transfer_type_id"));
		theTransfer.setTransfer_status_id(results.getLong("transfer_status_id"));
		theTransfer.setTransfer_from(results.getLong("account_from"));				// Recall that Transfer_from was used in place of account_from
		theTransfer.setTransfer_to(results.getLong("account_to"));					// And Transfer_to was used in place of account_to
		theTransfer.setAmount(results.getDouble("amount"));							// amount is a numeric type in the database and can be found using getDouble
		return theTransfer;															// Finally return the Transfer Object with all the correct data
	}


}
