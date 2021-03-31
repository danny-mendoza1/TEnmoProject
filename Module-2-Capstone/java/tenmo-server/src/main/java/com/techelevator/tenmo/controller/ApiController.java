package com.techelevator.tenmo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.AccountDAOJDBC;
import com.techelevator.tenmo.dao.AccountUserDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.AccountUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

/*******************************************************************************************************
 * This is where you code any API controllers you may create
********************************************************************************************************/
@RestController 


public class ApiController {

private AccountDAO accountDAO;
private UserDAO userDAO;
private AccountUserDAO accountUserDAO;
private TransferDAO transferDAO;


public ApiController(AccountDAOJDBC accountDAO, UserDAO userDAO, AccountUserDAO accountUserDAO, TransferDAO transferDAO) {
	this.accountDAO = accountDAO;
	this.userDAO = userDAO;
	this.accountUserDAO = accountUserDAO;
	this.transferDAO = transferDAO;

}

@RequestMapping(path = "/account/balance", method = RequestMethod.GET)
public double getAccountBalance(Principal userInfo) {				// Principal is a Spring boot security feature that contains the user information
	long userId = userDAO.findIdByUsername(userInfo.getName());		// I understand the use of principal allowed us to get the ID but where does getName come from?
    return accountDAO.getAccountBalance(userId);
    	
}

@RequestMapping (path = "/users", method = RequestMethod.GET)
public List<AccountUser> listUsers(){
	List<AccountUser> theUsers = new ArrayList();
	theUsers = accountUserDAO.listUsers();
	return theUsers;
}

@RequestMapping (path = "/transfer", method = RequestMethod.POST)
public void update(Principal userInfo, @RequestBody Transfer aTransfer) throws Exception {	// @RequestBody takes data out of the request body and makes a transfer object
	long transferFrom = userDAO.findIdByUsername(userInfo.getName());
	 transferDAO.makeTransfer(aTransfer.getTransfer_to(), transferFrom, aTransfer.getAmount());
	
}
@RequestMapping (path = "/transfer/list", method = RequestMethod.GET)
public List<Transfer> getAllTransfers(){
	List<Transfer> results = transferDAO.getAllTransfers();
	return results;
}

@RequestMapping (path = "/transfer/{id}", method = RequestMethod.GET)
public Transfer getTransferById(@PathVariable long id) {				// Id is passed as part of the path
	Transfer aTransfer = new Transfer();
	aTransfer = transferDAO.getTransferById(id);
			return aTransfer;
}

}
