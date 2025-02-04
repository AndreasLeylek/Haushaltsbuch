package view;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import datenbank.Datenbank;
import datenbank.Datenbank.KategorieTyp;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Ausgaben;
import model.Einnahmen;
import model.Kategorien;
import model.Uebersicht;
import model.WiederholungenAusgaben;
import wiederholung.TaskAusgaben;
import wiederholung.TaskEinnahmen;

public class MainClass extends Application {


	private AusgabenDetailDialog ausgabe;
	private DatePicker dp;
	private DatePicker dp1;
	private TableView<UebersichtFX> tv;
	private TableView<AusgabenFX> atv;
	private TableView<EinnahmenFX>etv;
	private ObservableList<UebersichtFX> olEuA = FXCollections.observableArrayList();
	private ObservableList<EinnahmenFX> olEinnahmen = FXCollections.observableArrayList();
	private ObservableList<AusgabenFX> olAusgaben = FXCollections.observableArrayList();
	private ObservableList<String> categoryListA = FXCollections.observableArrayList();
	private ObservableList<String> categoryListE = FXCollections.observableArrayList();
	private ObservableList<Kategorien> olKategorien = FXCollections.observableArrayList();
	private ChoiceBox<String> choiceBoxfirst;
	private ChoiceBox<Kategorien> cb2nd;
	private ChoiceBox<String> cbausgaben;
	private ChoiceBox<String> cbeinnahmen;
	private Button speichern; 
	private Button okButton;
	private Button kommende; 
	private Button kategorien; 
	private LocalDate startdatum;
	private LocalDate enddatum;
	private Kategorien kategorie;
	private Label lblA = new Label();
	private Label lblE = new Label();
	private Label lblEuA = new Label(); 
 
	
	public static void main(String[] args) {

		try {			// Kategorien werden erstellt beim start des Programms 
			Datenbank.createKategorie();
			Datenbank.createAusgabe();
			Datenbank.createEinnahmen();
			Datenbank.createWiederholungAusgaben();
			Datenbank.createWiederholungEinnahmen();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		launch(args);

	}

	public void start(Stage stage) throws Exception {
		stage.setTitle("Haushaltsbuch");
		stage.setHeight(500);
		stage.setWidth(750);

		TaskAusgaben.initDailyTask(()->{ // tägliche Aufgabe,  um Daten zu lesen und zu verarbeiten.
			readAusgaben();
			readAlle();
		});
		
		TaskEinnahmen.initDailyTask(()->{ // selbes hier mit Einnahmen 
			readEinnahmen();
			readAlle();
		});
		
		Datenbank.leseWiederholungenAusgaben();		//Ausgaben wiederholungen und Einnahmen werden gelesen 
		Datenbank.leseWiederholungenEinnahmen();
		choiceBoxfirst = new ChoiceBox<>(); 
		choiceBoxfirst.getItems().addAll("Übersicht", "Einnahmen", "Ausgaben");


		readEinnahmen();		// einnahmen, ausgaben und alle werden geladen 
		createEinnahmenTableView(); // Tableviews werden erstellt 
		readAusgaben();
		createAusgabenTableView();
		readAlle();
		createUebersichtTableView();


		cbausgaben = new ChoiceBox<>();		// Choiceboxen für einnahmen und ausgaben 
		cbeinnahmen = new ChoiceBox<>();

		cb2nd = new ChoiceBox<>();		//Choicebox für kategorien 
		cb2nd.setPrefWidth(100);
		cb2nd.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {		//Listener wird hinzu	
			kategorie = newValue;
			readAlle();
			if (newValue == null) {	//wenn keine kategorie gewählt ist
				readEinnahmen(); // liest einnahmen 
				readAusgaben();// ausgaben 
				return;
			}
			if(newValue.getKategorieTyp() == Datenbank.KategorieTyp.EINNAHMEN) { // filtern nach der Kategorie Einnahmen
				readEinnahmen();
			}else {
				readAusgaben(); // hier nach ausgaben
			}
		});

		dp = new DatePicker();
		dp.setPromptText("Startdatum"); // hier ist startdatum
		dp.valueProperty().addListener((observable, oldValue, newValue) -> { // Listener der reagiert auf die aktionen des dp 
			startdatum = newValue; // startdatum wird auf neuen Wert gesetzt 
			readAlle();	// Liest alle Einträge basierend auf dem neuen Startdatum
			readAusgaben(); // alle Ausgaben 
			readEinnahmen(); // Einnahmen 
		});

		dp1 = new DatePicker();
		dp1.setPromptText("Enddatum"); // hier ist enddatum 
		dp1.valueProperty().addListener((observable, oldValue, newValue) -> {  // nach enddatum sortieren 
			enddatum = newValue;
			readAlle();
			readAusgaben();
			readEinnahmen();
		});
		
		Button zuruecksetzen = new Button("<-"); // Button um DP und cb2nd auf null zu stellen 
		zuruecksetzen.setOnAction(e -> {
			dp.setValue(null);
			dp1.setValue(null);
			cb2nd.setValue(null);
		});

		HBox hbc = new HBox ();
		hbc.getChildren().addAll(choiceBoxfirst,cb2nd, dp, dp1, zuruecksetzen); // alle elemente in die hbc stellen
		hbc.setSpacing(10);
		hbc.setMaxHeight(50);
		HBox hb = new HBox();
		hb.setSpacing(10);
		HBox hbl = new HBox();
		hbl.setPadding(new Insets(0,20,0,0));

		VBox vb1 = new VBox();
		VBox vb = new VBox ();

		cb2nd.setVisible(false); // unsichtbar 
		cb2nd.setPrefWidth(0); // auf 0 stellen damit die DP und zurücksetzen button mehr nach links rutschen für ein schöneres Layout 

		Button statistik = new Button ("Statistik"); 
		statistik.setOnAction(e -> handleStatistikButton()); // Implementierung von statistik Button in der handleStatistik Methode

		Button neuButton = new Button ("Neu");
		neuButton.setVisible(false);
		neuButton.setOnAction(e -> handleNeuHinzufuegenButton()); //Implementierung von neu Button in der handleneu Methode

		Button kategorien = new Button ("Kategorien");
		kategorien.setOnAction(e -> {
			String selectedCategory = (String) choiceBoxfirst.getValue();

			if ("Einnahmen".equals(selectedCategory)) {		       					// Wenn Einnahmen gewählt ist und Kategorien gewählt wird verweis auf Kat.Ein.Det.Dialog fenster
				KategorienEinnahmenDetailDialog dialog = new KategorienEinnahmenDetailDialog();
				Optional <ButtonType> r = dialog.showAndWait();
				if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {					
					readKategorien(KategorieTyp.EINNAHMEN);
					readEinnahmen();
				}
			}else if ("Ausgaben".equals(selectedCategory)) {
				KategorienAusgabenDetailDialog dialog = new KategorienAusgabenDetailDialog();	// Selbes mit ausgaben 
				Optional <ButtonType> r = dialog.showAndWait();
				if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
					readKategorien(KategorieTyp.AUSGABEN);
					readAusgaben();
				}

			}
			
		});

