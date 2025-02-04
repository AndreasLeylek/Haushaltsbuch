package view;

import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import model.Einnahmen;
import model.Intervall;
import model.Kategorien;
import model.WiederholungenEinnahmen;

public class WiederholungenEinnahmenFX {

	private WiederholungenEinnahmen modellWiederholungenEinnahmen; 
	private SimpleIntegerProperty id;
	
	private SimpleObjectProperty <Einnahmen> einnahmen;
	private SimpleObjectProperty<Kategorien> kategorie;
	private SimpleStringProperty quelle;
	private SimpleDoubleProperty hoehe;
	private SimpleObjectProperty <Intervall> intervall;
	private SimpleObjectProperty <LocalDate> dp;
	public WiederholungenEinnahmenFX(WiederholungenEinnahmen modellWiederholungenEinnahmen) {
		super();
		this.modellWiederholungenEinnahmen = modellWiederholungenEinnahmen;
		this.id = new SimpleIntegerProperty (modellWiederholungenEinnahmen.getId());
		
		this.einnahmen = new SimpleObjectProperty<Einnahmen>(modellWiederholungenEinnahmen.getEinnahmen());
		this.intervall = new SimpleObjectProperty<>(modellWiederholungenEinnahmen.getIntervall());
		this.hoehe = new SimpleDoubleProperty(modellWiederholungenEinnahmen.getEinnahmen().getHoehe());
		this.kategorie = new SimpleObjectProperty(modellWiederholungenEinnahmen.getEinnahmen().getKategorie());
		this.quelle = new SimpleStringProperty(modellWiederholungenEinnahmen.getEinnahmen().getQuelle());
		this.dp = new SimpleObjectProperty<>(modellWiederholungenEinnahmen.getDp());
	}
	
	public WiederholungenEinnahmenFX() {
		
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
	

	public final SimpleObjectProperty<Einnahmen> einnahmenProperty() {
		return this.einnahmen;
	}
	

	public final Einnahmen getEinnahmen() {
		return this.einnahmenProperty().get();
	}
	

	public final void setEinnahmen(final Einnahmen einnahmen) {
		this.einnahmenProperty().set(einnahmen);
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
	

	public final SimpleStringProperty quelleProperty() {
		return this.quelle;
	}
	

	public final String getQuelle() {
		return this.quelleProperty().get();
	}
	

	public final void setQuelle(final String quelle) {
		this.quelleProperty().set(quelle);
	}
	

	public final SimpleDoubleProperty hoeheProperty() {
		return this.hoehe;
	}
	

	public final double getHoehe() {
		return this.hoeheProperty().get();
	}
	

	public final void setHoehe(final double hoehe) {
		this.hoeheProperty().set(hoehe);
	}
	

	public final SimpleObjectProperty<Intervall> intervallProperty() {
		return this.intervall;
	}
	

	public final Intervall getIntervall() {
		return this.intervallProperty().get();
	}
	

	public final void setIntervall(final Intervall intervall) {
		this.intervallProperty().set(intervall);
	}
	

	public final SimpleObjectProperty<LocalDate> dpProperty() {
		return this.dp;
	}
	

	public final LocalDate getDp() {
		return this.dpProperty().get();
	}
	

	public final void setDp(final LocalDate dp) {
		this.dpProperty().set(dp);
	}
	
	
}
