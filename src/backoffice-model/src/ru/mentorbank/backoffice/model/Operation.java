package ru.mentorbank.backoffice.model;

import java.io.Serializable;
import java.util.Calendar;

import ru.mentorbank.backoffice.model.stoplist.StopListInfo;
import ru.mentorbank.backoffice.model.transfer.AccountInfo;

public class Operation implements Serializable {

	private static final long serialVersionUID = 1L;

	private Account srcAccount;
	private Account dstAccount;
	private StopListInfo srcStoplistInfo;
	private StopListInfo dstStoplistInfo;
	
	private Calendar createDate;
	private Calendar sentDate;

	public Account getSrcAccount() {
		return srcAccount;
	}

	public void setSrcAccount(Account srcAccount) {
		this.srcAccount = srcAccount;
	}

	public void setSrcAccountFromAccountInfo(AccountInfo accInfo){
		if(this.srcAccount == null)
			this.srcAccount = new Account();
		this.srcAccount.setAccountNumber(accInfo.getAccountNumber());
	}
	
	public Account getDstAccount() {
		return dstAccount;
	}

	public void setDstAccount(Account dstAccount) {
		this.dstAccount = dstAccount;
	}
	
	public void setDstAccountFromAccountInfo(AccountInfo accInfo){
		if(this.dstAccount == null)
			this.dstAccount = new Account();
		this.dstAccount.setAccountNumber(accInfo.getAccountNumber());
	}

	public Calendar getSentDate() {
		return sentDate;
	}

	public void setSentDate(Calendar sentDate) {
		this.sentDate = sentDate;
	}

	public Calendar getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Calendar createDate) {
		this.createDate = createDate;
	}

	public StopListInfo getSrcStoplistInfo() {
		return srcStoplistInfo;
	}

	public void setSrcStoplistInfo(StopListInfo srcStoplistInfo) {
		this.srcStoplistInfo = srcStoplistInfo;
	}

	public StopListInfo getDstStoplistInfo() {
		return dstStoplistInfo;
	}

	public void setDstStoplistInfo(StopListInfo dstStoplistInfo) {
		this.dstStoplistInfo = dstStoplistInfo;
	}

}
