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

  //TODO java doc
  public void updateStats(Speler speler) {
    mapper.updateStats(speler);
  }

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

  private boolean bestaatSpeler(String gebruikersnaam) {
    for (Speler speler : spelers) {
      if (gebruikersnaam.equalsIgnoreCase(speler.getGebruikersnaam()))
        return true;
    }
    return false;
  }
}
