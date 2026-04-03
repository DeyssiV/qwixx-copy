package domein;

import utils.Kleur;

import java.util.ArrayList;
import java.util.List;

public class ScoreBlad {
    private Speler speler;
    String gebruikersnaam;
    private int score;
    private List<Rij> rijen;
    //private static int[] scoreRij = new int[12];

    public ScoreBlad(String gebruikersnaam) {

        this.gebruikersnaam = gebruikersnaam;
        this.score = 0;

        this.rijen = new ArrayList<>();
        this.rijen.add(new Rij(Kleur.ROOD));
        this.rijen.add(new Rij(Kleur.GEEL));
        this.rijen.add(new Rij(Kleur.GROEN));
        this.rijen.add(new Rij(Kleur.BLAUW));
    }

    public List<Rij> getRijen() {
        return rijen;
    }

    public int getScore() {
        return score;
    }


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

    public int berekenTotaleScore(int aantalMislukteWorpen){
        int totScore = 0;
        for (Rij rij:rijen){
            totScore += rij.berekenScoreRij(rij.getAantalKruisjes());
        }
        totScore -= (aantalMislukteWorpen*5);
        return totScore;
    }



}
