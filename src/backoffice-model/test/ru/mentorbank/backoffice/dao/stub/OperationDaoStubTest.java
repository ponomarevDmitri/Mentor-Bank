package ru.mentorbank.backoffice.dao.stub;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ru.mentorbank.backoffice.dao.OperationDao;
import ru.mentorbank.backoffice.dao.exception.OperationDaoException;
import ru.mentorbank.backoffice.model.Account;
import ru.mentorbank.backoffice.model.Operation;
import ru.mentorbank.backoffice.model.stoplist.StopListInfo;
import ru.mentorbank.backoffice.model.stoplist.StopListStatus;
import ru.mentorbank.backoffice.test.AbstractSpringTest;

public class OperationDaoStubTest extends AbstractSpringTest {

	@Autowired
	public OperationDao operationDao;

	@Test
	public void getOperations() throws OperationDaoException {
		Set<Operation> operaions = operationDao.getOperations();
		assertOperationsAreInAskSequrityStatus(operaions);
	}

	private void assertOperationsAreInAskSequrityStatus(Set<Operation> operaions) {
		assertNotNull(operaions);
		for (Operation operation : operaions) {
			if (StopListStatus.ASKSECURITY != operation.getDstStoplistInfo()
					.getStatus()) {
				fail("������ �������� ������ ���� ASKSECURITY");
			}
		}
	}
	
	
	
	@Test
	public void saveOperation(){
		assertNotSaveWhenOperationNull();
		
		StopListInfo stopListInfo = new StopListInfo();
		stopListInfo.setStatus(StopListStatus.OK);
		Calendar calendar = Calendar.getInstance();
		Account account = new Account();
		account.setAccountNumber("12");
		
		Operation operation = new Operation();
		operation.setCreateDate(calendar);
		operation.setDstAccount(account);
		operation.setSrcAccount(account);
		operation.setSrcStoplistInfo(stopListInfo);
		operation.setDstStoplistInfo(stopListInfo);
		
		assertSaveWithKnownOperation(operation);
	}
	
	private void assertNotSaveWhenOperationNull(){
		try{
			operationDao.saveOperation(null);
		}
		catch(OperationDaoException exeption){
			return;
		}
		fail("���������� ���������� null");
	}
	
	private void assertSaveWithKnownOperation(Operation operation){
		try{
			operationDao.saveOperation(operation);
		}
		catch(Exception e){
			fail("�� ���������� ���������� �������� ��������");
		}
		
	}
	
	
}
