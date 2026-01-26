package domein;

public class Speler {
  private final String gebruikersnaam;
  private final int geboortejaar;

  public Speler(String gebruikersnaam, int geboortejaar) {
        // TODO: controleermethodes aanroepen
        this.gebruikersnaam = gebruikersnaam;
        this.geboortejaar = geboortejaar;
  }

  public String getGebruikersnaam() {
    return gebruikersnaam;
  }
  public int getGeboortejaar() {
    return geboortejaar;
  }

  // TODO: controleermethodes toevoegen
}
