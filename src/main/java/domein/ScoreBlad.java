package domein;

import java.util.List;

public class ScoreBlad {
    private final String gebruikersnaam;
    private int score;
    private int mislukteWorpen;
    private List<Rij> rijen;
    //private Rij[] rijen = new Rij[4]

    public ScoreBlad(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
        this.score = 0;
        this.mislukteWorpen = 0;

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

}
