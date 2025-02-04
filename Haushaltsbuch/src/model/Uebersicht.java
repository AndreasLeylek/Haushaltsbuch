package model;

import java.time.LocalDate;

public class Uebersicht {
	
	private Kategorien kategorien;
	private double betrag;
	private LocalDate dp;
//	private double differenz;
	public Uebersicht(Kategorien kategorien, double betrag, LocalDate dp) {
		super();
		this.kategorien = kategorien;
		this.betrag = betrag;
		this.dp = dp;
//		this.differenz = differenz;
	}
	
	public Uebersicht(Einnahmen e) {
		kategorien = e.getKategorie();
		betrag = e.getHoehe();
		dp = e.getDp();
	}
	
	public Uebersicht(Ausgaben a) {
		kategorien = a.getKategorie();
		betrag = a.getPreis();
		dp = a.getDp();
	}
	
	public Uebersicht() {
	}

	public Kategorien getKategorien() {
		return kategorien;
	}
	public void setKategorien(Kategorien kategorien) {
		this.kategorien = kategorien;
	}
	public double getBetrag() {
		return betrag;
	}
	public void setBetrag(double betrag) {
		this.betrag = betrag;
	}
	public LocalDate getDp() {
		return dp;
	}
	public void setDp(LocalDate dp) {
		this.dp = dp;
	}
//	public double getDifferenz() {
//		return differenz;
//	}
//	public void setDifferenz(double differenz) {
//		this.differenz = differenz;
//	}
	
	
	

}
