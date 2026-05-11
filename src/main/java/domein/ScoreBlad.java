package domein;

import utils.Kleur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreBlad {
    public static final int STRAFPUNTEN_PER_MISLUKTE_WORP = 5;
    public static final int AANTAL_RIJEN = 4;
    public static final int AANTAL_VAKJES_PER_RIJ = 12;
    private final String gebruikersnaam;
    private final boolean alternatiefModus;
    private final List<Rij> rijen;
    private static final int[][] STANDAARD_MATRIX = {
            {2,3,4,5,6,7,8,9,10,11,12,0},
            {2,3,4,5,6,7,8,9,10,11,12,0},
            {12,11,10,9,8,7,6,5,4,3,2,0},
            {12,11,10,9,8,7,6,5,4,3,2,0}
    };
    private static final int[][] ALTERNATIEVE_MATRIX = {
            {10,6,2,8,3,4,12,5,9,7,11,0},
            {9,12,4,6,7,2,5,8,11,3,10,0},
            {8,2,10,12,6,9,7,4,5,11,3,0},
            {5,7,11,9,12,3,8,10,2,6,4,0}
    };

    /**
     * Maakt een nieuw scoreblad aan voor gekozen gebruiker.
     * Maakt 4 rijen aan ROOD, GEEL, GROEN en BLAUW
     * @param gebruikersnaam de naam van de gekozen speler die bij dit scoreblad hoort
     * @param alternatiefModus true als de alternatieve versie is anders false
     */
    public ScoreBlad(String gebruikersnaam, boolean alternatiefModus) {
        this.gebruikersnaam = gebruikersnaam;
        this.alternatiefModus = alternatiefModus;

        int[][] matrix = alternatiefModus
                ? ALTERNATIEVE_MATRIX
                : STANDAARD_MATRIX;

        this.rijen = new ArrayList<>();
        this.rijen.add(new Rij(Kleur.ROOD, matrix[0]));
        this.rijen.add(new Rij(Kleur.GEEL, matrix[1]));
        this.rijen.add(new Rij(Kleur.GROEN, matrix[2]));
        this.rijen.add(new Rij(Kleur.BLAUW, matrix[3]));
    }

    /**
     * Geeft lijst van de 4 rijen van scoreblad terug.
     * @return een onwijzigbare lijst van rijen
     */
    public List<Rij> getRijen() {
        return Collections.unmodifiableList(rijen);
    }

    /**
     * Geeft de gebruikersnaam terug die aan dit scoreblad gekoppeld is.
     * @return de gebruikersnaam
     */
    public String getGebruikersnaam() {
        return gebruikersnaam;
    }

    /**
     * Kruist gekozen vakje in rij door gekozen kleur.
     * Controleert of het oplopende vakjes zijn door de kleur.
     * (oplopend voor rood/geel en aflopend voor groen/blauw)
     * @param kleur de kleur van rij waarin het vakje moet aangekruist worden
     * @param vakje het getal van vakje dat aangekruist moet worden
     * @return true, false Als het aankruisen van vakje gelukt is
     */
    public boolean kruisVakje(Kleur kleur, int vakje){
        for (Rij rij : rijen) {
            if (rij.getKleur() == kleur) {
                return rij.registreerKruis(vakje);
            }
        }
        return false;
    }

    /**
     * Geeft de status van het scoreblad als 2D-array terug.
     * Elke cel is 1 als het vakje aangekruist is, anders 0.
     * @return een 4x12 matrix met de aangekruiste status per vakje
     */
    public int[][] getScoreBladStatus() {
        int[][] scoreBladStatus = new int[4][12]; // 4 rijen, 12 kolommen
        for (int r = 0; r < rijen.size(); r++) {
            List<Vakje> vakjes = rijen.get(r).getVakjes();
            for (int v = 0; v < vakjes.size(); v++) {
                scoreBladStatus[r][v] = vakjes.get(v).isAangekruist() ? 1 : 0;
            }
        }
        return scoreBladStatus;
    }

    /**
     * Berekent de totale score van het scoreblad door de scores per rij op te tellen
     * en daarna de strafpunten door het aantal mislukte worpen ervan af te trekken.
     * @param aantalMislukteWorpen het aantal mislukte worpen van de speler
     * @return de berekende totale eindscore
     */
    public int berekenTotaleScore(int aantalMislukteWorpen){
        int totScore = 0;
        for (Rij rij:rijen){
            totScore += rij.berekenScoreRij();
        }
        totScore -= aantalMislukteWorpen*STRAFPUNTEN_PER_MISLUKTE_WORP;
        return totScore;
    }

    /**
     * Zoekt de rij op die bij de opgegeven kleur hoort.
     * @param kleur de kleur van de gezochte rij
     * @return de bijhorende rij, of null als die niet gevonden wordt
     */
    private Rij isRijVan(Kleur kleur) {
        for (Rij rij : rijen) {
            if (rij.getKleur() == kleur) return rij;
        }
        return null;
    }

    /**
     * Controleert of de rij van de opgegeven kleur gesloten is.
     * @param kleur de kleur van de te controleren rij
     * @return true als de rij bestaat en gesloten is anders false
     */
    public boolean isRijGesloten(Kleur kleur) {
        return isRijVan(kleur) != null && isRijVan(kleur).isGesloten();
    }

    /**
     * Controleert of de rij van de opgegeven kleur op dit moment mag worden gesloten
     * @param kleur de kleur van de betreffende rij
     * @return true als de rij mag sluiten anders false
     */
    public boolean rijMagGeslotenWorden(Kleur kleur) {
        Rij r = isRijVan(kleur);
        return r != null && r.magSluiten();
    }

    /**
     * Geeft het aantal aangekruiste vakjes terug voor de rij van de opgegeven kleur.
     * @param kleur de kleur van de rij
     * @return het aantal kruisjes in de rij, of 0 als de rij niet bestaat
     */
    public int getAantalKruisjes(Kleur kleur) {
        Rij r = isRijVan(kleur);
        return r == null ? 0 : r.getAantalKruisjes();
    }

    /**
     * Geeft de waarden van alle vakjes terug als 2D-array.
     * @return een 4x12 matrix met de waarde van elk vakje
     */
    public int[][] getScorebladWaarden() {
        int[][] scorebladWaarden = new int[AANTAL_RIJEN][AANTAL_VAKJES_PER_RIJ];
        for (int r = 0; r < rijen.size(); r++) {
            List<Vakje> vakjes = rijen.get(r).getVakjes();
            for (int v = 0; v < vakjes.size(); v++) {
                scorebladWaarden[r][v] = vakjes.get(v).getWaarde();
            }
        }
        return scorebladWaarden;
    }




}
