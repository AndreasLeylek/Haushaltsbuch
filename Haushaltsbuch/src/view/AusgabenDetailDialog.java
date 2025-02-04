package view;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import datenbank.Datenbank;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.Ausgaben;
import model.Intervall;
import model.Kategorien;
import model.WiederholungenAusgaben;
import view.AusgabenKategorieDialog;

public class AusgabenDetailDialog extends Dialog<ButtonType> {
	private TextField produkttf;
	private TextField preistf;
	private TextField neueKategorieTF;
	private DatePicker dp;
	private RadioButton rbj;
	private RadioButton rbn;
	private Button kategorieHinzufuegenButton;
	private Button hinzufuegen;
	private Button zurueck;
	private ChoiceBox<Kategorien> cbausgabenKategorien;
	private ChoiceBox<String> cb2;
	private ObservableList<Kategorien> olKategorien = FXCollections.observableArrayList();
	private AusgabenFX ausgabenFX;
	private Button neueKatButton; 
	private TableView<WiederholungenAusgabenFX> atvw;
	private ObservableList<WiederholungenAusgabenFX> olAusgabenWiederholungen = FXCollections.observableArrayList();
	private ChoiceBox <Kategorien> mainKategoriecb;

	public AusgabenDetailDialog(AusgabenFX ausgabenFX, ChoiceBox <Kategorien> mainKategoriecb) {

		
		this.ausgabenFX = ausgabenFX;
		this.mainKategoriecb = mainKategoriecb;


		DialogPane dialogPane = getDialogPane();		// dialog erstellen 
		dialogPane.setContent(createContent());		// Content erstellen 
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); //Buttons mitgeben 


		cbausgabenKategorien.setItems(olKategorien);
		
		Button save = (Button) this.getDialogPane().lookupButton(ButtonType.OK);		//Alerts erstellen
		save.addEventFilter(ActionEvent.ACTION, e -> {

		    if (produkttf.getText().isEmpty()) {
		        new Alert(AlertType.ERROR, "Bitte geben Sie ein Produkt ein.").showAndWait();
		        e.consume();
		        return;
		    }

		    if (cbausgabenKategorien.getSelectionModel().isEmpty()) {
		        new Alert(AlertType.ERROR, "Bitte wählen Sie eine Kategorie aus.").showAndWait();
		        e.consume();
		        return;
		    }

		    String preisText = preistf.getText();
		    if (preisText.isEmpty()) {
		        new Alert(AlertType.ERROR, "Bitte geben Sie einen Preis ein.").showAndWait();
		        e.consume();
		        return;
		    }

		    
		    try {													// Überprüfen, ob der Preis eine Zahl ist
		        double preis = Double.parseDouble(preisText);
		    } catch (NumberFormatException ex) {
		        new Alert(AlertType.ERROR, "Bitte geben Sie einen gültigen Preis ein.").showAndWait();
		        e.consume();
		        return;
		    }
		});

		setResultConverter(buttonType -> {
			if (buttonType == ButtonType.OK) {			//Überprüfen ob Button OK ist und wenn True dann zur Methode "HandleOK" 
				if (!handleOK())
					return null;
			}
			return buttonType;
		});

