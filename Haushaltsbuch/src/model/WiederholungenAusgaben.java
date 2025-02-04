package model;

import java.time.LocalDate;

public class WiederholungenAusgaben {

	
	private int id;
	private Ausgaben ausgaben;
	private Intervall intervall;
	private LocalDate dp;
	public WiederholungenAusgaben(int id, Ausgaben ausgaben, Intervall intervall, LocalDate dp) {
		super();
		this.id = id;
		this.ausgaben = ausgaben;
		this.intervall = intervall;
		this.dp = dp;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Ausgaben getAusgaben() {
		return ausgaben;
	}
	public void setAusgaben(Ausgaben ausgaben) {
		this.ausgaben = ausgaben;
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
