package ru.mentorbank.backoffice.services.moneytransfer;


import java.util.Calendar;

import ru.mentorbank.backoffice.dao.OperationDao;
import ru.mentorbank.backoffice.dao.exception.OperationDaoException;
import ru.mentorbank.backoffice.dao.stub.OperationDaoStub;
import ru.mentorbank.backoffice.model.Operation;
import ru.mentorbank.backoffice.model.stoplist.JuridicalStopListRequest;
import ru.mentorbank.backoffice.model.stoplist.PhisicalStopListRecord;
import ru.mentorbank.backoffice.model.stoplist.PhysicalStopListRequest;
import ru.mentorbank.backoffice.model.stoplist.StopListInfo;
import ru.mentorbank.backoffice.model.stoplist.StopListStatus;
import ru.mentorbank.backoffice.model.transfer.AccountInfo;
import ru.mentorbank.backoffice.model.transfer.JuridicalAccountInfo;
import ru.mentorbank.backoffice.model.transfer.PhysicalAccountInfo;
import ru.mentorbank.backoffice.model.transfer.TransferRequest;
import ru.mentorbank.backoffice.services.accounts.AccountService;
import ru.mentorbank.backoffice.services.accounts.AccountServiceBean;
import ru.mentorbank.backoffice.services.moneytransfer.exceptions.TransferException;
import ru.mentorbank.backoffice.services.stoplist.StopListService;
import ru.mentorbank.backoffice.services.stoplist.StopListServiceStub;

public class MoneyTransferServiceBean implements MoneyTransferService {

	public static final String LOW_BALANCE_ERROR_MESSAGE = "Can not transfer money, because of low balance in the source account";
	private AccountService accountService;
	private StopListService stopListService;
	private OperationDao operationDao;
	
	public MoneyTransferServiceBean(){
		accountService = new AccountServiceBean();
		stopListService = new StopListServiceStub();
		operationDao = new OperationDaoStub();
	}
	

	public void transfer(TransferRequest request) throws TransferException {
		// ������ ����� ��������� ����������� ������, ��� ����, ����� �����
		// ���� ������� � ��������� ������� ���������� �� ������� �������.
		// ��� ��� MoneyTransferServiceBean ��������������� ��� singleton
		// scoped, �� � �� ������ ������� ��������� ������ ������� ��-��
		// ������� ������������� �������.
		new MoneyTransfer(request).transfer();
	}

	class MoneyTransfer {

		private TransferRequest request;
		private StopListInfo srcStopListInfo;
		private StopListInfo dstStopListInfo;

		public MoneyTransfer(TransferRequest request) {
			this.request = request;
		}

		public void transfer() throws TransferException {
			verifySrcBalance();
			initializeStopListInfo();
			saveOperation();
			if (isStopListInfoOK()) {
				transferDo();
				removeSuccessfulOperation();
			} else
				throw new TransferException(
						"���������� ������� �������. ���������� ������ �������������.");
		}

		/**
		 * ���� �������� �������� ������, �� � ����� ������� �� �������
		 * �������� ��� ������� �������������
		 */
		private void removeSuccessfulOperation() {

		}

		private void initializeStopListInfo() {
			srcStopListInfo = getStopListInfo(request.getSrcAccount());
			dstStopListInfo = getStopListInfo(request.getDstAccount());
		}

		private void saveOperation() {
			// TODO: ���������� ������� ����� �������� saveOperation � �������
			// ��������������� ���� ������ �������� operationDao.saveOperation()
			Operation operation = new Operation();
			
			operation.setSrcAccountFromAccountInfo(request.getSrcAccount());
			operation.setDstAccountFromAccountInfo(request.getDstAccount());
			operation.setDstStoplistInfo(dstStopListInfo);
			operation.setSrcStoplistInfo(srcStopListInfo);
			operation.setCreateDate(Calendar.getInstance());
			operation.setSentDate(Calendar.getInstance());
			
			try{
				operationDao.saveOperation(operation);
			}
			catch(OperationDaoException daoException){
				
			}
			
		}

		private void transferDo() throws TransferException {
			// ��� �������� ���� �� ������������. ��� ������ ��������
			// CDCMoneyTransferServiceConsumer �������� ��� ���
		}

		private boolean isStopListInfoOK() {
			if (StopListStatus.OK.equals(srcStopListInfo.getStatus())
					&& StopListStatus.OK.equals(dstStopListInfo.getStatus())) {
				return true;
			}
			return false;
		}

		private StopListInfo getStopListInfo(AccountInfo accountInfo) {
			if (accountInfo instanceof JuridicalAccountInfo) {
				JuridicalAccountInfo juridicalAccountInfo = (JuridicalAccountInfo) accountInfo;
				JuridicalStopListRequest request = new JuridicalStopListRequest();
				request.setInn(juridicalAccountInfo.getInn());
				StopListInfo stopListInfo = stopListService
						.getJuridicalStopListInfo(request);
				return stopListInfo;
			} else if (accountInfo instanceof PhysicalAccountInfo) {
				// TODO: ������� ����� stopListService ��� ���������� ���
				PhysicalAccountInfo physicalAccountInfo = (PhysicalAccountInfo) accountInfo;
				PhysicalStopListRequest request = new PhysicalStopListRequest();
				
				request.setDocumentNumber(physicalAccountInfo.getDocumentNumber());
				request.setDocumentSeries(physicalAccountInfo.getDocumentSeries());
				request.setFirstname(request.getFirstname());
				request.setLastname(request.getLastname());
				request.setMiddlename(request.getMiddlename());
				
				StopListInfo stopListInfo = stopListService.getPhysicalStopListInfo(request);
				return stopListInfo;
			}
			return null;
		}

		private boolean processStopListStatus(StopListInfo stopListInfo)
				throws TransferException {
			if (!StopListStatus.ASKSECURITY.equals(stopListInfo.getStatus())) {
				return false;
			}
			return true;
		}

		private void verifySrcBalance() throws TransferException {
			if (!accountService.verifyBalance(request.getSrcAccount()))
				throw new TransferException(LOW_BALANCE_ERROR_MESSAGE);
		}
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public void setStopListService(StopListService stopListService) {
		this.stopListService = stopListService;
	}

	public void setOperationDao(OperationDao operationDao) {
		this.operationDao = operationDao;
	}
}
