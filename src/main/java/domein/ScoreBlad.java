package domein;

import utils.Kleur;

import java.util.ArrayList;
import java.util.List;

public class ScoreBlad {
    private final String gebruikersnaam;
    private int score;
    private int mislukteWorpen;
    private List<Rij> rijen;
    //private Rij[] rijen = new Rij[4]
    private int[] scoreRij = new int[12];

    public ScoreBlad(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
        this.score = 0;
        this.mislukteWorpen = 0;

        this.rijen = new ArrayList<>();
        this.rijen.add(new Rij(Kleur.ROOD));
        this.rijen.add(new Rij(Kleur.GEEL));
        this.rijen.add(new Rij(Kleur.GROEN));
        this.rijen.add(new Rij(Kleur.BLAUW));
    }

    public String getGebruikersnaam() {
        return gebruikersnaam;
    }


    public int getScore() {
        return score;
    }

    public int getMislukteWorpen() {
        return mislukteWorpen;
    }

    public boolean kruisVakje(Kleur kleur, int vakje){
        for (Rij rij : rijen) {
            if (rij.getKleur() == kleur) {
                return rij.registreerKruis(vakje);
            }
        }
        return false;
    }

    public int[][] getScoreBladMatrix() {
        //TODO boolean matrix
        int[][] matrix = new int[4][12]; // 4 rijen, 12 kolommen
        for (int r = 0; r < rijen.size(); r++) {
            List<Vakje> vakjes = rijen.get(r).getVakjes();
            for (int v = 0; v < vakjes.size(); v++) {
                matrix[r][v] = vakjes.get(v).isAangekruist() ? 1 : 0;
            }
        }
        return matrix;
    }

    /*public int berekenScoreRij(Rij rij, int aantal){
        scoreRij = [1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78]; //TODO in static array


    }*/

}
