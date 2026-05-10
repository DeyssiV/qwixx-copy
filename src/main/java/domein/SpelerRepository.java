package domein;

import exceptions.GebruikersnaamInGebruikException;
import persistentie.SpelerMapper;

import java.util.ArrayList;
import java.util.List;

public class SpelerRepository {
  private final SpelerMapper mapper;
  private final List<Speler> spelers;

  /**
   * maakt een spelerRepository aan.
   * maakt een SpelerMapper aan.
   * gebruikt SpelerMapper om alle spelers lokaal op te slaan.
   */
  public SpelerRepository() {
    mapper = new SpelerMapper();
    spelers = mapper.geefAlleSpelers();
  }

  /**
   * kijkt of de gebruikersnaam al gebruikt wordt.
   * @param gebruikersnaam de gebruikersnaam die je wil checken.
   * @return een speler of null.
   */
  public Speler geefSpeler(String gebruikersnaam){
    for (Speler s : spelers){
      if(s.getGebruikersnaam().equalsIgnoreCase(gebruikersnaam)){
        return s;
      }
    }
    return null;
    //return mapper.geefSpeler(gebruikersnaam);
  }

  /**
   * Werkt de statistieken van de opgegeven speler bij in de database.
   *
   * @param speler de speler waarvan de statistieken gesynchroniseerd moeten worden.
   */
  public void updateStats(Speler speler) {
    mapper.updateStats(speler);
  }

  /**
   * Herlaadt de interne lijst van spelers door de huidige data te wissen
   * en de meest recente gegevens uit de mapper op te halen.
   */
  public void herlaadSpelers() {
    spelers.clear();
    spelers.addAll(mapper.geefAlleSpelers());
  }

  /**
   * geeft een lijst van spelers.
   * @return een lijst van spelers.
   */
  public List<Speler> geefSpelersLijst(){
    return spelers;
  }

  /**
   * voegt een speler toe lokaal en in de database.
   * @param speler de speler die je wil toevoegen.
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
   * @return {@code true} als de speler in de lijst voorkomt, anders {@code false}.
   */
  private boolean bestaatSpeler(String gebruikersnaam) {
    for (Speler speler : spelers) {
      if (gebruikersnaam.equalsIgnoreCase(speler.getGebruikersnaam()))
        return true;
    }
    return false;
  }
}
