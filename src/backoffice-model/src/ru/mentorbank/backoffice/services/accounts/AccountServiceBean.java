package ru.mentorbank.backoffice.services.accounts;

import ru.mentorbank.backoffice.model.transfer.AccountInfo;

public class AccountServiceBean implements AccountService {

	@Override
	public boolean verifyBalance(AccountInfo account) {
		if(account != null)
			return true;
		return false;
	}

}
