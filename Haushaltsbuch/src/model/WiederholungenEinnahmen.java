package model;

import java.time.LocalDate;

public class WiederholungenEinnahmen {

	
	private int id;
	private Einnahmen einnahmen;
	private Intervall intervall;
	private LocalDate dp;
	public WiederholungenEinnahmen(int id, Einnahmen einnahmen, Intervall intervall, LocalDate dp) {
		super();
		this.id = id;
		this.einnahmen = einnahmen;
		this.intervall = intervall;
		this.dp = dp;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Einnahmen getEinnahmen() {
		return einnahmen;
	}
	public void setEinnahmen(Einnahmen einnahmen) {
		this.einnahmen = einnahmen;
	}
	public Intervall getIntervall() {
		return intervall;
	}
	public void setIntervall(Intervall intervall) {
		this.intervall = intervall;
	}
	public LocalDate getDp() {
		return dp;
	}
	public void setDp(LocalDate dp) {
		this.dp = dp;
	}
	
	
}
