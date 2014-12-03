package com.rdr.rmaptest.dto;

import java.io.Serializable;

public class MarkerDTO implements Serializable{


	private static final long serialVersionUID = 1L;
	private LatLngDTO latlng;
	private String title;
	private String snippet;
	
	public LatLngDTO getLatlng() {
		return latlng;
	}
	
	public void setLatlng(LatLngDTO latlng) {
		this.latlng = latlng;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSnippet() {
		return snippet;
	}
	
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

}