		readAusgabenKategorien();
	}

	private VBox createContent() {
		produkttf = new TextField(ausgabenFX.getProdukt());			// TextFeld für Produkt  und geben es AusgabenFX mit 
		produkttf.setPromptText("Produkt");
		produkttf.setPrefWidth(130);

		preistf = new TextField(Double.toString(ausgabenFX.getPreis()));			// Hier mit Preis
		preistf.setPromptText("Preis");
		preistf.setPrefWidth(130);

		dp = new DatePicker(ausgabenFX.getDp());
		dp.setPrefWidth(130);

		Label lbl = new Label("Wiederkehrende Transaktion?");			//Label ob es eine Wiederkehrende Transaktion ist
		rbj = new RadioButton("Ja");
		rbn = new RadioButton("Nein");

		ToggleGroup toggleGroup = new ToggleGroup();		//erstellen einer ToggleGroup um sicherzustellen dass nur ein RadioButton angeklickt wird 
		rbj.setToggleGroup(toggleGroup);
		rbn.setToggleGroup(toggleGroup);

		cbausgabenKategorien = new ChoiceBox<>(olKategorien);

		readAusgabenKategorien(); 								// Kategorien in die ChoiceBox laden wenn die ID größer als 0 ist
		if (ausgabenFX.getId() > 0 ) {
			Kategorien selectedKategorie = olKategorien.stream().filter(e -> e.getName().equals 
					(ausgabenFX.getKategorie()) ).toList().get(0);
			cbausgabenKategorien.getSelectionModel().select(selectedKategorie);
		}

		cbausgabenKategorien.setPrefWidth(130);


		cb2 = new ChoiceBox<>(); //Neue ChoiceBox bekommt Items mitgeliefert um den Intervall für die Wiederkehrende Transaktion auszuwählen 
		cb2.getItems().addAll("Wöchentlich", " 2 Wochen ", "Monatlich", " Vierteljährlich", "Halbjährlich", "Jährlich"); 
		cb2.setVisible(false);

		hinzufuegen = new Button("Hinzufügen");	//Neuer Button
		zurueck = new Button("Zurück");	//Neuer Button 

		neueKatButton = new Button ("Neue Kategorie");		//Neuer Button 
		neueKatButton.setOnAction(event-> {
			AusgabenKategorieDialog dialog = new AusgabenKategorieDialog();	//Verweis auf AusgabenKategorieDialog wenn ageklickt 
			Optional<ButtonType> result = dialog.showAndWait();		// wird der Button OK angeklickt öffnet sich das Fenster
			result.ifPresent(buttonType -> {				
				if (buttonType == ButtonType.OK) {
					readAusgabenKategorien(); 
					cbausgabenKategorien.setItems(olKategorien); 

				}
			});
		});

		kategorieHinzufuegenButton = new Button("Kategorie hinzufügen");		// Neuer Button um Kategorien hinzuzfügen
		neueKategorieTF = new TextField();
		neueKategorieTF.setPromptText("Neue Kategorie");		
		neueKategorieTF.setPrefWidth(130);

		kategorieHinzufuegenButton.setOnAction(event -> {				
			String neueKategorie = neueKategorieTF.getText();			// Holt den Text aus dem TextFeld von der Kategorie 
			if (!neueKategorie.isEmpty()) {								// prüft ob die Kategorie nicht leer ist 
				try {
					Datenbank.insertKategorie(new Kategorien(0, neueKategorie, false, true));			// Fügt sie in die insertKategorie ein 
					Datenbank.updateAusgabe(ausgabenFX.getModellAusgaben());		// Update Ausgabe 
					System.out.println("geschafft ausgaben");
					readAusgabenKategorien();				// aktualisiert die AusgabenKategorien
					cbausgabenKategorien.setItems(olKategorien);			// Kategorie wird in die ChoiceBox eingespielt
				} catch (SQLException e) {
					e.printStackTrace(); 
					System.out.println("nicht geschafft ausgaben");
				}

				neueKategorieTF.clear();			// löscht den Text	
				
			}
		});

		rbj.setOnAction(e -> cb2.setVisible(true));			// Wenn ja angeklickt wird öffnet sich die ChoiceBox mit den Intervallen
		rbn.setOnAction(e -> cb2.setVisible(false));		// Hier bleibt sie unsichtbar 

		VBox content = new VBox();
		content.getChildren().addAll(neueKatButton, cbausgabenKategorien, 
				produkttf, preistf, dp, cb2, lbl,rbj, rbn );	//Alle buttons werden der VBox übergeben 	
		content.setSpacing(10);		// Abstand von 10 pixeln pro Element
		content.setPadding(new Insets(10));		// Abstand zwischen Elementen und Rand 

		return content;		// gibt die VBox zurück 
	}

	private void readAusgabenKategorien() {			// Liest die Ausgaben Kategorien 
		olKategorien.clear();		// Entfernt vorhandene Kategorien 
		try {
			ArrayList<Kategorien> alKategorien = Datenbank.leseKategorien(Datenbank.KategorieTyp.AUSGABEN); // Liest die Kategorien vom Typ Ausgaben 
			for(Kategorien eineKategorie : alKategorien) // Fügt jede gelesene Kategorie der ObservableList hinzu
				olKategorien.add(eineKategorie);
			cbausgabenKategorien.setItems(olKategorien); // fügt die gelesenen Kategorien der ol wieder hinzu
			mainKategoriecb.setItems(olKategorien); // Kategorien werden auch in die ChoiceBox von der Startseite hinzugefügt 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean handleOK() {				// Wenn Zeile 102 dann wird diese Methode gespielt

		ausgabenFX.setProdukt(produkttf.getText());		// Produkt wird hier erstellt und in AusgabenFX gesetzt
		ausgabenFX.setPreis(Double.parseDouble(preistf.getText())); // Preis wird in AusgabenFX gesetzt
		ausgabenFX.setDp(dp.getValue()); // Datum wird in AusgabenFX gesetzt 
		ausgabenFX.setKategorie(cbausgabenKategorien.getValue().getName()); // Der name aus der CB wird als Kategorie in AusgabenFX gesetzt 
		ausgabenFX.getModellAusgaben().setKategorie(cbausgabenKategorien.getValue()); // setzt die ausgewählte kategorie als Kategorie in  modellAusgaben 
		try {
			if(ausgabenFX.getId() == 0) {		// Prüft ob die ID 0 ist um zu erkennen ob es eine neue Ausgabe ist 
				Datenbank.insertAusgabe(ausgabenFX.getModellAusgaben()); 	// Wenn 0 wird sie in die insertAusgabe eingespielt 
				if(cb2.getSelectionModel().getSelectedIndex() >= 0) { // wird geprüft ob etwas aus der ChoiceBox gewählt wurde 
					Datenbank.insertWiederholungenAusgaben(new WiederholungenAusgaben(0, ausgabenFX.getModellAusgaben(),  // Wenn ja erstellt es eine neue Wiederholung
							Intervall.values()[cb2.getSelectionModel().getSelectedIndex()],  //holt sich das ausgewählte intervall
							Intervall.values()[cb2.getSelectionModel().getSelectedIndex()].addTo(dp.getValue()))); // setzt das Datum mit dem ausgewählten intervall 
				}
			}else {
				System.out.println("update wird ausgeführt Ausgaben");
				Datenbank.updateAusgabe(ausgabenFX.getModellAusgaben()); // Wenn Zeile 216 nicht zutrifft wird die Ausgabe aktualiseiert und in die updateAusgabe hinzugefügt
			}
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;

		}
	}

}
