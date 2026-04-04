package domein;

import dtos.SpelDTO;
import utils.Kleur;

import java.util.ArrayList;
import java.util.List;

public class Spel {
    List<Speler> spelers;
    List<Dobbelsteen> dobbelstenen;
    private Dobbelsteen dobbelsteen;
    private ScoreBlad scoreBlad;
    private int aantalDoorkruisteRijen = 0;

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

    public int getAantalDoorkruisteRijen() {
        return aantalDoorkruisteRijen;
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

    public List<Dobbelsteen> toonDobbelstenen(int waarde, Kleur kleur) {
        if (kleur == null) {
            throw new NullPointerException("Kleur mag niet null zijn");
        }
        return new ArrayList<>(dobbelstenen); // kopie van dobbelstenen
    }

    public int getWitteWaarde(int getal) {
        int teller = 1;
        for (Dobbelsteen d : dobbelstenen) {
            if (d.getKleur() == Kleur.WIT) {
                if (teller == getal) {
                    return d.getWaarde();
                }
                teller++;
            }
        }
        return 0;
    }

    public int getGekleurdeWaarde(Kleur kleur) {
        for (Dobbelsteen d : dobbelstenen) {
            if (d.getKleur() == kleur) {
                return d.getWaarde();
            }
        }
        return 0;
    }

    public void rolDobbelstenen() {
        for (Dobbelsteen dobbelsteen : dobbelstenen) {
            dobbelsteen.rolDobbelsteen();
        }
    }

    public List<Speler> geefSpelers(){
        return this.spelers;
    }

    public List<Integer> berekenCombinaties(Kleur kleur) {
        List<Integer> combinaties = new ArrayList<>();
        int gekleurdeWaarde = getGekleurdeWaarde(kleur);

        for (Dobbelsteen d : dobbelstenen) {
            if (d.getKleur() == Kleur.WIT) {
                combinaties.add(gekleurdeWaarde + d.getWaarde());
            }
        }
        return combinaties;
    }


}
