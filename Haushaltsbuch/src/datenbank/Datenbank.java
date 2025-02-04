package datenbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import model.Ausgaben;
import model.Einnahmen;
import model.Intervall;
import model.Kategorien;
import model.Uebersicht;
import model.WiederholungenAusgaben;
import model.WiederholungenEinnahmen;
import model.WiederholungenEinnahmen;
import view.UebersichtFX;
import view.WiederholungenEinnahmenDialog;


 
public class Datenbank {




	//	private static final String DB_LOCATION = "C:\\Datenbanken\\ABCD";
	private static final String DB_LOCATION = "C:\\Datenbanken\\Database_Folder";
	private static final String CONNECTION = "jdbc:derby:" + DB_LOCATION + ";create=true";

	private static final String KATEGORIE = "Kategorie";
	private static final String KATEGORIE_TABLE_ID = "KategorieID";
	private static final String KATEGORIE_TABLE_NAME = "KategorieName";
	private static final String KATEGORIE_TABLE_IST_AUSGABE = "KategorieIstAusgabe";
	private static final String KATEGORIE_TABLE_IST_EINNAHME = "KategorieIstEinnahme";

	private static final String AUSGABEN_TABLE = "Ausgaben";
	private static final String AUSGABEN_TABLE_ID = "AusgabenID";
	private static final String AUSGABEN_TABLE_KATEGORIE = "AusgabeKategorie";
	private static final String AUSGABEN_TABLE_PRODUKT = "AusgabeProdukt";
	private static final String AUSGABEN_TABLE_PREIS = "AusgabePreis";
	private static final String AUSGABEN_TABLE_DATUM = "AusgabeDatum";

	private static final String EINNAHME_TABLE = "Einnahmen";
	private static final String EINNAHME_TABLE_ID = "EinnahmenID";
	private static final String EINNAHME_TABLE_KATEGORIE = "EinnahmenKategorie";
	private static final String EINNAHME_TABLE_QUELLE = "EinnahmenQuelle";
	private static final String EINNAHME_TABLE_HOEHE = "EinnahmenHoehe";
	private static final String EINNAHME_TABLE_DATUM = "EinnahmenDatum";		

	private static final String WIEDERHOLUNG_TABLE_EINNAHMEN = "WiederholungEinnahmen";
	private static final String WIEDERHOLUNG_TABLE_EINNAHMEN_ID = "WiederholungEinnahmenID";	
	private static final String WIEDERHOLUNG_TABLE_EINNAHMEN_EINNAHMENID = "WiederholungEinnahmenEinnahmenID";	
	private static final String WIEDERHOLUNG_TABLE_EINNAHMEN_INTERVALL = "WiederholungEinnahmenIntervall";
	private static final String WIEDERHOLUNG_TABLE_EINNAHMEN_DATUM = "WiederholungEinnahmenDatum";

	private static final String WIEDERHOLUNG_TABLE_AUSGABEN = "WiederholungAusgaben";
	private static final String WIEDERHOLUNG_TABLE_AUSGABEN_ID = "WiederholungAusgabenID";
	private static final String WIEDERHOLUNG_TABLE_AUSGABEN_AUSGABENID = "WiederholungAusgabenAusgabenID";
	private static final String WIEDERHOLUNG_TABLE_AUSGABEN_INTERVALL = "WiederholungAusgabenIntervall";
	private static final String WIEDERHOLUNG_TABLE_AUSGABEN_DATUM = "WiederholungAusgabenDatum";



	public static void createKategorie() throws SQLException {
		// Kategorie  Tabelle erstellen
		System.out.println("Kategorie-Tabelle wird erstellt...");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, KATEGORIE.toUpperCase(), new String[]{"TABLE"});
			if (rs.next())
				return;
			String ct = "CREATE TABLE " + KATEGORIE + " (" +
					KATEGORIE_TABLE_ID + " INTEGER GENERATED ALWAYS AS IDENTITY, " +
					KATEGORIE_TABLE_NAME + " VARCHAR(200), " +
					KATEGORIE_TABLE_IST_AUSGABE + " BOOLEAN," +
					KATEGORIE_TABLE_IST_EINNAHME + " BOOLEAN," +
					"PRIMARY KEY(" + KATEGORIE_TABLE_ID + "))"; 

