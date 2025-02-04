package view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import datenbank.Datenbank;
import datenbank.Datenbank.KategorieTyp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Kategorien;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class KategorienAusgabenDetailDialog extends Dialog<ButtonType> {

	private ListView<Kategorien> kategorieListView;
	private Button neuButton;
	private Button bearbeitenButton;
	private Button loeschButton;
	private ObservableList<Kategorien> olKategorien = FXCollections.observableArrayList();

	public KategorienAusgabenDetailDialog() {
		setTitle("Kategorie bearbeiten");

		readKategorien(KategorieTyp.AUSGABEN); // KategorieTyp ausgaben wird gelesen 

		DialogPane dialogPane = getDialogPane();
		dialogPane.setContent(createContent());
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		setResultConverter(buttonType -> buttonType);
	}

	private HBox createContent() {
		kategorieListView = new ListView<>(); // ListView wird erstellt
		kategorieListView.setItems(olKategorien); // die olKategorien werden übergeben 
		kategorieListView.setPrefHeight(200);

		neuButton = new Button("Neu");
		neuButton.setOnAction(event -> {
			AusgabenKategorieDialog dialog = new AusgabenKategorieDialog(); // verweis auf AusgabenKategorieDialog wenn Neu geklickt wird 
			Optional<ButtonType> result = dialog.showAndWait();
			result.ifPresent(buttonType -> {
				if (buttonType == ButtonType.OK) { // Wenn OK geklickt Kategorien werden gelesen 
					readKategorien(KategorieTyp.AUSGABEN);
				}
			});
		});

		bearbeitenButton = new Button("Bearbeiten");
		bearbeitenButton.setOnAction(e -> {
		    Kategorien selectedKategorie = kategorieListView.getSelectionModel().getSelectedItem();		
		    if (selectedKategorie == null) {  // Wird geprüft ob das ausgewählte null ist
		        showErrorDialog("Keine Kategorie gewählt"); 	// Wenn null dann wird ein Error angezeigt
		        return; 
		    }

		    TextInputDialog dialog = new TextInputDialog(selectedKategorie.getName()); // Textinput um die Kategorie umzubennenen 
		    dialog.setTitle("Kategorie umbenennen"); 
		    dialog.setHeaderText(null);
		    dialog.setContentText("Geben Sie den neuen Namen für die Kategorie ein:");		

		    Optional<String> result = dialog.showAndWait();
		    result.ifPresent(newName -> {
		        try {
		            // Aktualisieren und in die Datenbank hinzufügen
		            selectedKategorie.setName(newName);
		            Datenbank.updateKategorie(selectedKategorie);
		            readKategorien(KategorieTyp.AUSGABEN);
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		        }
		    });
		});

		loeschButton = new Button("Löschen");
		loeschButton.setOnAction(e -> {
		    Kategorien selectedKategorie = kategorieListView.getSelectionModel().getSelectedItem();  
		    if (selectedKategorie == null) { 	 // prüfung ob eine kategorie angeklickt wurde
		        showErrorDialog("Keine Kategorie gewählt"); // Wenn nicht error
		        return; 
		    }
		    try {
		        Datenbank.deleteKategorieAusgabe(selectedKategorie.getId()); // Wenn ja delete Methode in der Datenbank wird aufgerufen 
		        readKategorien(KategorieTyp.AUSGABEN); // Kategorien werden gelesen 
		    } catch (SQLException ex) {
		        showErrorDialog("Kategorie in Benutzung, bitte im Startbildschirm die Ausgabe löschen"); //Falls es sich um eine Kategorie handelt die eine Wiederholung ist 
		    }
		});


		VBox vb = new VBox();
		vb.getChildren().addAll(neuButton, bearbeitenButton, loeschButton);
		vb.setSpacing(10);
		vb.setPadding(new Insets(10));

		HBox content = new HBox();
		content.getChildren().addAll(kategorieListView, vb);
		content.setSpacing(10);
		content.setPadding(new Insets(10));

		return content;
	}

	private void readKategorien(Datenbank.KategorieTyp kategorieTyp) {		//Kategorien werden gelesen 
		olKategorien.clear();
		try {
			ArrayList<Kategorien> alKategorien = Datenbank.leseKategorien(kategorieTyp); //Von der Datenbank leseKategorien
			olKategorien.addAll(alKategorien); // udn in die ol hinzugefügt 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showErrorDialog(String message) {	//Methode für Error dialog 
		Alert alert = new Alert(Alert.AlertType.ERROR);

		alert.setTitle("Fehler");
		alert.setHeaderText(null);
		alert.setContentText(message);
		Optional<ButtonType> result = alert.showAndWait();
	}
}
