package ru.mentorbank.backoffice.services.moneytransfer;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ru.mentorbank.backoffice.model.stoplist.JuridicalStopListRequest;
import ru.mentorbank.backoffice.model.stoplist.StopListInfo;
import ru.mentorbank.backoffice.model.stoplist.StopListStatus;
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
		// TODO: Ќеобходимо протестировать, что дл€ хорошего перевода всЄ
		// работает и вызываютс€ все необходимые методы сервисов
		// ƒалее следует закоментированна€ закотовка
		 StopListService mockedStopListService = (StopListServiceStub)mock(StopListServiceStub.class);
		 AccountService mockedAccountService = (AccountServiceBean)mock(AccountServiceBean.class);
		 
		 //==============порождение "хорошего" тестового запроса============
		 moneyTransferService = new MoneyTransferServiceBean();
		 TransferRequest request = new TransferRequest();
		 JuridicalAccountInfo accountInfo = new JuridicalAccountInfo();
		 accountInfo.setAccountNumber("2432");
		 accountInfo.setInn("1111111111111");
		 request.setDstAccount(accountInfo);
		 request.setSrcAccount(accountInfo);
		 
		 moneyTransferService.transfer(request);
		
		 //verify(mockedStopListService).getJuridicalStopListInfo(null);
		 //verify(mockedAccountService).verifyBalance(null);
		 verify(mockedStopListService);
		 verify(mockedAccountService);
	}
	
	
	private void verify(Object object){
		if(object instanceof StopListService){
			JuridicalStopListRequest request = new JuridicalStopListRequest();//предполагаемый "хороший" запрос
			request.setInn("1111111111111");
			StopListInfo stoplistInfo = ((StopListService) object).getJuridicalStopListInfo(request);
			if(stoplistInfo.getStatus() != StopListStatus.OK)
				fail("getJuridicalStopListInfo возвращает неправильное значение");
		}
		else
			if(object instanceof AccountService){
				if(((AccountService) object).verifyBalance(null))
					fail("проходит положительна€ проверка баланса при AccountInfo = null");
				AccountInfo accountInfo = new AccountInfo();
				accountInfo.setAccountNumber("555");  //предполагаемый "хороший" аккаунт
				if(!((AccountService) object).verifyBalance(accountInfo))
					fail("перевод c \"хорошего\" аккаунта не проходит");
			}
	}
	
	private Object mock(Class<?> class1){
		
		if(class1 == AccountServiceBean.class){
			AccountServiceBean resultOb = new AccountServiceBean();
			return resultOb;
		}
		if(class1 == StopListServiceStub.class){
			StopListServiceStub resultStopList = new StopListServiceStub();
			return resultStopList;
		}
		
		return null;
	}
	
}