			stmt.executeUpdate(ct);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}




	public static void createAusgabe() throws SQLException {
		System.out.println("Ausgabe-Tabelle wird erstellt...");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, AUSGABEN_TABLE.toUpperCase(), new String [] {"TABLE"});
			if (rs.next())
				return;
			String ct = "CREATE TABLE " + AUSGABEN_TABLE + " (" +
					AUSGABEN_TABLE_ID + " INTEGER GENERATED ALWAYS AS IDENTITY," +
					AUSGABEN_TABLE_KATEGORIE + " INTEGER," +
					AUSGABEN_TABLE_PRODUKT + " VARCHAR(200)," +
					AUSGABEN_TABLE_PREIS + " FLOAT," +
					AUSGABEN_TABLE_DATUM + " DATE," +
					"PRIMARY KEY(" + AUSGABEN_TABLE_ID + ")," + 
					"FOREIGN KEY(" + AUSGABEN_TABLE_KATEGORIE+ ") REFERENCES " + KATEGORIE  + "(" + KATEGORIE_TABLE_ID + ")" +
					")";
			stmt.executeUpdate(ct);
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	public static void createEinnahmen() throws SQLException {
		System.out.println("Einnahme-Tabelle wird erstellt...");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, EINNAHME_TABLE.toUpperCase(), new String [] {"TABLE"});
			if (rs.next())
				return;
			String ct = "CREATE TABLE " + EINNAHME_TABLE + " (" +
					EINNAHME_TABLE_ID + " INTEGER GENERATED ALWAYS AS IDENTITY," +
					EINNAHME_TABLE_KATEGORIE + " INTEGER," +
					EINNAHME_TABLE_QUELLE + " VARCHAR(200)," +
					EINNAHME_TABLE_HOEHE + " FLOAT," +
					EINNAHME_TABLE_DATUM + " DATE," +
					"PRIMARY KEY(" + EINNAHME_TABLE_ID + ")," + 
					"FOREIGN KEY(" + EINNAHME_TABLE_KATEGORIE+ ") REFERENCES " + KATEGORIE  + "(" + KATEGORIE_TABLE_ID + ")" +
					")";
			stmt.executeUpdate(ct);
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	public static void createWiederholungEinnahmen() throws SQLException {
		System.out.println("Wiederholung-Tabelle Einnahme wird erstellt...");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, WIEDERHOLUNG_TABLE_EINNAHMEN.toUpperCase(), new String[]{"TABLE"});
			if (rs.next())
				return;
			String ct = "CREATE TABLE " + WIEDERHOLUNG_TABLE_EINNAHMEN + " (" +
					WIEDERHOLUNG_TABLE_EINNAHMEN_ID + " INTEGER GENERATED ALWAYS AS IDENTITY," +
					WIEDERHOLUNG_TABLE_EINNAHMEN_EINNAHMENID + " INTEGER," +
					WIEDERHOLUNG_TABLE_EINNAHMEN_INTERVALL + " INTEGER," +					
					WIEDERHOLUNG_TABLE_EINNAHMEN_DATUM + " DATE," +
					"PRIMARY KEY(" + WIEDERHOLUNG_TABLE_EINNAHMEN_ID + ")," +
					"FOREIGN KEY(" + WIEDERHOLUNG_TABLE_EINNAHMEN_EINNAHMENID + ") REFERENCES " + EINNAHME_TABLE + "(" + EINNAHME_TABLE_ID + ")" +
					")";
			stmt.executeUpdate(ct);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void createWiederholungAusgaben() throws SQLException {
		System.out.println("Wiederholung-Tabelle Ausgaben wird erstellt...");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, WIEDERHOLUNG_TABLE_AUSGABEN.toUpperCase(), new String[]{"TABLE"});
			if (rs.next())
				return;
			String ct = "CREATE TABLE " + WIEDERHOLUNG_TABLE_AUSGABEN + " (" +
					WIEDERHOLUNG_TABLE_AUSGABEN_ID + " INTEGER GENERATED ALWAYS AS IDENTITY," +
					WIEDERHOLUNG_TABLE_AUSGABEN_AUSGABENID + " INTEGER," +
					WIEDERHOLUNG_TABLE_AUSGABEN_INTERVALL + " INTEGER," +					
					WIEDERHOLUNG_TABLE_AUSGABEN_DATUM + " DATE," +
					"PRIMARY KEY(" + WIEDERHOLUNG_TABLE_AUSGABEN_ID + ")," +
					"FOREIGN KEY(" + WIEDERHOLUNG_TABLE_AUSGABEN_AUSGABENID + ") REFERENCES " + AUSGABEN_TABLE + "(" + AUSGABEN_TABLE_ID + ")" +
					")";
			stmt.executeUpdate(ct);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}


	public static void insertAusgabe(Ausgaben ausgaben) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		String insert = "INSERT INTO " + AUSGABEN_TABLE + " (" +
				AUSGABEN_TABLE_KATEGORIE + ", " +
				AUSGABEN_TABLE_PRODUKT + ", " +
				AUSGABEN_TABLE_PREIS + ", " +
				AUSGABEN_TABLE_DATUM + ") " +
				"VALUES (?, ?, ?, ?)";

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);

			stmt.setInt(1, ausgaben.getKategorie().getId());
			stmt.setString(2, ausgaben.getProdukt());
			stmt.setDouble(3, ausgaben.getPreis());
			stmt.setDate(4, java.sql.Date.valueOf(ausgaben.getDp()));
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next())
				ausgaben.setId(rs.getInt(1));

		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void insertEinnahme(Einnahmen einnahme) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		String insert = "INSERT INTO " + EINNAHME_TABLE + " (" +
				EINNAHME_TABLE_KATEGORIE + ", " +
				EINNAHME_TABLE_QUELLE + ", " +
				EINNAHME_TABLE_HOEHE + ", " +
				EINNAHME_TABLE_DATUM + ") " +
				"VALUES (?, ?, ?, ?)";

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);

			stmt.setInt(1, einnahme.getKategorie().getId());
			stmt.setString(2, einnahme.getQuelle());
			stmt.setDouble(3, einnahme.getHoehe());
			stmt.setDate(4, java.sql.Date.valueOf(einnahme.getDp()));
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next())
				einnahme.setId(rs.getInt(1));

		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	public static void insertKategorie(Kategorien kategorie) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection(CONNECTION);
			String insertQuery = "INSERT INTO " + KATEGORIE + " (" +
					KATEGORIE_TABLE_NAME + ", " +
					KATEGORIE_TABLE_IST_EINNAHME + ", " +
					KATEGORIE_TABLE_IST_AUSGABE  +	
					") VALUES (?, ?, ?)";

			pstmt = conn.prepareStatement(insertQuery);
			pstmt.setString(1, kategorie.getName());
			pstmt.setBoolean(2, kategorie.isEinnahme()); 
			pstmt.setBoolean(3, kategorie.isAusgabe()); 

			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void insertWiederholungenEinnahmen(WiederholungenEinnahmen wiederholungenE) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection(CONNECTION);
			String insertQuery = "INSERT INTO " + WIEDERHOLUNG_TABLE_EINNAHMEN + " (" +
					WIEDERHOLUNG_TABLE_EINNAHMEN_EINNAHMENID + ", " +
					WIEDERHOLUNG_TABLE_EINNAHMEN_INTERVALL + ", " +
					WIEDERHOLUNG_TABLE_EINNAHMEN_DATUM + ") " +
					"VALUES (?, ?, ?)";

			pstmt = conn.prepareStatement(insertQuery);


			pstmt.setInt(1, wiederholungenE.getEinnahmen().getId());
			pstmt.setInt(2, wiederholungenE.getIntervall().ordinal());
			pstmt.setDate(3, java.sql.Date.valueOf(wiederholungenE.getDp()));

			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void insertWiederholungenAusgaben(WiederholungenAusgaben wiederholungenA) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection(CONNECTION);
			String insertQuery = "INSERT INTO " + WIEDERHOLUNG_TABLE_AUSGABEN + " (" +
					WIEDERHOLUNG_TABLE_AUSGABEN_AUSGABENID + ", " +
					WIEDERHOLUNG_TABLE_AUSGABEN_INTERVALL + ", " +
					WIEDERHOLUNG_TABLE_AUSGABEN_DATUM + ") " +
					"VALUES (?, ?, ?)";

			pstmt = conn.prepareStatement(insertQuery);


			pstmt.setInt(1, wiederholungenA.getAusgaben().getId());
			pstmt.setInt(2, wiederholungenA.getIntervall().ordinal());
			pstmt.setDate(3, java.sql.Date.valueOf(wiederholungenA.getDp()));

			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public enum KategorieTyp {EINNAHMEN, AUSGABEN}
	public static ArrayList<Kategorien> leseKategorien(KategorieTyp type) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String select = "SELECT * FROM " + KATEGORIE;
			if(type == KategorieTyp.EINNAHMEN) {
				select += " WHERE " + KATEGORIE_TABLE_IST_EINNAHME + "=?";
			}
			else if(type == KategorieTyp.AUSGABEN) {
				select += " WHERE " + KATEGORIE_TABLE_IST_AUSGABE + "=?";
			}
			pstmt = conn.prepareStatement(select);
			if(type == KategorieTyp.EINNAHMEN) {
				pstmt.setBoolean(1, true);
			}
			else if(type == KategorieTyp.AUSGABEN) {
				pstmt.setBoolean(1, true);
			}
			rs = pstmt.executeQuery();
			ArrayList<Kategorien> alKategorien = new ArrayList<>();
			while(rs.next())
				alKategorien.add(new Kategorien(rs.getInt(KATEGORIE_TABLE_ID), rs.getString(KATEGORIE_TABLE_NAME), 
						rs.getBoolean(KATEGORIE_TABLE_IST_EINNAHME), rs.getBoolean(KATEGORIE_TABLE_IST_AUSGABE) ));
			rs.close();
			return alKategorien;
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static ArrayList<Ausgaben> leseAusgaben(LocalDate startdatum, LocalDate enddatum, Kategorien kategorie) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean bothdates = false;
		int indexKategorieId = 1;
		ArrayList<Ausgaben> ausgabenListe = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String selectQuery = "SELECT * FROM " + AUSGABEN_TABLE + " INNER JOIN " + KATEGORIE + " ON " +
					AUSGABEN_TABLE + "." + AUSGABEN_TABLE_KATEGORIE +"=" + KATEGORIE + "." + KATEGORIE_TABLE_ID;

			if (startdatum != null || enddatum != null || kategorie != null ) {
				selectQuery = selectQuery.concat(" WHERE ");
			}
			if (startdatum != null ) {
				indexKategorieId++;
				selectQuery = selectQuery.concat(AUSGABEN_TABLE_DATUM + " >= ? ");			
			}
			if (startdatum != null && enddatum != null) {
				selectQuery = selectQuery.concat(" AND ");
				bothdates = true;
			}
			if (enddatum != null) {
				indexKategorieId++;
				selectQuery = selectQuery.concat(AUSGABEN_TABLE_DATUM + " <= ? ");			
			}
			if ((startdatum != null || enddatum != null) && kategorie != null ) {
				selectQuery = selectQuery.concat(" AND ");
			}
			if(kategorie != null ) {
				selectQuery = selectQuery.concat(AUSGABEN_TABLE_KATEGORIE + " =? ");
			}

			pstmt = conn.prepareStatement(selectQuery);
			if (startdatum != null) {
				pstmt.setDate(1, java.sql.Date.valueOf(startdatum));
			}
			if (enddatum != null) {
				pstmt.setDate(bothdates == true ? 2 : 1, java.sql.Date.valueOf(enddatum)); //  inline if, if in einer Zeile 
			}

			if (kategorie != null) {
				pstmt.setInt(indexKategorieId, kategorie.getId());
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				Ausgaben ausgabe = new Ausgaben();
				ausgabe.setId(rs.getInt(AUSGABEN_TABLE_ID));
				ausgabe.setKategorie(new Kategorien(
						rs.getInt(KATEGORIE_TABLE_ID), 
						rs.getString(KATEGORIE_TABLE_NAME), 
						rs.getBoolean(KATEGORIE_TABLE_IST_EINNAHME), 
						rs.getBoolean(KATEGORIE_TABLE_IST_AUSGABE)));
				ausgabe.setProdukt(rs.getString(AUSGABEN_TABLE_PRODUKT));
				ausgabe.setPreis(rs.getDouble(AUSGABEN_TABLE_PREIS));
				ausgabe.setDp(rs.getDate(AUSGABEN_TABLE_DATUM).toLocalDate());
				ausgabenListe.add(ausgabe);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
		return ausgabenListe;
	}

	public static ArrayList<Einnahmen> leseEinnahmen(LocalDate startdatum, LocalDate enddatum, Kategorien kategorie) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean bothdates = false;
		int indexKategorieId = 1;
		ArrayList<Einnahmen> einnahmenListe = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String selectQuery = "SELECT * FROM " + EINNAHME_TABLE + " INNER JOIN " + KATEGORIE + " ON " +
					EINNAHME_TABLE + "." + EINNAHME_TABLE_KATEGORIE +"=" + KATEGORIE + "." + KATEGORIE_TABLE_ID;

			if (startdatum != null || enddatum != null || kategorie != null ) {
				selectQuery = selectQuery.concat(" WHERE ");
			}
			if (startdatum != null ) {
				indexKategorieId++;
				selectQuery = selectQuery.concat(EINNAHME_TABLE_DATUM + " >= ? ");			
			}
			if (startdatum != null && enddatum != null) {
				selectQuery = selectQuery.concat(" AND ");
				bothdates = true;
			}
			if (enddatum != null) {
				indexKategorieId++;
				selectQuery = selectQuery.concat(EINNAHME_TABLE_DATUM + " <= ? ");			
			}
			if ((startdatum != null || enddatum != null) && kategorie != null ) {
				selectQuery = selectQuery.concat(" AND ");
			}
			if(kategorie != null ) {
				selectQuery = selectQuery.concat(EINNAHME_TABLE_KATEGORIE + " =? ");
			}

			pstmt = conn.prepareStatement(selectQuery);
			if (startdatum != null) {
				pstmt.setDate(1, java.sql.Date.valueOf(startdatum));
			}
			if (enddatum != null) {
				pstmt.setDate(bothdates == true ? 2 : 1, java.sql.Date.valueOf(enddatum)); //  inline if, if in einer Zeile 
			}

			if (kategorie != null) {
				pstmt.setInt(indexKategorieId, kategorie.getId());
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				Einnahmen einnahmen = new Einnahmen();
				einnahmen.setId(rs.getInt(EINNAHME_TABLE_ID));
				einnahmen.setKategorie(new Kategorien(
						rs.getInt(KATEGORIE_TABLE_ID), 
						rs.getString(KATEGORIE_TABLE_NAME), 
						rs.getBoolean(KATEGORIE_TABLE_IST_EINNAHME), 
						rs.getBoolean(KATEGORIE_TABLE_IST_AUSGABE)));
				einnahmen.setQuelle(rs.getString(EINNAHME_TABLE_QUELLE));
				einnahmen.setHoehe(rs.getDouble(EINNAHME_TABLE_HOEHE));
				einnahmen.setDp(rs.getDate(EINNAHME_TABLE_DATUM).toLocalDate());
				einnahmenListe.add(einnahmen);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
		return einnahmenListe;
	}

	public static ArrayList<WiederholungenEinnahmen> leseWiederholungenEinnahmen() throws SQLException {
		Connection conn = null;
		PreparedStatement pstmtEinnahmen = null;
		ResultSet rsEinnahmen = null;
		ArrayList<WiederholungenEinnahmen> wiederholungenListeEinnahmen = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String selectEinnahmenQuery = "SELECT * FROM " + WIEDERHOLUNG_TABLE_EINNAHMEN + " INNER JOIN " + EINNAHME_TABLE +
					" ON " + WIEDERHOLUNG_TABLE_EINNAHMEN + "." + WIEDERHOLUNG_TABLE_EINNAHMEN_EINNAHMENID + "=" + EINNAHME_TABLE + "." + EINNAHME_TABLE_ID + 
					" INNER JOIN " + KATEGORIE + " ON " +
					EINNAHME_TABLE + "." + EINNAHME_TABLE_KATEGORIE +"=" + KATEGORIE + "." + KATEGORIE_TABLE_ID;

			pstmtEinnahmen = conn.prepareStatement(selectEinnahmenQuery);
			rsEinnahmen = pstmtEinnahmen.executeQuery();
			while (rsEinnahmen.next()) {

				Einnahmen einnahmen = new Einnahmen();

				WiederholungenEinnahmen wiederholungE = new WiederholungenEinnahmen(rsEinnahmen.getInt(WIEDERHOLUNG_TABLE_EINNAHMEN_ID), 
						einnahmen, 
						Intervall.values()[rsEinnahmen.getInt(WIEDERHOLUNG_TABLE_EINNAHMEN_INTERVALL)] , 
						rsEinnahmen.getDate(WIEDERHOLUNG_TABLE_EINNAHMEN_DATUM).toLocalDate());

				einnahmen.setId(rsEinnahmen.getInt(EINNAHME_TABLE_ID));
				einnahmen.setKategorie(new Kategorien(rsEinnahmen.getInt(KATEGORIE_TABLE_ID), rsEinnahmen.getString(KATEGORIE_TABLE_NAME), 
						rsEinnahmen.getBoolean(KATEGORIE_TABLE_IST_EINNAHME), rsEinnahmen.getBoolean(KATEGORIE_TABLE_IST_AUSGABE)));
				einnahmen.setQuelle(rsEinnahmen.getString(EINNAHME_TABLE_QUELLE));
				einnahmen.setHoehe(rsEinnahmen.getDouble(EINNAHME_TABLE_HOEHE));
				einnahmen.setDp(rsEinnahmen.getDate(EINNAHME_TABLE_DATUM).toLocalDate());

				wiederholungenListeEinnahmen.add(wiederholungE);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rsEinnahmen != null) rsEinnahmen.close();
				if (pstmtEinnahmen != null) pstmtEinnahmen.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
		return wiederholungenListeEinnahmen;
	}

	public static ArrayList<WiederholungenAusgaben> leseWiederholungenAusgaben() throws SQLException {
		Connection conn = null;
		PreparedStatement pstmtAusgaben = null;
		ResultSet rsAusgaben = null;
		ArrayList<WiederholungenAusgaben> wiederholungenListeAusgaben = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String selectAusgabenQuery = "SELECT * FROM " + WIEDERHOLUNG_TABLE_AUSGABEN + " INNER JOIN " + AUSGABEN_TABLE +
					" ON " + WIEDERHOLUNG_TABLE_AUSGABEN + "." + WIEDERHOLUNG_TABLE_AUSGABEN_AUSGABENID + "=" + AUSGABEN_TABLE + "." + AUSGABEN_TABLE_ID + 
					" INNER JOIN " + KATEGORIE + " ON " +
					AUSGABEN_TABLE + "." + AUSGABEN_TABLE_KATEGORIE +"=" + KATEGORIE + "." + KATEGORIE_TABLE_ID;

			pstmtAusgaben = conn.prepareStatement(selectAusgabenQuery);
			rsAusgaben = pstmtAusgaben.executeQuery();
			while (rsAusgaben.next()) {

				Ausgaben ausgaben = new Ausgaben();

				WiederholungenAusgaben wiederholungA = new WiederholungenAusgaben(rsAusgaben.getInt(WIEDERHOLUNG_TABLE_AUSGABEN_ID), 
						ausgaben, 
						Intervall.values()[rsAusgaben.getInt(WIEDERHOLUNG_TABLE_AUSGABEN_INTERVALL)] , 
						rsAusgaben.getDate(WIEDERHOLUNG_TABLE_AUSGABEN_DATUM).toLocalDate());

				ausgaben.setId(rsAusgaben.getInt(AUSGABEN_TABLE_ID));
				ausgaben.setKategorie(new Kategorien(rsAusgaben.getInt(KATEGORIE_TABLE_ID), rsAusgaben.getString(KATEGORIE_TABLE_NAME), 
						rsAusgaben.getBoolean(KATEGORIE_TABLE_IST_EINNAHME), rsAusgaben.getBoolean(KATEGORIE_TABLE_IST_AUSGABE)));
				ausgaben.setProdukt(rsAusgaben.getString(AUSGABEN_TABLE_PRODUKT));
				ausgaben.setPreis(rsAusgaben.getDouble(AUSGABEN_TABLE_PREIS));
				ausgaben.setDp(rsAusgaben.getDate(AUSGABEN_TABLE_DATUM).toLocalDate());

				wiederholungenListeAusgaben.add(wiederholungA);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rsAusgaben != null) rsAusgaben.close();
				if (pstmtAusgaben != null) pstmtAusgaben.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
		return wiederholungenListeAusgaben;
	}

	public static void updateAusgabenWiederholungen (WiederholungenAusgaben wiederholungenausgaben) throws SQLException{

		Connection conn = null;
		PreparedStatement stmt = null;

		String insert = "UPDATE " + WIEDERHOLUNG_TABLE_AUSGABEN + " SET " 
				+ WIEDERHOLUNG_TABLE_AUSGABEN_INTERVALL + "=?,"
				+ WIEDERHOLUNG_TABLE_AUSGABEN_DATUM + "=?" +
				" WHERE " + WIEDERHOLUNG_TABLE_AUSGABEN_ID + "=?";

		try {

			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.prepareStatement(insert);

			stmt.setInt(1, wiederholungenausgaben.getIntervall().ordinal());
			stmt.setDate(2, java.sql.Date.valueOf(wiederholungenausgaben.getDp()));
			stmt.setInt(3, wiederholungenausgaben.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}


	public static void updateEinnahmenWiederholungen (WiederholungenEinnahmen wiederholungeneinnahmen) throws SQLException{

		Connection conn = null;
		PreparedStatement stmt = null;

		String insert = "UPDATE " + WIEDERHOLUNG_TABLE_EINNAHMEN + " SET " 
		        + WIEDERHOLUNG_TABLE_EINNAHMEN_INTERVALL + "=?, "
		        + WIEDERHOLUNG_TABLE_EINNAHMEN_DATUM + "=? " +
		        " WHERE " + WIEDERHOLUNG_TABLE_EINNAHMEN_ID + "=?";


		try {

			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.prepareStatement(insert);

			stmt.setInt(1, wiederholungeneinnahmen.getIntervall().ordinal());
			stmt.setDate(2, java.sql.Date.valueOf(wiederholungeneinnahmen.getDp()));
			stmt.setInt(3, wiederholungeneinnahmen.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	public static void deleteAusgabenWiederholungen (int id) throws SQLException{


		Connection conn = null;
		PreparedStatement stmt = null;
		String delete = "DELETE FROM " + WIEDERHOLUNG_TABLE_AUSGABEN + " WHERE " + WIEDERHOLUNG_TABLE_AUSGABEN_ID + "=?";
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.prepareStatement(delete);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void deleteEinnahmenWiederholungen (int id) throws SQLException{


		Connection conn = null;
		PreparedStatement stmt = null;
		String delete = "DELETE FROM " + WIEDERHOLUNG_TABLE_EINNAHMEN + " WHERE " + WIEDERHOLUNG_TABLE_EINNAHMEN_ID + "=?";
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.prepareStatement(delete);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}


	public static void deleteAusgabe(int id) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		String delete = "DELETE FROM " + AUSGABEN_TABLE + " WHERE " + AUSGABEN_TABLE_ID + "=?";
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.prepareStatement(delete);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void deleteEinnahmen(int id) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		String delete = "DELETE FROM " + EINNAHME_TABLE + " WHERE " + EINNAHME_TABLE_ID + "=?";
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.prepareStatement(delete);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void deleteKategorieEinnahmen(int id) throws SQLException {
		Connection conn = null;
		PreparedStatement stmtEinnahmen = null;
		PreparedStatement stmtKategorie = null;
		String deleteEinnahmen = "DELETE FROM " + EINNAHME_TABLE + " WHERE " + EINNAHME_TABLE_KATEGORIE + "=?";
		String deleteKategorie = "DELETE FROM " + KATEGORIE + " WHERE " + KATEGORIE_TABLE_ID + "=?";

		try {
			conn = DriverManager.getConnection(CONNECTION);
			conn.setAutoCommit(false); // Starte Transaktion

			// Löschen der Einträge in der Einnahme-Tabelle, die dieser Kategorie zugeordnet sind
			stmtEinnahmen = conn.prepareStatement(deleteEinnahmen);
			stmtEinnahmen.setInt(1, id);
			stmtEinnahmen.executeUpdate();

			// Löschen der Kategorie
			stmtKategorie = conn.prepareStatement(deleteKategorie);
			stmtKategorie.setInt(1, id);
			stmtKategorie.executeUpdate();

			conn.commit(); // Transaktion erfolgreich, Änderungen speichern
		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback(); // Transaktion rückgängig machen
			}
			throw e;
		} finally {
			try {
				if (stmtEinnahmen != null) {
					stmtEinnahmen.close();
				}
				if (stmtKategorie != null) {
					stmtKategorie.close();
				}
				if (conn != null) {
					conn.setAutoCommit(true); // Zurücksetzen auf automatische Commit-Einstellung
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void deleteKategorieAusgabe(int id) throws SQLException {
		Connection conn = null;
		PreparedStatement stmtAusgabe = null;
		PreparedStatement stmtKategorie = null;
		String deleteAusgabe = "DELETE FROM " + AUSGABEN_TABLE + " WHERE " + AUSGABEN_TABLE_KATEGORIE + "=?";
		String deleteKategorie = "DELETE FROM " + KATEGORIE + " WHERE " + KATEGORIE_TABLE_ID + "=?";

		try {
			conn = DriverManager.getConnection(CONNECTION);
			conn.setAutoCommit(false); // Starte Transaktion

			// Löschen der Einträge in der Einnahme-Tabelle, die dieser Kategorie zugeordnet sind
			stmtAusgabe = conn.prepareStatement(deleteAusgabe);
			stmtAusgabe.setInt(1, id);
			stmtAusgabe.executeUpdate();

			// Löschen der Kategorie
			stmtKategorie = conn.prepareStatement(deleteKategorie);
			stmtKategorie.setInt(1, id);
			stmtKategorie.executeUpdate();

			conn.commit(); // Transaktion erfolgreich, Änderungen speichern
		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback(); // Transaktion rückgängig machen
			}
			throw e;
		} finally {
			try {
				if (stmtAusgabe != null) {
					stmtAusgabe.close();
				}
				if (stmtKategorie != null) {
					stmtKategorie.close();
				}
				if (conn != null) {
					conn.setAutoCommit(true); // Zurücksetzen auf automatische Commit-Einstellung
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}


	public static void deleteEinnahmenWiederholungenbyKategorienID(int id) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		String delete = "DELETE FROM " + KATEGORIE + " WHERE " + WIEDERHOLUNG_TABLE_EINNAHMEN_EINNAHMENID + " = ?)";

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.prepareStatement(delete);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void deleteAusgabenWiederholungenbyKategorienID(int id) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		String delete = "DELETE FROM " + KATEGORIE + " WHERE " + WIEDERHOLUNG_TABLE_AUSGABEN_AUSGABENID + " = ?)";

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.prepareStatement(delete);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void deleteAusgabenbyKategorieID(int id) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		String delete = "DELETE FROM " + AUSGABEN_TABLE + " WHERE " + AUSGABEN_TABLE_KATEGORIE + " = ?)";

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.prepareStatement(delete);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}


	public static void updateAusgabe(Ausgaben ausgabe) throws SQLException {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    String insert = "UPDATE " + AUSGABEN_TABLE + " SET " 
	            + AUSGABEN_TABLE_KATEGORIE + "=?," 
	            + AUSGABEN_TABLE_PRODUKT + "=?," 
	            + AUSGABEN_TABLE_PREIS + "=?," 
	            + AUSGABEN_TABLE_DATUM + "=?" +
	            " WHERE " + AUSGABEN_TABLE_ID + "=?";

	    try {
	        conn = DriverManager.getConnection(CONNECTION);
	        stmt = conn.prepareStatement(insert);

	        stmt.setInt(1, ausgabe.getKategorie().getId());
	        stmt.setString(2, ausgabe.getProdukt());
	        stmt.setDouble(3, ausgabe.getPreis());
	        stmt.setDate(4, java.sql.Date.valueOf(ausgabe.getDp()));
	        stmt.setInt(5, ausgabe.getId());
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        throw e;
	    } finally {
	        try {
	            if (stmt != null) {
	                stmt.close();
	            }
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            throw e;
	        }
	    }
	}

	
	public static void updateEinnahme(Einnahmen einnahme) throws SQLException {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    String insert = "UPDATE " + EINNAHME_TABLE + " SET " 
	            + EINNAHME_TABLE_KATEGORIE + "=?," 
	            + EINNAHME_TABLE_QUELLE + "=?," 
	            + EINNAHME_TABLE_HOEHE + "=?," 
	            + EINNAHME_TABLE_DATUM + "=?" +
	            " WHERE " + EINNAHME_TABLE_ID + "=?";

	    try {
	        conn = DriverManager.getConnection(CONNECTION);
	        stmt = conn.prepareStatement(insert);

	        stmt.setInt(1, einnahme.getKategorie().getId());
	        stmt.setString(2, einnahme.getQuelle());
	        stmt.setDouble(3, einnahme.getHoehe());
	        stmt.setDate(4, java.sql.Date.valueOf(einnahme.getDp()));
	        stmt.setInt(5, einnahme.getId());
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        throw e;
	    } finally {
	        try {
	            if (stmt != null) {
	                stmt.close();
	            }
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            throw e;
	        }
	    }
	}


//	public static void updateEinnahme (Einnahmen einnahme ) throws SQLException{
//
//		Connection conn = null;
//		PreparedStatement stmt = null;
//		String insert = "UPDATE " + EINNAHME_TABLE + " SET " 
//				+ EINNAHME_TABLE_KATEGORIE + "=?," 
//				+ EINNAHME_TABLE_QUELLE + "=?," 
//				+ EINNAHME_TABLE_HOEHE + "=?," 
//				+ EINNAHME_TABLE_DATUM + "=?" +
//				" WHERE " + EINNAHME_TABLE_ID + "=?";
//
//		try {
//
//			conn = DriverManager.getConnection(CONNECTION);
//			stmt = conn.prepareStatement(insert); // , PreparedStatement.RETURN_GENERATED_KEYS
//
//			stmt.setInt(1, einnahme.getKategorie().getId());
//			stmt.setString(2, einnahme.getQuelle());
//			stmt.setDouble(3, einnahme.getHoehe());
//			stmt.setDate(4, java.sql.Date.valueOf(einnahme.getDp()));
//			stmt.setInt(5, einnahme.getId());
//			stmt.executeUpdate();
//			ResultSet rs = stmt.getGeneratedKeys();
//			if(rs.next())
//				einnahme.setId(rs.getInt(1));
//		} catch (SQLException e) {
//			throw e;
//		} finally {
//			try {
//				if (stmt != null) {
//					stmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				throw e;
//			}
//		}
//	}

	public static void updateKategorie(Kategorien kategorie) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String updateQuery = "UPDATE " + KATEGORIE + " SET " +
				KATEGORIE_TABLE_NAME + "=?, " +
				KATEGORIE_TABLE_IST_EINNAHME + "=?, " +
				KATEGORIE_TABLE_IST_AUSGABE + "=? " +
				"WHERE " + KATEGORIE_TABLE_ID + "=?";

		try {
			conn = DriverManager.getConnection(CONNECTION);
			pstmt = conn.prepareStatement(updateQuery);

			pstmt.setString(1, kategorie.getName());
			pstmt.setBoolean(2, kategorie.isEinnahme());
			pstmt.setBoolean(3, kategorie.isAusgabe());
			pstmt.setInt(4, kategorie.getId());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void deleteAusgabenWiederholungenbyAusgabenID (int id) throws SQLException{


		Connection conn = null;
		PreparedStatement stmt = null;
		String delete = "DELETE FROM " + WIEDERHOLUNG_TABLE_AUSGABEN + " WHERE " + WIEDERHOLUNG_TABLE_AUSGABEN_AUSGABENID + "=?";
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.prepareStatement(delete);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void deleteEinnahmenWiederholungenbyEinnahmenID (int id) throws SQLException{


		Connection conn = null;
		PreparedStatement stmt = null;
		String delete = "DELETE FROM " + WIEDERHOLUNG_TABLE_EINNAHMEN + " WHERE " + WIEDERHOLUNG_TABLE_EINNAHMEN_EINNAHMENID + "=?";
		try {

			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.prepareStatement(delete);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}


}