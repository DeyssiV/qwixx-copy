package domein;

import utils.Kleur;

import java.util.ArrayList;
import java.util.List;

public class ScoreBlad {
    private Speler speler;
    String gebruikersnaam;
    private int score;
    private List<Rij> rijen;

    /**
     * Maakt een nieuw scoreblad aan voor gekozen gebruiker.
     * Maakt 4 rijen aan ROOD, GEEL, GROEN en BLAUW
     * @param gebruikersnaam De naam van de gekozen speler die bij deze scoreblad hoort
     */
    public ScoreBlad(String gebruikersnaam) {

        this.gebruikersnaam = gebruikersnaam;
        this.score = 0;

        this.rijen = new ArrayList<>();
        this.rijen.add(new Rij(Kleur.ROOD));
        this.rijen.add(new Rij(Kleur.GEEL));
        this.rijen.add(new Rij(Kleur.GROEN));
        this.rijen.add(new Rij(Kleur.BLAUW));
    }

    /**
     * Geeft lijst van al de rijen van scoreblad.
     * @return Een lijst van 4 rijen van Rij.
     */
    public List<Rij> getRijen() {
        return rijen;
    }

    /**
     * Geeft huidige score van scoreblad terug.
     * @return Een score in Integer.
     */
    public int getScore() {
        return score;
    }


    /**
     * Kruist gekozen vakje in rij door gekozen kleur.
     * Controleert of het oplopende vakjes zijn door de kleur.
     * (oplopend voor rood/geel en aflopend voor groen/blauw)
     * @param kleur De kleur van rij waarin het vakje moet aangekruist worden
     * @param vakje Het getal van vakje dat aangekruist moet worden.
     * @return true, false Als het aankruisen van vakje gelukt is, geeft true anders false.
     */
    public boolean kruisVakje(Kleur kleur, int vakje){
        boolean isGeelOfRood = false;
        for (Rij rij : rijen) {
            if (rij.getKleur() == kleur) {
                if (kleur.equals(Kleur.ROOD) || kleur.equals(Kleur.GEEL)){
                    isGeelOfRood = true;
                }
                return rij.registreerKruis(vakje, isGeelOfRood);
            }
        }
        return false;
    }

    /**
     * Maakt een matrix van scoreblad voor de CUI.
     * Elke rij heeft 12 elementen waarbij 1 voor de aangekruist staat en 0 voor open.
     * @return 2D-array Matrix van Integers die de status van scoreblad weergeven, 4 op 12.
     */
    public int[][] getScoreBladMatrix() {
        int[][] matrix = new int[4][12]; // 4 rijen, 12 kolommen
        for (int r = 0; r < rijen.size(); r++) {
            List<Vakje> vakjes = rijen.get(r).getVakjes();
            for (int v = 0; v < vakjes.size(); v++) {
                matrix[r][v] = vakjes.get(v).isAangekruist() ? 1 : 0;
            }
        }
        return matrix;
    }

    /**
     * Berekent de totale score van het scoreblad door de scores per rij op te tellen
     * en daarna de strafpunten door het aantal mislukte worpen ervan af te trekken.
     * @param aantalMislukteWorpen Het aantal keren dat een actieve speler zijn beurt overgeslagen
     *                             (=mislukte worp heeft gedaan)
     * @return eindscore De berekende totale eindscore in Integer
     */
    public int berekenTotaleScore(int aantalMislukteWorpen){
        int totScore = 0;
        for (Rij rij:rijen){
            totScore += rij.berekenScoreRij(rij.getAantalKruisjes());
        }
        totScore -= (aantalMislukteWorpen*5);
        return totScore;
    }

    public boolean isRijGesloten(Kleur kleur) {
        for (Rij rij : rijen) {
            if (rij.getKleur() == kleur) {
                return rij.isGesloten();
            }
        }
        return false;
    }



}
