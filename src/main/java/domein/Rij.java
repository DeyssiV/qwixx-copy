package domein;

import utils.Kleur;

import java.util.List;

public class Rij {
    private Kleur kleur;
    private boolean gesloten;
    //private ScoreBlad scoreBlad;
    private List<Vakje> vakjes;
    //private Vakje[] vakjes = new Vakje[11];


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
}
