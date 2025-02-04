package view;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Ausgaben;
import model.Kategorien;
import model.Uebersicht;

import java.time.LocalDate;

public class UebersichtFX {

	private Uebersicht modellUebersicht;
    private SimpleObjectProperty<Kategorien> kategorie;
    private SimpleDoubleProperty betrag;
    private SimpleObjectProperty<LocalDate> datum;


    public UebersichtFX(Uebersicht modellUebersicht) {
        this.modellUebersicht = modellUebersicht;
        this.kategorie = new SimpleObjectProperty<>(modellUebersicht.getKategorien());
        this.betrag = new SimpleDoubleProperty(modellUebersicht.getBetrag());
        this.datum = new SimpleObjectProperty<>(modellUebersicht.getDp());

    }
    	
	public UebersichtFX() {
	    this(new Uebersicht());
	}

	public final SimpleObjectProperty<Kategorien> kategorieProperty() {
		return this.kategorie;
	}
	

	public final Kategorien getKategorie() {
		return this.kategorieProperty().get();
	}
	

	public final void setKategorie(final Kategorien kategorie) {
		this.kategorieProperty().set(kategorie);
	}
	

	public final SimpleDoubleProperty betragProperty() {
		return this.betrag;
	}
	

	public final double getBetrag() {
		return this.betragProperty().get();
	}
	

	public final void setBetrag(final double betrag) {
		this.betragProperty().set(betrag);
	}
	

	public final SimpleObjectProperty<LocalDate> datumProperty() {
		return this.datum;
	}
	

	public final LocalDate getDatum() {
		return this.datumProperty().get();
	}
	

	public final void setDatum(final LocalDate datum) {
		this.datumProperty().set(datum);
	}
	
	
	
	

}