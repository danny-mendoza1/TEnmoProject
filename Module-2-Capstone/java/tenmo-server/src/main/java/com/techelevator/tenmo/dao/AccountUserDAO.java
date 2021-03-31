package com.techelevator.tenmo.dao;

import java.util.List;
import com.techelevator.tenmo.model.AccountUser;

// This interface was named AccountUser instead of Users to avoid confusion with the already made User DAO
public interface AccountUserDAO {
	
	List<AccountUser> listUsers();

}
