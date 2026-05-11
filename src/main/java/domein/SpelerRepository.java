package domein;

import exceptions.GebruikersnaamInGebruikException;
import exceptions.SpelerNietGevondenException;
import persistentie.SpelerMapper;

import java.util.List;

public class SpelerRepository {
  private final SpelerMapper mapper;
  private final List<Speler> spelers;

  /**
   * Maakt een nieuwe SpelerRepository aan en laadt alle spelers uit de database.
   */
  public SpelerRepository() {
    mapper = new SpelerMapper();
    spelers = mapper.geefAlleSpelers();
  }

  /**
   * Zoekt een speler op basis van gebruikersnaam, hoofdletterongevoelig.
   * @param gebruikersnaam de gebruikersnaam die je wil checken.
   * @return de gevonden speler.
   * @throws SpelerNietGevondenException als geen speler met die gebruikersnaam bestaat.
   */
  public Speler geefSpeler(String gebruikersnaam){
    for (Speler s : spelers){
      if(s.getGebruikersnaam().equalsIgnoreCase(gebruikersnaam)){
        return s;
      }
    }
    throw new SpelerNietGevondenException(gebruikersnaam);  }

  /**
   * Werkt de statistieken van de opgegeven speler bij in de database.
   * @param speler de speler waarvan de statistieken gesynchroniseerd moeten worden.
   */
  public void updateStats(Speler speler) {
    mapper.updateStats(speler);
  }

  /**
   * Herlaadt de interne spelerslijst vanuit de database.
   */
  public void herlaadSpelers() {
    spelers.clear();
    spelers.addAll(mapper.geefAlleSpelers());
  }

  /**
   * Geeft de volledige lijst van spelers terug.
   * @return een lijst van alle spelers.
   */
  public List<Speler> geefSpelersLijst(){
    return spelers;
  }

  /**
   * Voegt een nieuwe speler toe aan de lijst en de database.
   * @param speler de speler die je wil toevoegen.
   * @throws GebruikersnaamInGebruikException als de gebruikersnaam al bezet is
   */
  public void voegToe(Speler speler) {
    if (bestaatSpeler(speler.getGebruikersnaam())) {
      throw new GebruikersnaamInGebruikException();
    }
    mapper.voegToe(speler);
    spelers.add(speler);
  }

  /**
   * Controleert of er een speler bestaat met de opgegeven gebruikersnaam,
   * ongeacht of er hoofdletters of kleine letters worden gebruikt.
   *
   * @param gebruikersnaam de naam van de speler die gecontroleerd moet worden.
   * @return true als de speler in de lijst voorkomt, anders false.
   */
  private boolean bestaatSpeler(String gebruikersnaam) {
    for (Speler speler : spelers) {
      if (gebruikersnaam.equalsIgnoreCase(speler.getGebruikersnaam()))
        return true;
    }
    return false;
  }
}
