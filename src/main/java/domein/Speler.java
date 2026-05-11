package domein;

import utils.Kleur;

import java.time.LocalDate;

public class Speler implements Comparable<Speler> {
  private final String gebruikersnaam;
  private final int geboortejaar;
  private ScoreBlad scoreBlad;
  private int mislukteWorpen;
  private int aantalGespeeld;
  private int aantalGewonnen;
  private static final int MIN_LEEFTIJD = 8;
  private static final int MAX_LEEFTIJD = 100;
  private static final int MIN_LENGTE_GEBRUIKERSNAAM = 5;

  /**
   * Maakt een nieuwe speler aan met gebruikersnaam en geboortejaar.
   * @param gebruikersnaam de unieke naam van de speler
   * @param geboortejaar het geboortejaar van de speler
   */
  public Speler(String gebruikersnaam, int geboortejaar) {
    controleerGebruikersnaam(gebruikersnaam);
    controleerGeboortejaar(geboortejaar);
    this.gebruikersnaam = gebruikersnaam;
    this.geboortejaar = geboortejaar;
  }


  /**
   * Maakt een nieuwe speler aan met de opgegeven statistieken.
   *
   * @param gebruikersnaam de unieke naam van de speler
   * @param geboortejaar het geboortejaar van de speler
   * @param aantalGespeeld het totaal aantal gespeelde spellen
   * @param aantalGewonnen het totaal aantal gewonnen spellen
   */
  public Speler(String gebruikersnaam, int geboortejaar, int aantalGespeeld, int aantalGewonnen) {
    this(gebruikersnaam, geboortejaar);
    this.aantalGespeeld = aantalGespeeld;
    this.aantalGewonnen = aantalGewonnen;
  }

  /**
   * Geeft het scoreblad van de speler terug.
   * @return het scoreblad
   */
  public ScoreBlad getScoreBlad() { return scoreBlad; }

  /**
   * Geeft de gebruikersnaam van de speler terug.
   * @return de gebruikersnaam
   */
  public String getGebruikersnaam() { return gebruikersnaam; }

  /**
   * Geeft het geboortejaar van de speler terug.
   * @return het geboortejaar
   */
  public int getGeboortejaar() { return geboortejaar; }

  /**
   * Geeft het aantal mislukte worpen van de speler terug.
   * @return aantal mislukte worpen
   */
  public int getMislukteWorpen() { return mislukteWorpen; }

  /**
   * Geeft het totaal aantal gespeelde spellen terug.
   * @return het aantal gewonnen spellen
   */
  public int getAantalGespeeld() { return aantalGespeeld; }

  /**
   * Geeft het totaal aantal gewonnen spellen terug.
   * @return het aantal gewonnen spellen
   */
  public int getAantalGewonnen() { return aantalGewonnen; }


  /**
   * Verhoogt het aantal mislukte worpen met 1.
   */
  public void verhoogMislukteWorpen() {
    this.mislukteWorpen++;
  }

  /**
   * Reset de status van de speler voor een nieuw spel. Zowel de mislukte worpen als de score worden
   * op nul gezet en er wordt een nieuw scoreblad aangemaakt.
   *
   * @param alternatiefModus true als het scoreblad de alternatieve regels
   *                         moet gebruiken, anders false
   */
  public void resetSpeler(boolean alternatiefModus) {
    this.mislukteWorpen = 0;
    maakNieuwScoreBlad(alternatiefModus);
  }


  /**
   * Verhoogt de teller van het aantal gespeelde spellen met 1.
   */
  public void verhoogAantalGespeeld() { this.aantalGespeeld++; }

  /**
   * Verhoogt de teller van het aantal gewonnen spellen met 1.
   */
  public void verhoogAantalGewonnen() { this.aantalGewonnen++; }

  /**
   * Berekent de huidige leeftijd van de speler op basis van het geboortejaar.
   * @return de leeftijd in jaren
   */
  public int berekenLeeftijd() {
    int huidigJaar = LocalDate.now().getYear();
    return huidigJaar - this.geboortejaar;
  }

