package view;

import java.sql.SQLException;
import java.util.ArrayList;

import datenbank.Datenbank;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Kategorien;

public class EinnahmenKategorieDialog extends Dialog <ButtonType>{

	private ObservableList<Kategorien> olKategorien = FXCollections.observableArrayList();
	private TextField neueKategorieTF;
	private HBox hb;
	private VBox vb;
	private Button bearbeiten;

	public EinnahmenKategorieDialog() {

		setTitle("Einnahmen Kategorie");

		DialogPane dialogPane = getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		Button save = (Button) this.getDialogPane().lookupButton(ButtonType.OK);
		
	
		
		neueKategorieTF = new TextField();
		neueKategorieTF.setPromptText("Neue Kategorie");
		neueKategorieTF.setPrefWidth(130);
		
		save.setOnAction(event -> {		// EventHändler für save
			String neueKategorie = neueKategorieTF.getText(); 		// holt den Text der neue Kategorie 
			if (!neueKategorie.isEmpty()) {		// prüft ob textfeld leer ist 
				try {
					Datenbank.insertKategorie(new Kategorien(0, neueKategorie, true, false));	// Wenn nicht fügt es die Kategorie in die Datenbank hinzu (false für ausgabe, True für einnahme 
					System.out.println("geschafft ausgaben");    // Wenn geklappt hat meldung in der Konsole
					readEinnahmenKategorien(); 		// aktualisierung durch die read Methode 
				} catch (SQLException e) {
					e.printStackTrace();
					
				}

				neueKategorieTF.clear(); // TextFeld wird gelöscht
				readEinnahmenKategorien(); // aktualisierung der Kategorien von Ausgaben 
			} else {
				new Alert(AlertType.ERROR, "Bitte Text eingeben.").showAndWait(); // Falls kein Text eingegeben fehlermeldung
				
			}
		});
		

		hb = new HBox (neueKategorieTF);
		hb.setSpacing(10);
		hb.setPadding(new Insets (20,0,0,0));
		
		
		vb = new VBox();
		vb.getChildren().addAll(hb);
		
		getDialogPane().setContent(vb);
		
		setResultConverter(buttonType-> {
			if (buttonType == ButtonType.CLOSE) { // Wenn ButtonType close geklickt wird dann schließen
				return ButtonType.CLOSE;
			}
			if (buttonType == ButtonType.OK) { // Wenn OK dann ausführen vom Code
				return ButtonType.OK; 
			}
			return null; // Man kann nur OK oder Close klicken 
		});
		
		readEinnahmenKategorien();
				
	}
	
	private void readEinnahmenKategorien() {  // Methode für das lesen der EinnahmeKategorie 
		  olKategorien.clear();	// Kategorie liste wird gelöscht 
	        try {
	            ArrayList<Kategorien> elKategorien = Datenbank.leseKategorien(Datenbank.KategorieTyp.EINNAHMEN);//liest die kategorien aus der db und fügt sie der alKat. hinzu 
	            for (Kategorien eineKategorie : elKategorien) // Durchläuft jede kategorie in der liste 
	                olKategorien.add(eineKategorie);// Fügt die kategorien der ol hinzu 
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		
	}
}