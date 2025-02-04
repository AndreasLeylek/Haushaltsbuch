package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import model.Einnahmen;
import model.Kategorien;
import datenbank.Datenbank;

import java.sql.SQLException;
import java.util.ArrayList;

public class StatistikEinnahmen extends Dialog<ButtonType> {

	@SuppressWarnings("exports")
	public static VBox erstelleEinnahmenStatistik() {

		ObservableList<PieChart.Data> pieChartData = ladeEinnahmenDaten();

		// Tortendiagramm erstellen
		PieChart pieChart = new PieChart(pieChartData);
		pieChart.setTitle("Einnahmen");


		VBox vBox = new VBox(pieChart);

		return vBox;
	}

	private static ObservableList<PieChart.Data> ladeEinnahmenDaten() {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

		// Einnahmen aus der Datenbank laden
		ArrayList<Einnahmen> einnahmenListe = new ArrayList<>();
		try {
			einnahmenListe = Datenbank.leseEinnahmen(null, null, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Summe der Einnahmen für jede Kategorie berechnen
		ArrayList<Kategorien> kategorienListe = new ArrayList<>();
		try {
			kategorienListe = Datenbank.leseKategorien(Datenbank.KategorieTyp.EINNAHMEN);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (Kategorien kategorie : kategorienListe) {	// Durchläuft die Liste der Kategorien
			double summe = 0;
			for (Einnahmen einnahme : einnahmenListe) {//Durchläuft die liste der einnahmen
				if (einnahme.getKategorie().getId() == kategorie.getId()){// Überprüft ob die Ausgabe zur aktuellen Kategorie gehört
					summe += einnahme.getHoehe();// Addiert den Preis der Ausgabe zur Summe
				}
			}
			pieChartData.add(new PieChart.Data(kategorie.getName(), summe));// Gibt die Liste pieChartData zurück, die für die Darstellung in einem Pie-Chart verwendet werden kann
		}

		return pieChartData;
	}
}




