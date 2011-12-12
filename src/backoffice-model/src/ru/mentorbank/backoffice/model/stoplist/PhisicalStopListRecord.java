package ru.mentorbank.backoffice.model.stoplist;

import java.io.Serializable;


public class PhisicalStopListRecord implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String documentSeries;
	private String documentNumber;
	
	private String firstname;
	private String lastname;
	private String middlename;
	
	public PhisicalStopListRecord(String documentSeries,
	                              String documentNumber,
	                              String firstname,
	                              String lastname,
	                              String middlename){
		this.documentNumber = documentNumber;
		this.documentSeries = documentSeries;
		this.firstname = firstname;
		this.lastname = lastname;
		this.middlename = middlename;
	}

	public String getDocumentSeries() {
		return documentSeries;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getMiddlename() {
		return middlename;
	}
	
	
	
	public boolean equals(Object object){
		boolean result = false;
		if(object instanceof PhisicalStopListRecord){
			PhisicalStopListRecord record = (PhisicalStopListRecord)object;
			if((record.documentSeries.equals(this.documentSeries)) && 
			   (record.documentNumber.equals(this.documentNumber)) && 
			   (record.firstname.equals(this.firstname)) &&
			   (record.lastname.equals(this.lastname)) &&
			   (record.middlename.equals(this.middlename)))
				result = true;
		}
		return result;
	}
	
	public int hashCode(){
		int result = 20;
		result = result*20 + documentSeries.hashCode();
		return result;
	}
	
	
	
}
