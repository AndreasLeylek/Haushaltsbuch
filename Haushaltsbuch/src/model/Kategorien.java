package model;

import java.sql.SQLException;

import datenbank.Datenbank;

public class Kategorien {
    
    private int id; 
    private String name; 
    private boolean einnahme;
    private boolean ausgabe;
	public Kategorien(int id, String name, boolean einnahme, boolean ausgabe) {
		super();
		this.id = id;
		this.name = name;
		this.einnahme = einnahme;
		this.ausgabe = ausgabe;
	
	}
	
	
	public Datenbank.KategorieTyp getKategorieTyp() {
		return einnahme==true ? Datenbank.KategorieTyp.EINNAHMEN:Datenbank.KategorieTyp.AUSGABEN;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isEinnahme() {
		return einnahme;
	}
	public void setEinnahme(boolean einnahme) {
		this.einnahme = einnahme;
	}
	public boolean isAusgabe() {
		return ausgabe;
	}
	public void setAusgabe(boolean ausgabe) {
		this.ausgabe = ausgabe;
	}
	@Override
	public String toString() {
		return name;
	}

    
 }
