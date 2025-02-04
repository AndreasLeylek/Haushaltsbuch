package model;

import java.time.LocalDate;

public class Einnahmen {
	
	private int id;
	private Kategorien kategorie;
	private String quelle; 
	private double hoehe;
	private LocalDate dp;
	
	public Einnahmen(int id, Kategorien kategorie, String quelle, double hoehe, LocalDate dp) {
		super();
		this.id = id;
		this.kategorie = kategorie;
		this.quelle = quelle;
		this.hoehe = hoehe;
		this.dp = dp;
	}
	
	
	
	public Einnahmen() {
		quelle = "";
		dp = LocalDate.now();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Kategorien getKategorie() {
		return kategorie;
	}

	public void setKategorie(Kategorien kategorie) {
		this.kategorie = kategorie;
	}

	public String getQuelle() {
		return quelle;
	}

	public void setQuelle(String quelle) {
		this.quelle = quelle;
	}

	public double getHoehe() {
		return hoehe;
	}

	public void setHoehe(double hoehe) {
		this.hoehe = hoehe;
	}

	public LocalDate getDp() {
		return dp;
	}

	public void setDp(LocalDate dp) {
		this.dp = dp;
	}

	
	

	
	
}
