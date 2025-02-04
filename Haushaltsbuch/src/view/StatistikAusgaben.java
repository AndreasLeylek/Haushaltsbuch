package view;

import java.sql.SQLException;
import java.util.ArrayList;

import datenbank.Datenbank;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import model.Ausgaben;
import model.Einnahmen;
import model.Kategorien;

public class StatistikAusgaben extends Dialog<ButtonType> {

	@SuppressWarnings("exports")
	public static VBox erstelleAusgabenStatistik() {

		ObservableList<PieChart.Data> pieChartData = ladeAusgabenDaten();

		// Tortendiagramm erstellen
		PieChart pieChart = new PieChart(pieChartData);
		pieChart.setTitle("Ausgaben");

		VBox vBox = new VBox(pieChart);
		vBox.setMinWidth(700);
		vBox.setMinHeight(500); 

		return vBox;
	}


	private static ObservableList<PieChart.Data> ladeAusgabenDaten() {
	    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

	    // Ausgaben aus der Datenbank laden
	    ArrayList<Ausgaben> ausgabenListe;
	    try {
	        ausgabenListe = Datenbank.leseAusgaben(null, null, null);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return pieChartData;
	    }
	    		
	    // Summe der Ausgabe für jede Kategorie berechnen 
	    ArrayList<Kategorien> kategorienListe;
	    try {
	        kategorienListe = Datenbank.leseKategorien(Datenbank.KategorieTyp.AUSGABEN);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return pieChartData;
	    }

	    for (Kategorien kategorie : kategorienListe) {// Durchläuft die Liste der Kategorien
	        double summe = 0; // Summe 0 
	        for (Ausgaben ausgabe : ausgabenListe) { //Durchläuft die liste der ausgaben 
	            if (ausgabe.getKategorie().getId() == kategorie.getId()) { // Überprüft ob die Ausgabe zur aktuellen Kategorie gehört
	                summe += ausgabe.getPreis(); // Addiert den Preis der Ausgabe zur Summe
	            }
	        }
	       
	            pieChartData.add(new PieChart.Data(kategorie.getName(), summe));// Gibt die Liste pieChartData zurück, die für die Darstellung in einem Pie-Chart verwendet werden kann
	        }
	    

	    return pieChartData;
	}

}