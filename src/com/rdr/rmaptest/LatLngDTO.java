package com.rdr.rmaptest;

import java.io.Serializable;

public class LatLngDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	private double latitude;
	private double longitude;
	
	
	public LatLngDTO(double latitude, double longitude){
		this.latitude = latitude;
		this.longitude = longitude;

	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

}