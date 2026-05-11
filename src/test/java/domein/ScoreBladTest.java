package domein;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Kleur;

import static org.junit.jupiter.api.Assertions.*;

class ScoreBladTest {
    private static final String GEBRUIKER = "TestGebruiker";
    private ScoreBlad scorebladStandaard;
    private ScoreBlad scorebladAlternatief;

    @BeforeEach
    void setUp() {
        scorebladStandaard = new ScoreBlad(GEBRUIKER, false);
        scorebladAlternatief = new ScoreBlad(GEBRUIKER, true);

    }

    @Test
    void scoreblad_bewaart_gebruikersnaam() {
        assertEquals(GEBRUIKER, scorebladStandaard.getGebruikersnaam());
    }

    @Test
    void scoreblad_heeft_vier_rijen_in_juiste_kleuren() {
        var rijen = scorebladStandaard.getRijen();
        assertEquals(4, rijen.size());
        assertEquals(Kleur.ROOD,  rijen.get(0).getKleur());
        assertEquals(Kleur.GEEL,  rijen.get(1).getKleur());
        assertEquals(Kleur.GROEN, rijen.get(2).getKleur());
        assertEquals(Kleur.BLAUW, rijen.get(3).getKleur());
    }

    @Test
    void leegScoreblad_TotaleScoreNul() {
        assertEquals(0, scorebladStandaard.berekenTotaleScore(0));
    }

    @Test
    void berekenTotaleScore_MetStrafpunten_TrektStrafpuntenAf() {
        assertEquals(-15, scorebladStandaard.berekenTotaleScore(3));
    }

    @Test
    void kruisVakje_OngeldigGetal_GeeftFalse() {
        assertFalse(scorebladStandaard.kruisVakje(Kleur.ROOD, 99));
    }
    @Test
    void alternatiefScoreblad_GebruiktAndereWaardenDanStandaard() {
        int[][] standaardWaarden = scorebladStandaard.getScorebladWaarden();
        int[][] alternatieveWaarden = scorebladAlternatief.getScorebladWaarden();
        assertNotEquals(standaardWaarden[0][0], alternatieveWaarden[0][0]);
    }
    @Test
    void getScoreBladStatus_LeegScoreblad_AllesNul() {
        int[][] matrix = scorebladStandaard.getScoreBladStatus();
        for (int[] rij : matrix) {
            for (int v : rij) {
                assertEquals(0, v);
            }
        }
    }
}