package view;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import datenbank.Datenbank;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.*;
import model.Intervall;
import model.Kategorien;
import model.WiederholungenAusgaben;
import model.WiederholungenEinnahmen;

public class EinnahmeDetailDialog extends Dialog<ButtonType> {


	private TextField tfq;
	private TextField tfh;
	private TextField neueKategorieTF;
	private DatePicker dp;
	private RadioButton rbj;
	private RadioButton rbn;
	private Button kategorieHinzufuegenButton;
	private Button hinzufuegen;
	private Button zurueck;
	private Button neueKatButton;
	private ChoiceBox<Kategorien> cbeinnahmenKategorien;
	private ChoiceBox<String> cb2;	
	private ObservableList<Kategorien> olKategorien = FXCollections.observableArrayList();
	private EinnahmenFX einnahmenFX;
	private ChoiceBox <Kategorien> mainKategoriecb;

	public EinnahmeDetailDialog(EinnahmenFX einnahmeFX, ChoiceBox <Kategorien> mainKategoriecb) {

		setTitle("Einnahme hinzufügen");				
		this.einnahmenFX = einnahmeFX;
		this.mainKategoriecb = mainKategoriecb;

		DialogPane dialogPane = getDialogPane();		//Dialog erstellen 
		dialogPane.setContent(createContent());
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		cbeinnahmenKategorien.setItems(olKategorien);

		Button save = (Button) this.getDialogPane().lookupButton(ButtonType.OK);		// alerts erstellen 
		save.addEventFilter(ActionEvent.ACTION, e -> {

			if (tfq.getText().isEmpty()) {
				new Alert(AlertType.ERROR, "Quelle eingeben").showAndWait();
				e.consume();
				return;
			}
			if (cbeinnahmenKategorien.getSelectionModel().getSelectedIndex() < 0) {
				new Alert(AlertType.ERROR, "Kategorie auswählen").showAndWait();
				e.consume();
				return;
			}

			try {
				Double.parseDouble(tfh.getText());
			} catch (NumberFormatException ex) {
				new Alert(AlertType.ERROR, "Gültige Höhe eingeben").showAndWait();
				e.consume();
				return;
			}
		});

		setResultConverter(buttonType -> {		
			if (buttonType == ButtonType.OK) {		// HandleOK aufrufen wenn true 
				if (!handleOK())
					return null;
			}
			return buttonType;
		});

		readEinnahmenKategorien();
	}

