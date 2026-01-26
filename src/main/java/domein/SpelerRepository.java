package domein;

import exceptions.GebruikersnaamInGebruikException;
import persistentie.SpelerMapper;

public class SpelerRepository {
  private final SpelerMapper mapper;

  public SpelerRepository() {
    mapper = new SpelerMapper();
  }

  public void voegToe(Speler speler) {
    if (bestaatSpeler(speler.getGebruikersnaam())){
      throw new GebruikersnaamInGebruikException();
    }

    mapper.voegToe(speler);
  }

  private boolean bestaatSpeler(String gebruikersnaam){
    return mapper.geefSpelerDummy(gebruikersnaam) != null;
  }
}
