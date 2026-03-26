package domein;

import dtos.SpelDTO;
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
        this.spelers = new ArrayList<>(spelers);
        java.util.Collections.sort(this.spelers);
        this.dobbelstenen = new ArrayList<>();

        //de 6 dobbelstenen volgens spelregels QWIXX toevoegen
        dobbelstenen.add(new Dobbelsteen(Kleur.WIT));
        dobbelstenen.add(new Dobbelsteen(Kleur.WIT));
        dobbelstenen.add(new Dobbelsteen(Kleur.ROOD));
        dobbelstenen.add(new Dobbelsteen(Kleur.GEEL));
        dobbelstenen.add(new Dobbelsteen(Kleur.GROEN));
        dobbelstenen.add(new Dobbelsteen(Kleur.BLAUW));
    }

    public List<Speler> getSpelers()
    {
        return spelers;
    }

    public List<Dobbelsteen> getDobbelstenen()
    {
        return dobbelstenen;
    }

    public Dobbelsteen getDobbelsteen() {
        return dobbelsteen;
    }

    public ScoreBlad getScoreBlad()
    {
        return scoreBlad;
    }

    public int geefWaardeDobbelsteen(){
        return dobbelsteen.getWaarde();
    }

    public Kleur geefKleurDobbelsteen(){
        return dobbelsteen.getKleur();
    }

//    public List<Dobbelsteen> toonDobbelstenen(){
//        int waarde = geefWaardeDobbelsteen();
//        Kleur kleur = geefKleurDobbelsteen();
//        for (Dobbelsteen dobbelsteen:dobbelstenen){
//            dobbelstenen.add(dobbelsteen);
//        }
//        return dobbelstenen;
//
//    }



    public List<Dobbelsteen> toonDobbelstenen(int waarde, Kleur kleur) {
        if (kleur == null) {
            throw new NullPointerException("Kleur mag niet null zijn");
        }
        return new ArrayList<>(dobbelstenen); // kopie van dobbelstenen
    }

    public void rolDobbelstenen() {
        for (Dobbelsteen dobbelsteen : dobbelstenen) {
            dobbelsteen.rolDobbelsteen();
        }
    }

    public List<Speler> geefSpelers(){
        return this.spelers;
    }


    public int toonSomWaardeWitteDobbelstenenEnScoreblad(){
        return geefWaardeDobbelsteen() + scoreBlad.getScore();
    }

//    public List<Integer> toonAlleMogelijkeCombinaties(){
//
//
//    }

    //getSomWitteDobbelstenen


    public List<Dobbelsteen> getDobbelstenen() {
        return this.dobbelstenen;
    }
}
