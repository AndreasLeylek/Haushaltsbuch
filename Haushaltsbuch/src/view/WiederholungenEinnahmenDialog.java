package view;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import datenbank.Datenbank;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Kategorien;
import model.WiederholungenAusgaben;
import model.WiederholungenEinnahmen;

public class WiederholungenEinnahmenDialog extends Dialog<ButtonType> {

	private ObservableList<Kategorien> olKategorien = FXCollections.observableArrayList();
	private ObservableList<EinnahmenFX> olEinnahmen = FXCollections.observableArrayList();
	private ObservableList<WiederholungenEinnahmenFX> olEinnahmenWiederholungen = FXCollections.observableArrayList();
	private TableView<WiederholungenEinnahmenFX> etvw;

	public WiederholungenEinnahmenDialog() {
		setTitle("Kommende Einnahmen");
		readEinnahmenWiederholungen();
		createContent();
		getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
	}

	private void createContent() {
		createEinnahmenTableView();
		HBox hbox = new HBox(etvw);
		hbox.setSpacing(10);
		hbox.setPadding(new Insets(10));

		VBox vbox = new VBox(hbox);
		vbox.setSpacing(10);
		vbox.setPadding(new Insets(10));

		DialogPane dialogPane = getDialogPane();
		dialogPane.setContent(vbox);
	}
	
	private void readEinnahmenWiederholungen() {
		olEinnahmenWiederholungen.clear();
		try {
			ArrayList <WiederholungenEinnahmen> einnahmenWListe = Datenbank.leseWiederholungenEinnahmen(); // Einlesen der Datenbankdaten in ausgabenlist
			for (WiederholungenEinnahmen wiederholungenEinnahmen : einnahmenWListe) {
				WiederholungenEinnahmenFX wiederholungenEinnahmenFX = new WiederholungenEinnahmenFX(wiederholungenEinnahmen); // Konvertierung in EinnahmenFX
				olEinnahmenWiederholungen.add(wiederholungenEinnahmenFX); // Einzelne Hinzufügung von ausgabenFX zur ObservableList
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createEinnahmenTableView() {

		TableColumn<WiederholungenEinnahmenFX, Kategorien> nameColumn = new TableColumn<>("Kategorien");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("kategorie"));
		nameColumn.setPrefWidth(150);

		TableColumn<WiederholungenEinnahmenFX, String> quelleColumn = new TableColumn<>("Quelle");
		quelleColumn.setCellValueFactory(new PropertyValueFactory<>("quelle"));
		quelleColumn.setPrefWidth(150);

		TableColumn<WiederholungenEinnahmenFX, Double> hoeColumn = new TableColumn<>("Hoehe");
		hoeColumn.setCellValueFactory(new PropertyValueFactory<>("hoehe"));
		hoeColumn.setPrefWidth(150);

		TableColumn<WiederholungenEinnahmenFX, LocalDate> datumColumn = new TableColumn<>("Datum");
		datumColumn.setCellValueFactory(new PropertyValueFactory<>("dp"));
		datumColumn.setPrefWidth(150);


		etvw = new TableView<>(olEinnahmenWiederholungen);
		etvw.getColumns().addAll(nameColumn,quelleColumn, hoeColumn, datumColumn);
		etvw.refresh();



	}
}
