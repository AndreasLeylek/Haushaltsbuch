package view;

import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.DatePicker;
import model.Ausgaben;
import model.Einnahmen;
import model.Kategorien;

public class EinnahmenFX  {

	private Einnahmen modellEinnahme;
	private SimpleIntegerProperty id;
	private SimpleStringProperty kategorie;
	private SimpleStringProperty quelle;
	private SimpleDoubleProperty hoehe;
	private SimpleObjectProperty<LocalDate> dp;
	public EinnahmenFX(Einnahmen einnahmen) {
		super();
		this.modellEinnahme = einnahmen;
		id = new SimpleIntegerProperty(modellEinnahme.getId());
		kategorie = new SimpleStringProperty(modellEinnahme.getKategorie() != null ? modellEinnahme.getKategorie().getName() : "");
		quelle = new SimpleStringProperty(modellEinnahme.getQuelle());
		if(modellEinnahme.getHoehe() != 0.0 ) {
			hoehe = new SimpleDoubleProperty(einnahmen.getHoehe());
		} else {
			hoehe = new SimpleDoubleProperty(0.0);
		}
		
		dp = new SimpleObjectProperty<LocalDate>(modellEinnahme.getDp());
	}

	public EinnahmenFX() {
		this(new Einnahmen()); 
	}

	public Einnahmen getModellEinnahmen() {
		return modellEinnahme;
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

	public final SimpleStringProperty quelleProperty() {
		return this.quelle;
	}

	public final String getQuelle() {
		return this.quelleProperty().get();
	}

	public final void setQuelle(final String quelle) {
		this.quelleProperty().set(quelle);
		modellEinnahme.setQuelle(quelle);
	}

	public final SimpleDoubleProperty hoeheProperty() {
		return this.hoehe;
	}

	public final double getHoehe() {
		return this.hoeheProperty().get();
		
	}

	public final void setHoehe(final double hoehe) {
		this.hoeheProperty().set(hoehe);
		modellEinnahme.setHoehe(hoehe);
	}

	public final SimpleObjectProperty<LocalDate> dpProperty() {
		return this.dp;
	}

	public final LocalDate getDp() {
		return this.dpProperty().get();
	}

	public final void setDp(final LocalDate dp) {
		this.dpProperty().set(dp);
		modellEinnahme.setDp(dp);
	}

	public final SimpleStringProperty kategorieProperty() {
		return this.kategorie;
	}
	

	public final String getKategorie() {
		return this.kategorieProperty().get();
	}
	

	public final void setKategorie(final String kategorie) {
		this.kategorieProperty().set(kategorie);
	}
	
	


	


}
