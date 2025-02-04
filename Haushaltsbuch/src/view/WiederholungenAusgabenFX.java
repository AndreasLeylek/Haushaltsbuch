package view;

import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import model.Ausgaben;
import model.Intervall;
import model.Kategorien;
import model.WiederholungenAusgaben;


public class WiederholungenAusgabenFX {
    private WiederholungenAusgaben modellWiederholungenAusgaben;
    private SimpleIntegerProperty id;


    private SimpleObjectProperty<Ausgaben> ausgaben;
    private SimpleObjectProperty <Kategorien> kategorie; 
    private SimpleStringProperty produkt; 
    private SimpleDoubleProperty preis;
    private SimpleObjectProperty<Intervall> intervall;
    private SimpleObjectProperty<LocalDate> dp;

    public WiederholungenAusgabenFX(WiederholungenAusgaben modellWiederholungenAusgaben) {
        super();
        this.modellWiederholungenAusgaben = modellWiederholungenAusgaben;
        this.id = new SimpleIntegerProperty(modellWiederholungenAusgaben.getId()); 
        
       
        this.ausgaben = new SimpleObjectProperty<Ausgaben>(modellWiederholungenAusgaben.getAusgaben());
        this.intervall = new SimpleObjectProperty<>(modellWiederholungenAusgaben.getIntervall());
        this.preis = new SimpleDoubleProperty(modellWiederholungenAusgaben.getAusgaben().getPreis());
        this.kategorie = new SimpleObjectProperty(modellWiederholungenAusgaben.getAusgaben().getKategorie()); // Umbenannt von 'produkt' zu 'kategorie'
        this.produkt = new SimpleStringProperty(modellWiederholungenAusgaben.getAusgaben().getProdukt());
        this.dp = new SimpleObjectProperty<>(modellWiederholungenAusgaben.getDp());
    }


	
	public WiederholungenAusgabenFX () {
		
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
	



	public final SimpleObjectProperty<Ausgaben> ausgabenProperty() {
		return this.ausgaben;
	}
	



	public final Ausgaben getAusgaben() {
		return this.ausgabenProperty().get();
	}
	



	public final void setAusgaben(final Ausgaben ausgaben) {
		this.ausgabenProperty().set(ausgaben);
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
	



	public final SimpleStringProperty produktProperty() {
		return this.produkt;
	}
	



	public final String getProdukt() {
		return this.produktProperty().get();
	}
	



	public final void setProdukt(final String produkt) {
		this.produktProperty().set(produkt);
	}
	



	public final SimpleDoubleProperty preisProperty() {
		return this.preis;
	}
	



	public final double getPreis() {
		return this.preisProperty().get();
	}
	



	public final void setPreis(final double preis) {
		this.preisProperty().set(preis);
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


