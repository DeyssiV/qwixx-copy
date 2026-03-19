package domein;

import exceptions.GebruikersnaamInGebruikException;
import persistentie.SpelerMapper;

import java.util.ArrayList;
import java.util.List;

public class SpelerRepository {
  private final SpelerMapper mapper;
  private final List<Speler> spelers;

  public SpelerRepository() {
    mapper = new SpelerMapper();
    spelers = mapper.geefAlleSpelers();
  }

  public Speler geefSpeler(String gebruikersnaam){
    return mapper.geefSpeler(gebruikersnaam);
  }

  public List<Speler> geefSpelersLijst(){
    return spelers;
  }

  public void voegToe(Speler speler) {
    if (bestaatSpeler(speler.getGebruikersnaam())) {
      throw new GebruikersnaamInGebruikException();
    }
    mapper.voegToe(speler);
    spelers.add(speler);
  }

  private boolean bestaatSpeler(String gebruikersnaam) {
    for (Speler speler : spelers)
      return gebruikersnaam.equals(speler.getGebruikersnaam());
    return false;
  }
}