  /**
   * Controleert of de gebruikersnaam voldoet aan de eisen: niet leeg en
   * minimaal 5 karakters lang.
   *
   * @param gebruikersnaam de gebruikersnaam van de speler
   * @throws IllegalArgumentException als de naam leeg is of te kort is
   */
  private void controleerGebruikersnaam(String gebruikersnaam)
  {
    if (gebruikersnaam == null || gebruikersnaam.isBlank())
      throw new IllegalArgumentException("Gebruikersnaam moet ingevuld zijn!");
    else if (gebruikersnaam.length() < MIN_LENGTE_GEBRUIKERSNAAM)
    {
      throw new IllegalArgumentException("Gebruikersnaam moet minstens 5 karakters hebben!");
    }
  }

  /**
   * Controleert of de speler een geldige leeftijd heeft (tussen 8 en 100 jaar).
   *
   * @param geboortejaar het geboortejaar van de speler
   * @throws IllegalArgumentException als de speler te jong of te oud is (niet tussen 8-100 jaar ligt)
   */
  private void controleerGeboortejaar(int geboortejaar)
  {
    int huidigJaar = LocalDate.now().getYear();
    int leeftijd = huidigJaar - geboortejaar;
    if (leeftijd < MIN_LEEFTIJD || leeftijd > MAX_LEEFTIJD)
      throw new IllegalArgumentException(String.format("Alleen mensen tussen de %d en %d jaar mogen dit spel spelen!", MIN_LEEFTIJD, MAX_LEEFTIJD));
  }

  /**
   * Geeft de totale score van de speler terug op basis van het scoreblad.
   * @return de score van de speler. Of 0 als er nog geen scoreblad is
   */
  public int geefTotaleScore(){
    if (scoreBlad == null) return 0;
    return scoreBlad.berekenTotaleScore(getMislukteWorpen());
  }

  /**
   * Kruist een vakje aan op het scoreblad van de speler.
   * @param kleur de kleur van de rij
   * @param vakje de numerieke waarde van het vakje
   * @return true als het gelukt anders false
   */
  public boolean kruisAan(Kleur kleur, int vakje){
    if (scoreBlad == null) return false;
    return scoreBlad.kruisVakje(kleur, vakje);
  }


  //Doordat we de interface Comparable implementeren moeten we de compareTo methode overriden
  //Dit doen we om later makkelijk de spelers te kunnen sorteren obv de gewonnen score en zo de winnaar makkelijk aan te duiden

  /**
   * Vergelijkt deze speler met een andere speler voor sortering.
   * Volgorde: jongste eerst, daarna langste gebruikersnaam, daarna alfabetisch.
   * @param andereSpeler de andere speler waarmee vergeleken wordt
   * @return negatief als deze speler voor de andere komt, positief als erna, 0 als gelijk
   */
  @Override
  public int compareTo(Speler andereSpeler) {
    // sortering obv geboortejaar (jongste start als eerste)
    if (this.geboortejaar != andereSpeler.geboortejaar) {
      return Integer.compare(andereSpeler.geboortejaar, this.geboortejaar);
    }
    // sortering obv lengte gebruikersnaam (langste gebruikersnaam gaat eerst)
    if (this.gebruikersnaam.length() != andereSpeler.gebruikersnaam.length()) {
      return Integer.compare(andereSpeler.gebruikersnaam.length(), this.gebruikersnaam.length());
    }
    // sortering obv alfabetische volgorde (eerst in alfabet gaat eerst)
    return this.gebruikersnaam.compareToIgnoreCase(andereSpeler.gebruikersnaam);
  }

  /**
   * Maakt een nieuw scoreblad aan voor huidige speler.
   * @param alternatiefModus true voor de alternatieve spelregels anders false
   */
  public void maakNieuwScoreBlad(boolean alternatiefModus) {
    this.scoreBlad = new ScoreBlad(gebruikersnaam, alternatiefModus);
  }
}
