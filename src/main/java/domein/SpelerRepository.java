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
