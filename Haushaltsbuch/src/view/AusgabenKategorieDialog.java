package view;

import java.sql.SQLException;
import view.AusgabenDetailDialog;
import java.util.ArrayList;

import datenbank.Datenbank;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Kategorien;
import view.AusgabenFX;

public class AusgabenKategorieDialog extends Dialog<ButtonType> {

	private ObservableList<Kategorien> olKategorien = FXCollections.observableArrayList();
	private TextField neueKategorieTF;
	private Button kategorieHinzufuegenButton;
	private VBox vb; 
	private HBox hb;
	private HBox hb1;
	private Button closeButton;
	private ChoiceBox<Kategorien> cbausgabenKategorien;


	public AusgabenKategorieDialog() {

		setTitle("Ausgaben Kategorie hinzufügen");			// Titel des Fensters setzen 

		DialogPane dialogPane = getDialogPane(); 	// ruft den Dialog auf 
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); // füge ButtonTypes hinzu

		Button save = (Button) this.getDialogPane().lookupButton(ButtonType.OK);	// save Button tritt in kraft wenn OK geklickt wird 

		
		neueKategorieTF = new TextField();		// Erstelle neues TextFeld
		neueKategorieTF.setPromptText("Neue Kategorie");	// Name für TextFeld 
		neueKategorieTF.setPrefWidth(130);	//breite

		save.setOnAction(event -> {		// EventHändler für save
			String neueKategorie = neueKategorieTF.getText(); 		// holt den Text der neue Kategorie 
			if (!neueKategorie.isEmpty()) {		// prüft ob textfeld leer ist 
				try {
					Datenbank.insertKategorie(new Kategorien(0, neueKategorie, false, true));	// Wenn nicht fügt es die Kategorie in die Datenbank hinzu (false für einnahme, True für ausgabe 
					System.out.println("geschafft ausgaben");    // Wenn geklappt hat meldung in der Konsole
					readAusgabenKategorien(); 		// aktualisierung durch die read Methode 
				} catch (SQLException e) {
					e.printStackTrace();
					
				}

				neueKategorieTF.clear(); // TextFeld wird gelöscht
				readAusgabenKategorien(); // aktualisierung der Kategorien von Ausgaben 
			} else {
				new Alert(AlertType.ERROR, "Bitte Text eingeben.").showAndWait(); // Falls kein Text eingegeben fehlermeldung
				
			}
		});

		hb = new HBox(neueKategorieTF);		// Neue HBox hinzufügen vom TextFext
		hb.setSpacing(10); // Abstand 
		hb.setPadding(new Insets(20, 0, 0, 0)); // Abstand zum rand
 
		vb = new VBox(); // neue VBOX 
		vb.getChildren().addAll(hb);		// setzte hbox in die vbox 

		getDialogPane().setContent(vb);

		setResultConverter(buttonType -> {			// Wenn ButtonType close geklickt wird dann schließen 
			if (buttonType == ButtonType.CLOSE) {
				return ButtonType.CLOSE;
				
			}
			if (buttonType == ButtonType.OK) { // Wenn OK dann ausführen vom Code 
				return ButtonType.OK;
			}
			return null; // Man kann nur OK oder Close klicken 
		});        
	}

	private void readAusgabenKategorien() { // Methode für das lesen der AusgabenKategorie 
		olKategorien.clear(); // Kategorie liste wird gelöscht 
		try {
			ArrayList<Kategorien> alKategorien = Datenbank.leseKategorien(Datenbank.KategorieTyp.AUSGABEN); //liest die kategorien aus der db und fügt sie der alKat. hinzu 
			for (Kategorien eineKategorie : alKategorien) // Durchläuft jede kategorie in der liste 
				olKategorien.add(eineKategorie); // Fügt die kategorien der ol hinzu 
		} catch (SQLException e) {
			e.printStackTrace();
		}        
	}      

}