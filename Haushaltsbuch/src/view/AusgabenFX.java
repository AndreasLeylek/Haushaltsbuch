package view;

import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import model.Ausgaben;
import model.Kategorien;



public class AusgabenFX {

	private Ausgaben modellAusgaben;
	private SimpleIntegerProperty id;
	private SimpleStringProperty kategorie;
	private SimpleStringProperty produkt;
	private SimpleDoubleProperty preis;
	private SimpleObjectProperty<LocalDate> dp;
	public AusgabenFX(Ausgaben ausgaben) {
		super();
		this.modellAusgaben = ausgaben;
		System.out.println("Ist initialisiert");
		id = new SimpleIntegerProperty(modellAusgaben.getId());
		kategorie = new SimpleStringProperty(modellAusgaben.getKategorie() != null ? modellAusgaben.getKategorie().getName() : "");
		produkt = new SimpleStringProperty(modellAusgaben.getProdukt());
		if (modellAusgaben.getPreis() != 0.0) {
			preis = new SimpleDoubleProperty(modellAusgaben.getPreis());
		} else {
			preis = new SimpleDoubleProperty(0.0); //Standardwert 0.0 
		}

		//		preis = new SimpleDoubleProperty(modellAusgaben.getPreis());
		dp = new SimpleObjectProperty<LocalDate>(modellAusgaben.getDp());
	}

	public AusgabenFX() {
		this(new Ausgaben()); 
	}

	public Ausgaben getModellAusgaben() {
		return modellAusgaben;
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

	public final SimpleStringProperty produktProperty() {
		return this.produkt;
	}

	public final String getProdukt() {
		return this.produktProperty().get();
	}

	public final void setProdukt(final String produkt) {
		this.produktProperty().set(produkt);
		modellAusgaben.setProdukt(produkt);
	}


	public final SimpleDoubleProperty preisProperty() {
		return this.preis;
	}

	public final double getPreis() {
		return this.preisProperty().get();
	}

	public final void setPreis(final double preis) {
		this.preisProperty().set(preis);
		modellAusgaben.setPreis(preis);
	}

	public final SimpleObjectProperty<LocalDate> dpProperty() {
		return this.dp;
	}

	public final LocalDate getDp() {
		return this.dpProperty().get();
	}

	public final void setDp(final LocalDate dp) {
		this.dpProperty().set(dp);
		modellAusgaben.setDp(dp);
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


