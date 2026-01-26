package persistentie;

import domein.Speler;

import java.sql.*;

public class SpelerMapper {

  // TODO: PreparedStatement Query invullen (Tip: kijk in het voorbeeldproject dobbelsteen)
  private static final String INSERT_SPELER = "";

  // TODO: PreparedStatement Query invullen (Tip: kijk in het voorbeeldproject dobbelsteen)
  private static final String GEEF_SPELER = "";

  public void voegToe(Speler speler) {
    try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
         PreparedStatement query = conn.prepareStatement(INSERT_SPELER)) {
      // TODO: Query variabelen invullen en uitvoeren, gebruik Speler (Tip: kijk in het voorbeeldproject dobbelsteen)
      throw new UnsupportedOperationException("Not yet implemented");
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  public Speler geefSpeler(String gebruikersnaam) {
    Speler speler = null;
    try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
         PreparedStatement query = conn.prepareStatement(GEEF_SPELER)) {
      // TODO: Query variabelen invullen en uitvoeren, speler object aanmaken (Tip: kijk in het voorbeeldproject dobbelsteen)
      throw new UnsupportedOperationException("Not yet implemented");
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
    // TODO: gevonden Speler of null retourneren
  }

  public Speler geefSpelerDummy(String gebruikersnaam) {
    return new Speler(gebruikersnaam, 1994);
  }
}
