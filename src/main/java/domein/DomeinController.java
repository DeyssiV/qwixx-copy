package domein;

import java.util.List;

public class DomeinController {
  private final SpelerRepository spelerRepository;
  private Speler speler;

  public DomeinController() {
    spelerRepository = new SpelerRepository();
  }

  public void registreerSpeler(String gebruikersnaam, int geboortejaar) {
    Speler nieuweSpeler = new Speler(gebruikersnaam, geboortejaar);
    spelerRepository.voegToe(nieuweSpeler);
  }
}
