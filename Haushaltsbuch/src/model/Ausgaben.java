package model;

import java.time.LocalDate;

public class Ausgaben {

	private int id;
	private Kategorien kategorie;
	private String produkt;
	private double preis; 
	private LocalDate dp;




	public Ausgaben(int id, Kategorien kategorie, String produkt, double preis, LocalDate dp) {
		super();
		this.id = id;
		this.kategorie = kategorie;
		this.produkt = produkt;
		this.preis = preis;
		this.dp = dp;
	}
	public Ausgaben() {
		produkt = "";
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
	public String getProdukt() {
		return produkt;
	}
	public void setProdukt(String produkt) {
		this.produkt = produkt;
	}
	public double getPreis() {
		return preis;
	}
	public void setPreis(double preis) {
		this.preis = preis;
	}
	public LocalDate getDp() {
		return dp;
	}
	public void setDp(LocalDate dp) {
		this.dp = dp;
	}



}
