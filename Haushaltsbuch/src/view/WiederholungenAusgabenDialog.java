package view;

import java.sql.SQLException;
import java.util.ArrayList;

import datenbank.Datenbank;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Ausgaben;
import model.Kategorien;
import model.WiederholungenAusgaben;
import view.AusgabenFX;

public class WiederholungenAusgabenDialog extends Dialog<ButtonType> {

    private ObservableList<Kategorien> olKategorien = FXCollections.observableArrayList();
    private ObservableList<AusgabenFX> olAusgaben = FXCollections.observableArrayList();
    private ObservableList<WiederholungenAusgabenFX> olAusgabenWiederholungen = FXCollections.observableArrayList();
    private TableView<WiederholungenAusgabenFX> atvw;


    public WiederholungenAusgabenDialog() {

        setTitle("Kommende Ausgaben");
        readAusgabenWiederholungen();
        createContent();       
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    }
    
    

    private void createContent() {
        createAusgabenTableVieww();
        HBox hbox = new HBox(atvw);
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10));

        VBox vbox = new VBox(hbox);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));        

        DialogPane dialogPane = getDialogPane();
        dialogPane.setContent(vbox);
        
       
        
    }
    
	private void readAusgabenWiederholungen() {
		olAusgabenWiederholungen.clear();
		try {
			ArrayList <WiederholungenAusgaben> ausgabenWListe = Datenbank.leseWiederholungenAusgaben(); // Einlesen der Datenbankdaten in ausgabenlist
			for (WiederholungenAusgaben wiederholungenAusgaben : ausgabenWListe) {
				WiederholungenAusgabenFX wiederholungenAusgabenFX = new WiederholungenAusgabenFX(wiederholungenAusgaben); // Konvertierung in EinnahmenFX
				olAusgabenWiederholungen.add(wiederholungenAusgabenFX); // Einzelne Hinzufügung von ausgabenFX zur ObservableList
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    private void createAusgabenTableVieww() {
        TableColumn<WiederholungenAusgabenFX, String> nameColumn = new TableColumn<>("Kategorien");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("kategorie"));
        nameColumn.setPrefWidth(150);

        TableColumn<WiederholungenAusgabenFX, String> proColumn = new TableColumn<>("Produkt");
        proColumn.setCellValueFactory(new PropertyValueFactory<>("produkt"));
        proColumn.setPrefWidth(150);

        TableColumn<WiederholungenAusgabenFX, Double> preisColumn = new TableColumn<>("Preis");
        preisColumn.setCellValueFactory(new PropertyValueFactory<>("preis"));
        preisColumn.setPrefWidth(150);

        TableColumn<WiederholungenAusgabenFX, String> datumColumn = new TableColumn<>("Datum");
        datumColumn.setCellValueFactory(new PropertyValueFactory<>("dp"));
        datumColumn.setPrefWidth(150);

        atvw = new TableView<>(olAusgabenWiederholungen);
        atvw.getColumns().addAll(nameColumn, proColumn, preisColumn, datumColumn);
        atvw.refresh();
        
    }

}