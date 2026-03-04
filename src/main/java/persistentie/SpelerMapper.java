package persistentie;

import domein.Speler;

import java.sql.*;

public class SpelerMapper {

  // TODO: PreparedStatement Query invullen (Tip: kijk in het voorbeeldproject dobbelsteen)
  private static final String INSERT_SPELER = "INSERT INTO G85.speler (gebruikersnaam, geboortejaar) VALUES (?, ?)";

  // TODO: PreparedStatement Query invullen (Tip: kijk in het voorbeeldproject dobbelsteen)
  private static final String GEEF_SPELER = "SELECT gebruikersnaam, geboortejaar FROM Speler WHERE gebruikersnaam = ? AND geboortejaar = ?";

  public void voegToe(Speler speler) {
    try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
         PreparedStatement query = conn.prepareStatement(INSERT_SPELER)) {
      // TODO: Query variabelen invullen en uitvoeren, gebruik Speler (Tip: kijk in het voorbeeldproject dobbelsteen)
      query.setString(1, speler.getGebruikersnaam());
      query.setInt(2, speler.getGeboortejaar());
      //throw new UnsupportedOperationException("Not yet implemented");
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  public Speler geefSpeler(String gebruikersnaam) {
    Speler speler = null;
    try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
         PreparedStatement query = conn.prepareStatement(GEEF_SPELER)) {
      // TODO: Query variabelen invullen en uitvoeren, speler object aanmaken (Tip: kijk in het voorbeeldproject dobbelsteen)
        query.setString(1, gebruikersnaam);
        try (ResultSet rs =query.executeQuery()){
            if(rs.next()){
                String naam = rs.getString("gebruikersnaam");
                int geboortejaar = rs.getInt("geboortejaar");
                speler = new Speler(naam, geboortejaar);
            }
        }

    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
   return speler; // TODO: gevonden Speler of null retourneren
  }

  public Speler geefSpelerDummy(String gebruikersnaam) {
    return new Speler(gebruikersnaam, 1994);
  }
}
