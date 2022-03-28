package com.example.demo;

import lombok.Data;

// setter/getterを付けるアノテーション

@Data

public class Company_data {
	private String company;
	private String adress;
	private String latitude;
	private String longitude;
	private String DISTANCE;

	// Default Constructor
	public Company_data() {
	}

	public Company_data(String company, String adress, String latitude, String longitude, String DISTANCE){
		this.company = company;
		this.adress = adress;
		this.latitude = latitude;
		this.longitude = longitude;
		this.DISTANCE = DISTANCE;
	}
}