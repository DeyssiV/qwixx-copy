package domein;

public class Speler {
  private final String gebruikersnaam;
  private final int geboortejaar;

  public Speler(String gebruikersnaam, int geboortejaar) {
        //TODO: controleermethodes aanroepen
        controleerGebruikersnaam(gebruikersnaam);
        this.gebruikersnaam = gebruikersnaam;
        this.geboortejaar = geboortejaar;
  }

  public String getGebruikersnaam() {
    return gebruikersnaam;
  }
  public int getGeboortejaar() {
    return geboortejaar;
  }

  //TODO: controleermethodes toevoegen
  private void controleerGebruikersnaam(String gebruikersnaam)
  {
    if (gebruikersnaam == null || gebruikersnaam.isBlank())
        throw new IllegalArgumentException("Gebruikersnaam moet ingevuld zijn!");
    else if (gebruikersnaam.length() < 5)
    {
      throw new IllegalArgumentException("Gebruikersnaam moet minstens 5 karakters hebben!");
    }
  }

}
