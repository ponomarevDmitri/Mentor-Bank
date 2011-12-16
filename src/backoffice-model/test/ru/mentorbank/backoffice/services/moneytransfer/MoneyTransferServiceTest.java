package ru.mentorbank.backoffice.services.moneytransfer;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import ru.mentorbank.backoffice.dao.OperationDao;
import ru.mentorbank.backoffice.dao.exception.OperationDaoException;
import ru.mentorbank.backoffice.dao.stub.OperationDaoStub;
import ru.mentorbank.backoffice.model.Operation;
import ru.mentorbank.backoffice.model.stoplist.JuridicalStopListRequest;
import ru.mentorbank.backoffice.model.transfer.AccountInfo;
import ru.mentorbank.backoffice.model.transfer.JuridicalAccountInfo;
import ru.mentorbank.backoffice.model.transfer.TransferRequest;
import ru.mentorbank.backoffice.services.accounts.AccountService;
import ru.mentorbank.backoffice.services.accounts.AccountServiceBean;
import ru.mentorbank.backoffice.services.moneytransfer.exceptions.TransferException;
import ru.mentorbank.backoffice.services.stoplist.StopListService;
import ru.mentorbank.backoffice.services.stoplist.StopListServiceStub;
import ru.mentorbank.backoffice.test.AbstractSpringTest;

public class MoneyTransferServiceTest extends AbstractSpringTest {

	@Autowired
	private MoneyTransferService moneyTransferService;

	@Before
	public void setUp() {
	}
	
	@Test
	public void transfer() throws TransferException {
		//fail("not implemented yet");
		// TODO: Необходимо протестировать, что для хорошего перевода всё
		// работает и вызываются все необходимые методы сервисов
		// Далее следует закоментированная закотовка
		JuridicalAccountInfo accountInfo = mock(JuridicalAccountInfo.class);
		when(accountInfo.getInn()).thenReturn("1111111111111");
		
		TransferRequest mockRequest = mock(TransferRequest.class);
		when(mockRequest.getDstAccount()).thenReturn(accountInfo);
		when(mockRequest.getSrcAccount()).thenReturn(accountInfo);
		
		StopListService mockedStopListService = new StopListServiceStub();
		StopListService spyStopListService = spy(mockedStopListService);
		
		AccountService mockedAccountService = Mockito.mock(AccountServiceBean.class);
		when(mockedAccountService.verifyBalance(accountInfo)).thenReturn(true);
		OperationDao operationDao = Mockito.mock(OperationDaoStub.class);
		
		((MoneyTransferServiceBean) moneyTransferService).setAccountService(mockedAccountService);
		((MoneyTransferServiceBean)moneyTransferService).setStopListService(spyStopListService);
		((MoneyTransferServiceBean)moneyTransferService).setOperationDao(operationDao);
		moneyTransferService.transfer(mockRequest);
		
		Mockito.verify(spyStopListService,times(2)).getJuridicalStopListInfo(any(JuridicalStopListRequest.class));
		Mockito.verify(mockedAccountService).verifyBalance(any(AccountInfo.class));
		try{
			Mockito.verify(operationDao).saveOperation(any(Operation.class));
		}
		catch(OperationDaoException e){
			fail("DAO exception occured");
		}
	}
}

