package domein;

import utils.Kleur;

import java.util.ArrayList;
import java.util.List;

public class Spel {
    List<Speler> spelers;
    List<Dobbelsteen> dobbelstenen;
    //Dobbelsteen[] dobbelstenen = new Dobbelsteen[6];
    private Dobbelsteen dobbelsteen;
    private ScoreBlad scoreBlad;

    public Spel(List<Speler> spelers) {
        this.spelers = spelers;
        this.dobbelstenen = new ArrayList<>();

        //de 6 dobbelstenen volgens spelregels QWIXX toevoegen
        dobbelstenen.add(new Dobbelsteen(Kleur.WIT));
        dobbelstenen.add(new Dobbelsteen(Kleur.WIT));
        dobbelstenen.add(new Dobbelsteen(Kleur.ROOD));
        dobbelstenen.add(new Dobbelsteen(Kleur.GEEL));
        dobbelstenen.add(new Dobbelsteen(Kleur.GROEN));
        dobbelstenen.add(new Dobbelsteen(Kleur.BLAUW));
    }

    public int geefWaardeDobbelsteen(){
        return dobbelsteen.getWaarde();
    }

    public Kleur geefKleurDobbelsteen(){
        return dobbelsteen.getKleur();
    }

    public List<Dobbelsteen> toonDobbelstenen(){
        int waarde = geefWaardeDobbelsteen();
        Kleur kleur = geefKleurDobbelsteen();
        for (Dobbelsteen dobbelsteen:dobbelstenen){
            dobbelstenen.add(dobbelsteen);
        }
        return dobbelstenen;

    }

    public int toonSomWaardeWitteDobbelstenenEnScoreblad(){
        return geefWaardeDobbelsteen() + scoreBlad.getScore();
    }

    public List<Integer> toonAlleMogelijkeCombinaties(){


    }


}
