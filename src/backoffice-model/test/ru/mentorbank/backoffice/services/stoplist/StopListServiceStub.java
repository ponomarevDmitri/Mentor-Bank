package ru.mentorbank.backoffice.services.stoplist;

import java.util.HashMap;


import ru.mentorbank.backoffice.model.stoplist.JuridicalStopListRequest;
import ru.mentorbank.backoffice.model.stoplist.PhisicalStopListRecord;
import ru.mentorbank.backoffice.model.stoplist.PhysicalStopListRequest;
import ru.mentorbank.backoffice.model.stoplist.StopListInfo;
import ru.mentorbank.backoffice.model.stoplist.StopListStatus;
import ru.mentorbank.backoffice.services.stoplist.StopListService;

public class StopListServiceStub implements StopListService {

	public static final String INN_FOR_OK_STATUS = "1111111111111";
	public static final String INN_FOR_STOP_STATUS = "22222222222222";
	public static final String INN_FOR_ASKSECURITY_STATUS = "33333333333333";

	//hash map сопоставляет известные физ лица с их статусами в стоп листе
	public static final HashMap<PhisicalStopListRecord, StopListStatus> phisicalStopList = new HashMap<PhisicalStopListRecord, StopListStatus>(){/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
		
		put(new PhisicalStopListRecord("2", "2", "2", "2", "2"), StopListStatus.OK);
	}};
	
	
	
	
	
	@Override
	public StopListInfo getJuridicalStopListInfo(
			JuridicalStopListRequest request) {
		StopListInfo stopListInfo = new StopListInfo();
		stopListInfo.setComment("Комментарий");
		if (INN_FOR_OK_STATUS.equals(request.getInn())){			
			stopListInfo.setStatus(StopListStatus.OK);
		} else if (INN_FOR_STOP_STATUS.equals(request.getInn())) {
			stopListInfo.setStatus(StopListStatus.STOP);			
		} else {
			stopListInfo.setStatus(StopListStatus.ASKSECURITY);			
		}
		return stopListInfo;
	}

	@Override
	public StopListInfo getPhysicalStopListInfo(PhysicalStopListRequest request) {
		//TODO: Реализовать
		StopListInfo resultInfo = new StopListInfo();
		resultInfo.setComment("comment");
		
		PhisicalStopListRecord consideredRecord = new PhisicalStopListRecord(request.getDocumentSeries(), 
																			 request.getDocumentNumber(),
																			 request.getFirstname(), 
																			 request.getLastname(), 
																			 request.getMiddlename());
		StopListStatus status = phisicalStopList.get(consideredRecord);
		if(status != null)
			resultInfo.setStatus(status);
		else
			resultInfo.setStatus(StopListStatus.ASKSECURITY);
		return resultInfo;
	}

}
