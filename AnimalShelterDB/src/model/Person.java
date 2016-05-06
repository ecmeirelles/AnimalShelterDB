package model;

import java.util.ArrayList;

public class Person {
	private String personName;
	private String personAddress;
	private String personPhone;
	private String personEmail;
	private ArrayList<Person> personList = new ArrayList<Person>();
	
	public Person() {
		
	}
	
	public Person(String personName, String personAddress, String personPhone, String personEmail) {
		this.personName = personName;
		this.personAddress = personAddress;
		this.personPhone = personPhone;
		this.personEmail = personEmail;
		
		personList.add(this);
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonAddress() {
		return personAddress;
	}

	public void setPersonAddress(String personAddress) {
		this.personAddress = personAddress;
	}

	public String getPersonPhone() {
		return personPhone;
	}

	public void setPersonPhone(String personPhone) {
		this.personPhone = personPhone;
	}

	public String getPersonEmail() {
		return personEmail;
	}

	public void setPersonEmail(String personEmail) {
		this.personEmail = personEmail;
	}
	
	public void printPersonScreen() {
		System.out.println("-------------------------------");
		System.out.println("         PERSON DETAILS");
		System.out.println("-------------------------------");
		System.out.println("Name: " + this.personName);
		System.out.println("Address: " + this.personAddress);
		System.out.println("Phone: " + this.personPhone);
		System.out.println("Email: " + this.personEmail);
		System.out.println();
		System.out.println("-------------------------------");
		System.out.println();
	}
}
