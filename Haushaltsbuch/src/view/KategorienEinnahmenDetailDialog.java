package view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import datenbank.Datenbank;
import datenbank.Datenbank.KategorieTyp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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

public class KategorienEinnahmenDetailDialog extends Dialog<ButtonType> {

    private ListView<Kategorien> kategorieListView;
    private Button neuButton;
    private Button bearbeitenButton;
    private Button loeschButton;
    private ObservableList<Kategorien> olKategorien = FXCollections.observableArrayList();

    public KategorienEinnahmenDetailDialog() {
        setTitle("Kategorie bearbeiten");

        readKategorien(KategorieTyp.EINNAHMEN);

        DialogPane dialogPane = getDialogPane();
        dialogPane.setContent(createContent());
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        setResultConverter(buttonType -> buttonType);
    }

    private HBox createContent() {
        kategorieListView = new ListView<>();
        kategorieListView.setItems(olKategorien);
        kategorieListView.setPrefHeight(200);

        neuButton = new Button("Neu");
        neuButton.setOnAction(event -> {
            EinnahmenKategorieDialog dialog = new EinnahmenKategorieDialog();
            Optional<ButtonType> result = dialog.showAndWait();
            result.ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    readKategorien(KategorieTyp.EINNAHMEN);
                    
                    
                }
            });
        });
        
        bearbeitenButton = new Button("Bearbeiten");
        bearbeitenButton.setOnAction(e -> {
        	
            Kategorien selectedKategorie = kategorieListView.getSelectionModel().getSelectedItem();
            
            if (selectedKategorie == null) {
    		        showErrorDialog("Keine Kategorie gewählt");
    		        return; 
    		    }
                //TextInput öffnen um die Kategorie umzubenennen 
                TextInputDialog dialog = new TextInputDialog(selectedKategorie.getName());
                dialog.setTitle("Kategorie umbenennen");
                dialog.setHeaderText(null);
                dialog.setContentText("Geben Sie den neuen Namen für die Kategorie ein:");

                
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(newName -> {
                    try {
                        // Aktualisieren und in die Datenbank hinzufügen
                        selectedKategorie.setName(newName);
                        Datenbank.updateKategorie(selectedKategorie);
                        readKategorien(KategorieTyp.EINNAHMEN);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });
        });
    
    
       


        loeschButton = new Button("Löschen");			
        loeschButton.setOnAction(e -> {
        	Kategorien selectedKategorie = kategorieListView.getSelectionModel().getSelectedItem();
		    if (selectedKategorie == null) {
		        showErrorDialog("Keine Kategorie gewählt");
		        return; 
		    }
		    try {
		        Datenbank.deleteKategorieEinnahmen(selectedKategorie.getId());
		        readKategorien(KategorieTyp.EINNAHMEN);
		    } catch (SQLException ex) {
		        showErrorDialog("Kategorie in Benutzung, bitte im Startbildschirm die Ausgabe löschen");
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


    

	private void readKategorien(Datenbank.KategorieTyp kategorieTyp) {		// lesen der Kategorie
        olKategorien.clear();
        try {
            ArrayList<Kategorien> alKategorien = Datenbank.leseKategorien(kategorieTyp);
            olKategorien.addAll(alKategorien); // udn in die ol hinzugefügt 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
