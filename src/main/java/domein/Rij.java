package domein;

import utils.Kleur;

import java.util.ArrayList;
import java.util.List;

public class Rij {
    private Kleur kleur;
    private boolean gesloten;
    //private ScoreBlad scoreBlad;
    private List<Vakje> vakjes;
    //private Vakje[] vakjes = new Vakje[11];


    public Rij(Kleur kleur) {
        this.kleur = kleur;

        this.vakjes = new ArrayList<>();

        if (kleur == Kleur.ROOD || kleur == Kleur.GEEL) {
            for (int i = 2; i <= 12; i++) vakjes.add(new Vakje(i));
        } else {
            for (int i = 12; i >= 2; i--) vakjes.add(new Vakje(i));
        }

    }

    public Kleur getKleur() {
        return kleur;
    }

    public boolean isGesloten() {
        return gesloten;
    }

    public List<Vakje> getVakjes() {
        return vakjes;
    }

    public void setGesloten(boolean gesloten) {
        this.gesloten = gesloten;
    }

    public boolean registreerKruis(int waardeVak){
        Vakje gekozenVak = null;
        boolean verbodenVakje = false;

        for (Vakje vak:vakjes) {
            if (vak.getWaarde() == waardeVak) {
                gekozenVak = vak;
            }
        }

        if (gekozenVak == null || gekozenVak.isAangekruist() || verbodenVakje ){
            System.out.println("vak niet gevonden");
            return false;
        }
        gekozenVak.setAangekruist(true);
        System.out.println("vak gevonden");
        return true;

    }

    public int berekenScoreRij(int aantal){
        int [] scoreRij = {1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78};
        return scoreRij[aantal-1];
    }
}