	private VBox createContent() {						
		tfq = new TextField(einnahmenFX.getQuelle());				// erstellt TextFeld und holt die Quelle aus einnahmenFX 
		tfq.setPromptText("Quelle");
		tfq.setPrefWidth(130);

		tfh = new TextField(Double.toString(einnahmenFX.getHoehe()));
		tfh.setPromptText("Höhe");
		tfh.setPrefWidth(130);

		dp = new DatePicker(einnahmenFX.getDp());
		dp.setPrefWidth(130);

		Label lbl = new Label("Wiederkehrende Transaktion?");
		rbj = new RadioButton("Ja");
		rbn = new RadioButton("Nein");

		ToggleGroup toggleGroup = new ToggleGroup();		//Schaut dass nur ein RB angeklickt wird 
		rbj.setToggleGroup(toggleGroup);
		rbn.setToggleGroup(toggleGroup);

		cbeinnahmenKategorien = new ChoiceBox<>(olKategorien); // neue CB

		readEinnahmenKategorien();
		if (einnahmenFX.getId() > 0) {		// schaut ob ID größer als 0 um zu prüfen ob es sich um eine neue einnahme handelt 
			Kategorien selectedKategorie = olKategorien.stream().filter(e -> e.getName().equals
					(einnahmenFX.getKategorie())).toList().get(0); // sucht nach der eingegeben kategorie 
			cbeinnahmenKategorien.getSelectionModel().select(selectedKategorie); // wählt die entsprechende kategorie aus der cb 
		}

		cbeinnahmenKategorien.setPrefWidth(130);

		cb2 = new ChoiceBox<>();
		cb2.getItems().addAll("Wöchentlich", "2 Wochen ", "Monatlich", "Vierteljährlich", "Halbjährlich", "Jährlich");
		cb2.setVisible(false);

		hinzufuegen = new Button("Hinzufügen");
		zurueck = new Button("Zurück");

		neueKatButton = new Button ("Neue Kategorie");
		neueKatButton.setOnAction(event-> { // wenn button geklickt 
			EinnahmenKategorieDialog dialog = new EinnahmenKategorieDialog(); // verweis auf diese Klasse 
			Optional<ButtonType> result = dialog.showAndWait(); // öffnet
			result.ifPresent(buttonType -> {
				if (buttonType == ButtonType.OK) { // Wenn ok angeklickt 
					readEinnahmenKategorien();  // read einnahmen 
					cbeinnahmenKategorien.setItems(olKategorien); // fügt der CB die ol hinzu 

				}
			});
		});
		kategorieHinzufuegenButton = new Button("Kategorie hinzufügen");
		neueKategorieTF = new TextField();
		neueKategorieTF.setPromptText("Neue Kategorie");
		neueKategorieTF.setPrefWidth(130);

		kategorieHinzufuegenButton.setOnAction(event -> {
			String neueKategorie = neueKategorieTF.getText(); 
			if (!neueKategorie.isEmpty()) { // prüft ob kategorie nicht leer ist 
				try {
					Datenbank.insertKategorie(new Kategorien(0, neueKategorie, true, false)); // wenn nicht leer in die insertKategorie 
					System.out.println("geschafft einnahmen");
					readEinnahmenKategorien(); // danach aktualisieren
					cbeinnahmenKategorien.setItems(olKategorien); // in die cb einfügen 
				}catch (SQLException e) {
					e.printStackTrace();
					System.out.println("nicht geschafft einnahmen"); 
				}
				neueKategorieTF.clear();
				cbeinnahmenKategorien.setItems(olKategorien);
			}
		});

		rbj.setOnAction(e -> cb2.setVisible(true));
		rbn.setOnAction(e -> cb2.setVisible(false));

		VBox content = new VBox();
		content.getChildren().addAll(neueKatButton, cbeinnahmenKategorien, tfq, tfh, dp, cb2, lbl, rbj, rbn);
		content.setSpacing(10);
		content.setPadding(new Insets(10));

		return content;
	}

	private void readEinnahmenKategorien() {
		olKategorien.clear();
		try {
			ArrayList<Kategorien> alKategorien = Datenbank.leseKategorien(Datenbank.KategorieTyp.EINNAHMEN);
			for(Kategorien eineKategorie : alKategorien)
				olKategorien.add(eineKategorie);
			mainKategoriecb.setItems(olKategorien);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean handleOK() {
		einnahmenFX.setQuelle(tfq.getText());
		einnahmenFX.setHoehe(Double.parseDouble(tfh.getText()));
		einnahmenFX.setDp(dp.getValue());
		einnahmenFX.setKategorie(cbeinnahmenKategorien.getValue().getName());
		einnahmenFX.getModellEinnahmen().setKategorie(cbeinnahmenKategorien.getValue());

		try {
			if(einnahmenFX.getId() == 0) {
				Datenbank.insertEinnahme(einnahmenFX.getModellEinnahmen());  	// warum ID 0 ? 
				if(cb2.getSelectionModel().getSelectedIndex() >= 0) {
					Datenbank.insertWiederholungenEinnahmen(new WiederholungenEinnahmen(0, einnahmenFX.getModellEinnahmen(), 
							Intervall.values()[cb2.getSelectionModel().getSelectedIndex()], 
							Intervall.values()[cb2.getSelectionModel().getSelectedIndex()].addTo(dp.getValue())));
				}
			}else {
				System.out.println("update wird ausgeführt Einnahmen");
				Datenbank.updateEinnahme(einnahmenFX.getModellEinnahmen());
			}
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
