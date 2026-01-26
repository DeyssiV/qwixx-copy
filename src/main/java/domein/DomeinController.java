package domein;

public class DomeinController {
  private final SpelerRepository spelerRepository;

  public DomeinController() {
    spelerRepository = new SpelerRepository();
  }

  public void registreerSpeler(String gebruikersnaam, int geboortejaar) {
    Speler nieuweSpeler = new Speler(gebruikersnaam, geboortejaar);
    spelerRepository.voegToe(nieuweSpeler);
  }
}
