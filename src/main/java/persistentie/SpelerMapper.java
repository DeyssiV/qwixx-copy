package persistentie;

import domein.Speler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpelerMapper {

    private static final String INSERT_SPELER = "INSERT INTO G85.speler (gebruikersnaam, geboortejaar, aantalGespeeld, aantalGewonnen) VALUES (?, ?, ?, ?)";

    private static final String GEEF_SPELER = "SELECT gebruikersnaam, geboortejaar, aantalGespeeld, aantalGewonnen FROM speler WHERE gebruikersnaam = ?";

    private static final String GEEF_SPELERS = "SELECT gebruikersnaam, geboortejaar, aantalGespeeld, aantalGewonnen FROM speler";

    // Nieuw: voor updaten na een spel
    private static final String UPDATE_STATS = "UPDATE g85.speler SET aantalGespeeld = ?, aantalGewonnen = ? WHERE gebruikersnaam = ?";
//  // TODO: PreparedStatement Query invullen (Tip: kijk in het voorbeeldproject dobbelsteen)
//  private static final String INSERT_SPELER = "INSERT INTO G85.speler (gebruikersnaam, geboortejaar) VALUES (?, ?)";
//
//  // TODO: PreparedStatement Query invullen (Tip: kijk in het voorbeeldproject dobbelsteen)
//  private static final String GEEF_SPELER = "SELECT gebruikersnaam, geboortejaar FROM speler WHERE gebruikersnaam = ?";
//
//  private static final String GEEF_SPELERS = "SELECT gebruikersnaam, geboortejaar FROM speler";

    public void voegToe(Speler speler) {
        try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
             PreparedStatement query = conn.prepareStatement(INSERT_SPELER)) {
            // TODO: Query variabelen invullen en uitvoeren, gebruik Speler (Tip: kijk in het voorbeeldproject dobbelsteen)
            query.setString(1, speler.getGebruikersnaam());
            query.setInt(2, speler.getGeboortejaar());
            query.setInt(3, speler.getAantalGespeeld());
            query.setInt(4, speler.getAantalGewonnen());

            query.executeUpdate(); //Zorgt ervoor dat het in de database komt
            //throw new UnsupportedOperationException("Not yet implemented");
        } catch (SQLIntegrityConstraintViolationException ex) {
            throw new exceptions.GebruikersnaamInGebruikException("Gebruikersnaam reeds in gebruik.");
        }catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Speler geefSpeler(String gebruikersnaam) {
        Speler speler = null;
        try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
             PreparedStatement query = conn.prepareStatement(GEEF_SPELER)) {
            // TODO: Query variabelen invullen en uitvoeren, speler object aanmaken (Tip: kijk in het voorbeeldproject dobbelsteen)
            query.setString(1, gebruikersnaam);
            try (ResultSet rs = query.executeQuery()){
                if(rs.next()){
                    String naam = rs.getString("gebruikersnaam");
                    int geboortejaar = rs.getInt("geboortejaar");
                    int aantalGespeeld = rs.getInt("aantalGespeeld");  // nieuw
                    int aantalGewonnen = rs.getInt("aantalGewonnen");   // nieuw
                    speler = new Speler(naam, geboortejaar, aantalGespeeld, aantalGewonnen);
                    return speler;
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return null; // TODO: gevonden Speler of null retourneren
    }

    public void updateStats(Speler speler) {
        try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
             PreparedStatement query = conn.prepareStatement(UPDATE_STATS)) {
            query.setInt(1, speler.getAantalGespeeld());
            query.setInt(2, speler.getAantalGewonnen());
            query.setString(3, speler.getGebruikersnaam());
            query.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Speler> geefAlleSpelers() {
        List<Speler> spelers = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
             PreparedStatement query = conn.prepareStatement(GEEF_SPELERS);
             ResultSet rs = query.executeQuery()) {

            while (rs.next()) {
                String naam = rs.getString("gebruikersnaam");
                int geboortejaar = rs.getInt("geboortejaar");
                int aantalGespeeld = rs.getInt("aantalGespeeld");   // nieuw
                int aantalGewonnen = rs.getInt("aantalGewonnen");   // nieuw

                spelers.add(new Speler(naam, geboortejaar, aantalGespeeld, aantalGewonnen));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return spelers;
    }

    public Speler geefSpelerDummy(String gebruikersnaam) {
        return new Speler(gebruikersnaam, 1994);
    }
}
