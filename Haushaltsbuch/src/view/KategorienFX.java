package view;

import java.sql.SQLException;
import java.time.LocalDate;

import datenbank.Datenbank;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import model.Ausgaben;
import model.Kategorien;

public class KategorienFX {

	private Kategorien modellKategorien;
	private SimpleIntegerProperty id;
	private SimpleStringProperty name; 
	private SimpleBooleanProperty einnahme;
	private SimpleBooleanProperty  ausgabe;
	public KategorienFX(Kategorien modellKategorien) {
		super();
		this.modellKategorien = modellKategorien;
		id = new SimpleIntegerProperty(modellKategorien.getId());
		name = new SimpleStringProperty(modellKategorien.getName());
		einnahme = new SimpleBooleanProperty  (modellKategorien.isEinnahme());
		ausgabe = new SimpleBooleanProperty  (modellKategorien.isAusgabe());


	}
	
	public void saveKategorie(LocalDate datum) {
//	    Kategorien kategorie = new Kategorien(kategorie);
	    try {
	        Datenbank.insertKategorie(modellKategorien);
	        System.out.println("geschafft kategorie");
	    } catch (SQLException e) {
	        e.printStackTrace(); 
	        System.out.println("nicht geschafft kategorie");
	    }
	}

	public final SimpleIntegerProperty idProperty() {
		return this.id;
	}
	

	public final int getId() {
		return this.idProperty().get();
	}
	

	public final void setId(final int id) {
		this.idProperty().set(id);
	}
	

	public final SimpleStringProperty nameProperty() {
		return this.name;
	}
	

	public final String getName() {
		return this.nameProperty().get();
	}
	

	public final void setName(final String name) {
		this.nameProperty().set(name);
	}
	

	public final SimpleBooleanProperty einnahmeProperty() {
		return this.einnahme;
	}
	

	public final boolean isEinnahme() {
		return this.einnahmeProperty().get();
	}
	

	public final void setEinnahme(final boolean einnahme) {
		this.einnahmeProperty().set(einnahme);
	}
	

	public final SimpleBooleanProperty ausgabeProperty() {
		return this.ausgabe;
	}
	

	public final boolean isAusgabe() {
		return this.ausgabeProperty().get();
	}
	

	public final void setAusgabe(final boolean ausgabe) {
		this.ausgabeProperty().set(ausgabe);
	}
	

}