		System.out.println("ich komme hier her");

		

		Button bearButton = new Button ("Bearbeiten");		// Bearbeiten methode in der HandleBear. 			
		bearButton.setOnAction(e->  handleBearbeiten());

		Button loeschButton = new Button ("Löschen");  	// selbes mit löschen
		loeschButton.setOnAction(event -> handleloesch());


		Button kommende = new Button ("Kommende"); // und mit Kommend
		kommende.setOnAction(e -> handleWiederholungen());


		Button beendenButton = new Button("Beenden"); // schließt das fenster 
		beendenButton.setOnAction(e -> stage.close());


		vb.getChildren().addAll(neuButton,bearButton, loeschButton,kommende,statistik, kategorien, beendenButton); // Buttons in eine VBox hinzufügen 
		vb.setSpacing(10);

		vb1.getChildren().addAll(hbc,hb, hbl); 
		vb1.setSpacing(10);

		choiceBoxfirst.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { //Layout anpassen bei Ausgaben
			if ("Ausgaben".equals(newValue)) {  // wenn ausgaben gewählt ist dann: 
				neuButton.setVisible(true); // anzeigen von allen Buttons die auf true gesetzt sind 
				cb2nd.setVisible(true);
				cb2nd.setPrefWidth(100);
				hb.getChildren().set(0, atv);				// holt sich die hb mit der Tableview 
				hbl.getChildren().clear();				
				hbl.getChildren().add(lblA);		// Hbox für Label 
				statistik.setVisible(true);
				bearButton.setVisible(true);
				kommende.setVisible(true);
				loeschButton.setVisible(true);
				beendenButton.setVisible(true);
				kategorien.setVisible(true);
				readKategorien(Datenbank.KategorieTyp.AUSGABEN); // holt sich die Kategorien vom Typ Ausgaben
				readAusgaben(); // holt sich die Ausgaben 

			} else if ("Einnahmen".equals(newValue)) {		//Einnahmen
				neuButton.setVisible(true);
				cb2nd.setVisible(true);
				cb2nd.setPrefWidth(100);
				hb.getChildren().set(0, etv);
				hbl.getChildren().clear();				
				hbl.getChildren().add(lblE);
				statistik.setVisible(true);
				bearButton.setVisible(true);
				kommende.setVisible(true);
				loeschButton.setVisible(true);
				beendenButton.setVisible(true);
				kategorien.setVisible(true);
				readKategorien(Datenbank.KategorieTyp.EINNAHMEN);
				readEinnahmen();

			} else if ("Übersicht".equals(newValue)) {		//Übersicht
				neuButton.setVisible(false);		// hier werden keine Buttons angezeigt, dient nur als übersicht über alle Transaktionen 
				cb2nd.setVisible(false);
				hb.getChildren().set(0, tv);
				hbl.getChildren().clear();
				hbl.getChildren().add(lblEuA);
				statistik.setVisible(false);
				bearButton.setVisible(false);
				kommende.setVisible(false);
				loeschButton.setVisible(false);
				beendenButton.setVisible(false);
				kategorien.setVisible(false);
				readAlle();
			} 
		});

		hb.getChildren().addAll(tv, vb); 

		choiceBoxfirst.getSelectionModel().select("Übersicht"); // Start der Anwendung immer im Übersicht fenster 


		Scene originalScene = new Scene(vb1);

		stage.setScene(originalScene);
		stage.show();
	}

	private void createUebersichtTableView() {		// Erstellt TableView von Übersicht


		TableColumn<UebersichtFX, Kategorien> bezeichnungColumn = new TableColumn<>("Kategorie");
		bezeichnungColumn.setCellValueFactory(new PropertyValueFactory<>("kategorie"));
		bezeichnungColumn.setCellFactory(tc -> new TableCell<>() {
			@Override
			public void updateItem(Kategorien item, boolean empty) {
				super.updateItem(item, empty);
				if(item != null && !empty) {
					this.setText(item.getName());		
					if(item.isAusgabe())				// Items von ausgaben werden rot angezeigt und einnahmen grün 
						this.setTextFill(Color.RED);
					else 
						this.setTextFill(Color.GREEN);
				}
				else {
					this.setText(null);
					this.setGraphic(null);
				}
			}
		});
		bezeichnungColumn.setPrefWidth(230);

		TableColumn<UebersichtFX, Double> betragColumn = new TableColumn<>("Betrag");
		betragColumn.setCellValueFactory(new PropertyValueFactory<>("betrag"));
		betragColumn.setPrefWidth(230);

		TableColumn<UebersichtFX, LocalDate> datumColumn = new TableColumn<>("Date");
		datumColumn.setCellValueFactory(new PropertyValueFactory<>("datum"));
		datumColumn.setPrefWidth(230);

		tv = new TableView<>(olEuA);
		tv.getColumns().addAll(bezeichnungColumn, betragColumn, datumColumn);
		tv.refresh();

	}

	private void createAusgabenTableView() {				// Ausgaben TV wird erstellt 

		TableColumn<AusgabenFX, String> nameColumn = new TableColumn<>("Kategorien");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("kategorie"));
		nameColumn.setPrefWidth(150);

		TableColumn<AusgabenFX, String> proColumn = new TableColumn<>("Produkt");
		proColumn.setCellValueFactory(new PropertyValueFactory<>("produkt"));
		proColumn.setPrefWidth(150);

		TableColumn<AusgabenFX, Double> preisColumn = new TableColumn<>("Preis");
		preisColumn.setCellValueFactory(new PropertyValueFactory<>("preis"));
		preisColumn.setPrefWidth(150);

		TableColumn<AusgabenFX, String> datumColumn = new TableColumn<>("Datum");
		datumColumn.setCellValueFactory(new PropertyValueFactory<>("dp"));
		datumColumn.setPrefWidth(150);

		atv = new TableView<>(olAusgaben);
		atv.getColumns().addAll(nameColumn, proColumn,preisColumn, datumColumn);
		atv.refresh();

	}

	private void createEinnahmenTableView() {		// Einnahmen TV wird erstellt 

		TableColumn<EinnahmenFX, Kategorien> nameColumn = new TableColumn<>("Kategorien");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("kategorie"));
		nameColumn.setPrefWidth(150);

		TableColumn<EinnahmenFX, String> quelleColumn = new TableColumn<>("Quelle");
		quelleColumn.setCellValueFactory(new PropertyValueFactory<>("quelle"));
		quelleColumn.setPrefWidth(150);

		TableColumn<EinnahmenFX, Double> hoeColumn = new TableColumn<>("Hoehe");
		hoeColumn.setCellValueFactory(new PropertyValueFactory<>("hoehe"));
		hoeColumn.setPrefWidth(150);

		TableColumn<EinnahmenFX, LocalDate> datumColumn = new TableColumn<>("Datum");
		datumColumn.setCellValueFactory(new PropertyValueFactory<>("dp"));
		datumColumn.setPrefWidth(150);


		etv = new TableView<>(olEinnahmen);
		etv.getColumns().addAll(nameColumn,quelleColumn, hoeColumn, datumColumn);
		etv.refresh();

	}

	private void readEinnahmen() { // Einnahmen lesen
		olEinnahmen.clear();
		try {
			ArrayList<Einnahmen> einnahmenListe = Datenbank.leseEinnahmen(startdatum, enddatum, kategorie); // Einlesen der Datenbank daten in einnahmenliste
			for (Einnahmen einnahme : einnahmenListe) {
				EinnahmenFX einnahmeFX = new EinnahmenFX(einnahme); // Konvertierung in EinnahmenFX
				olEinnahmen.add(einnahmeFX); // Einzelne Hinzufügung von einnahmeFX zur ObservableList
			}
			double ein = olEinnahmen.stream().mapToDouble(EinnahmenFX::getHoehe).sum();  // holt die Höhe der ol 
			String text = String.format("Einnahmen : %.2f", ein);		//Label auf 2 dezimalstellen und text auf Einnahme stellen
			lblE.setText(text);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void readAusgaben() { // Ausgaben lesen
		olAusgaben.clear();
		try {
			ArrayList<Ausgaben> ausgabenListe = Datenbank.leseAusgaben(startdatum, enddatum, kategorie); // Einlesen der Datenbank daten in ausgabenlist
			for (Ausgaben ausgaben : ausgabenListe) {
				AusgabenFX ausgabenFX = new AusgabenFX(ausgaben); // Konvertierung in AusgabenFX
				olAusgaben.add(ausgabenFX); // Einzelne Hinzufügung von ausgabenFX zur ObservableList
			}
			double aus = olAusgaben.stream().mapToDouble(AusgabenFX::getPreis).sum(); // holt die Höhe der ol 
			String text = String.format("Ausgaben: %.2f", aus); //Label auf 2 dezimalstellen und text auf Einnahme stellen 
			lblA.setText(text);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	private void readAlle() { // Alle Einträge lesen 
		olEuA.clear(); 

		try { // Einnahmen und Ausgaben in die olEuA hinzufügen

			ArrayList<Einnahmen> einnahmenListe = Datenbank.leseEinnahmen(startdatum, enddatum, 
					kategorie != null && kategorie.getKategorieTyp() == 	// checkt ob eine Kategorie ausgewählt wurde und welcher Typ es ist(wenn keine Kategorie ausgewählt ist nehmen wir null) Einnahme = kategorie Ausgabe = null 
					Datenbank.KategorieTyp.EINNAHMEN ? kategorie : null); // holen uns die ausgewählte Kategorie von der cb2nd und schauen ob es den richtigen Typ hat
			for (Einnahmen einnahme : einnahmenListe) {
				olEuA.add(new UebersichtFX(new Uebersicht(einnahme)));
			}

			ArrayList<Ausgaben> ausgabenListe = Datenbank.leseAusgaben(startdatum, enddatum, 
					kategorie != null && kategorie.getKategorieTyp() == 	// checkt ob eine Kategorie ausgewählt wurde und welcher Typ es ist(wenn keine Kategorie ausgewählt ist nehmen wir null) Ausgabe = kategorie, Einnahme = null 
					Datenbank.KategorieTyp.AUSGABEN ? kategorie : null); // holen uns die ausgewählte Kategorie von der cb2nd und schauen ob es den richtigen Typ hat));
			for (Ausgaben ausgabe : ausgabenListe) {
				olEuA.add(new UebersichtFX(new Uebersicht(ausgabe)));
			}
			double ein = olEuA.stream().filter(e -> e.getKategorie().isEinnahme()).mapToDouble(UebersichtFX::getBetrag).sum(); // holt die Höhe der ol einnahmen 
			double aus = olEuA.stream().filter(e -> e.getKategorie().isAusgabe()).mapToDouble(UebersichtFX::getBetrag).sum(); // holt den betrag der ol  ausgaben 
			double differenz = ein - aus; // Subtrahiert die einnahmen von den Ausgaben 
			String text = String.format("Übersicht: %.2f", differenz); // zeigt die differenz 
			lblEuA.setText(text);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void readKategorien(Datenbank.KategorieTyp kategorieTyp) { // Kategorien werden gelesen 
		olKategorien.clear();
		try {
			ArrayList<Kategorien> alKategorien = Datenbank.leseKategorien(kategorieTyp); // aus der DB 
			for(Kategorien eineKategorie : alKategorien)
				olKategorien.add(eineKategorie);
			cb2nd.setItems(olKategorien);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void handleNeuHinzufuegenButton() {				// Neue Ein oder Ausgabe hinzufügen 
		String selectedCategory = (String) choiceBoxfirst.getValue();		//Holt den inhalt der cb 

		if ("Einnahmen".equals(selectedCategory)) {		// Wenn Einnahme 
			EinnahmeDetailDialog dialog = new EinnahmeDetailDialog(new EinnahmenFX(), cb2nd); // verweis zu der Klasse 
			Optional<ButtonType> r = dialog.showAndWait();
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) { // Mit ok wird ausgeführt
				readEinnahmen();				 // Einnahmen werden gelesen 
			}
		} else if ("Ausgaben".equals(selectedCategory)) { 	// Wenn Einnahme 
			AusgabenDetailDialog dialog = new AusgabenDetailDialog(new AusgabenFX(), cb2nd);	// verweis zu der Klasse 
			Optional<ButtonType> r = dialog.showAndWait();
			if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)// ausführung
				readAusgaben();		// lesen der Ausgaben 
		}
	}

	private void handleStatistikButton() { //Statistiken 
		String selectedCategory = (String) choiceBoxfirst.getValue();

		if ("Einnahmen".equals(selectedCategory)) {
			VBox statistik = StatistikEinnahmen.erstelleEinnahmenStatistik();		// verweis zur klasse Statistik Einnahme 

			// Erstellt ein Dialogfeld und setzt den Inhalt
			Dialog<ButtonType> dialog = new Dialog<>();		//Dialog wird erstellt 
			dialog.getDialogPane().setContent(statistik);
			dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK); // mit ok ausgeführt
			dialog.showAndWait(); 
		}

		if ("Ausgaben".equals(selectedCategory)) {
			VBox statistik = StatistikAusgaben.erstelleAusgabenStatistik();

			// Erstellt ein Dialogfeld und setzt den Inhalt
			Dialog<ButtonType> dialog = new Dialog<>();
			dialog.getDialogPane().setContent(statistik);
			dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
			dialog.showAndWait();
		}
	}
	
	private void handleloesch() {    
	    String selectedCategory = (String) choiceBoxfirst.getValue();		// Löschen 

	    if ("Einnahmen".equals(selectedCategory)) {		// wenn Einnahmen gewählt 
	        EinnahmenFX selectedData1 = etv.getSelectionModel().getSelectedItem();		
	        if (selectedData1 == null) {		// prüft ob das angeklickte null ist 
	            showErrorAlert("Keine Einnahme ausgewählt", "Bitte wählen Sie eine Einnahme zum Löschen aus.");	// wenn ja dann Error 
	            return;
	        }
	        try {
	        	Datenbank.deleteEinnahmenWiederholungenbyEinnahmenID(selectedData1.getId());	// Wenn nein dann löschen mit der ID von Wiederholung 
	            Datenbank.deleteEinnahmen(selectedData1.getId());                // und Einnahmen 
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        olEinnahmen.remove(selectedData1);
	    }

	    if ("Ausgaben".equals(selectedCategory)) {
	        AusgabenFX selectedData = atv.getSelectionModel().getSelectedItem();	// prüft ob ein item ausgewählt wurde
	        if (selectedData == null) { // wenn null ist 
	            showErrorAlert("Keine Ausgabe ausgewählt", "Bitte wählen Sie eine Ausgabe zum Löschen aus."); // Kommt ein Error 
	            return;
	        }
	        try {
	            Datenbank.deleteAusgabenWiederholungenbyAusgabenID(selectedData.getId()); // wenn nicht löschen aus Wiederholungen 
	            Datenbank.deleteAusgabe(selectedData.getId());                // und aus Ausgaben
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        olAusgaben.remove(selectedData);    // remove aus ol    
	    }

	}
	
	private void handleBearbeiten() {
		 String selectedCategory = choiceBoxfirst.getValue();		//Bearbiten
		 
		 if (selectedCategory.equals("Einnahmen")) {
		        EinnahmenFX einnahmen = etv.getSelectionModel().getSelectedItem();
		        if (einnahmen == null) {
		            
		            showErrorAlert("Keine Einnahme ausgewählt", "Bitte wählen Sie eine Einnahme zum Bearbeiten aus."); // Error wenn kein Item ausgewählt wurde 
		            return;
		        }
		        
		        Optional<ButtonType> b1 = new EinnahmeDetailDialog(einnahmen, cb2nd).showAndWait();// Mit der Bearbeitung des Items fortfahren
		        if (b1.isPresent() && b1.get().getButtonData() == ButtonData.OK_DONE) {
		            readEinnahmen();
		        }
		    } else if (selectedCategory.equals("Ausgaben")) {
		        AusgabenFX ausgaben = atv.getSelectionModel().getSelectedItem();
		        if (ausgaben == null) {
		            
		            showErrorAlert("Keine Ausgabe ausgewählt", "Bitte wählen Sie eine Ausgabe zum Bearbeiten aus."); // Error wenn kein Item ausgewählt wurde 
		            return;
		        }
		        
		        Optional<ButtonType> b = new AusgabenDetailDialog(ausgaben, cb2nd).showAndWait();// Mit der Bearbeitung des Items fortfahren
		        if (b.isPresent() && b.get().getButtonData() == ButtonData.OK_DONE) {
		            readAusgaben();
		        }
		    }		
	}

	private void showErrorAlert(String title, String message) {		//Alert methode 
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}

	private void handleWiederholungen() {		//Wiederholungs Fenster "Kommende" 
		String selectedCategory = (String) choiceBoxfirst.getValue();
		if ("Einnahmen".equals(selectedCategory)) {		//wenn einnahmen aus der CB dann verweis zur
			WiederholungenEinnahmenDialog dialog = new WiederholungenEinnahmenDialog(); // Klasse WiederholungenEInnahmenDialog
			Optional<ButtonType> r = dialog.showAndWait();		
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {		// Mit klick auf den Button wird die Methode ausgeführt
				readEinnahmen();				//Einnahmen werden gelesen 
			}
		} else if ("Ausgaben".equals(selectedCategory)) {	//wenn ausgaben aus der CB dann verweis zur
			WiederholungenAusgabenDialog dialog = new WiederholungenAusgabenDialog(); // // Klasse WiederholungenAusgabenDialog
			Optional<ButtonType> r = dialog.showAndWait();
			if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)// Mit klick auf den Button wird die Methode ausgeführt
				readAusgaben();		// Ausgaben werden gelesen 
		}
	}

	public void updateTableView(AusgabenFX newData) {		//TableView update von ausgaben 
		System.out.println("wird aufgerufen atv");
		atv.setItems(olAusgaben);		// setzt die aktuellste ol zur TV 
		atv.getSelectionModel().select(newData);  //new Data wird hervorgehoben 
		atv.refresh(); //TV wird aktualisiert 
	}

	public void updateTableView(UebersichtFX newData1) { //TableView update von übersicht 
		System.out.println("wird aufgerufen tv");
		tv.setItems(olEuA);		// setzt die aktuelle ol EuA zur TV 
		tv.getSelectionModel().select(newData1); // NewData1 wird optisch angeklickt 
		tv.refresh(); // Refresh 
	}

	public void updateTableView(EinnahmenFX newData2) { // selbes hier 
		System.out.println("wird aufgerufen etv");
		etv.setItems(olEinnahmen);
		etv.getSelectionModel().select(newData2);
		etv.refresh();
	}
}