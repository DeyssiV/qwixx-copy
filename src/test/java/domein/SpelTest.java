package domein;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Kleur;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpelTest {

    private Spel spel;
    private Speler speler1;
    private Speler speler2;


    @BeforeEach
    void setUp() {
        int huidigJaar = LocalDate.now().getYear();
        speler1 = new Speler("JohnDoe", 2007);
        speler2 = new Speler("JaneDoe", 2007);
        List<Speler> spelers = new ArrayList<>(List.of(speler1, speler2));
        spel = new Spel(spelers, false);
    }

    @Test
    void maakSpel_TeWeinigSpelers_WerptException() {
        List<Speler> teWeinigSpelersLijst = List.of(speler1);
        assertThrows(IllegalArgumentException.class, () -> new Spel(teWeinigSpelersLijst, false));
    }
    @Test
    void maakSPel_TeVeelSpelers_WerptException() {
        List<Speler> teVeelSpelersLijst = new ArrayList<>();
        for (int i = 0; i < Spel.MAX_AANTAL_SPELERS + 1; i++) {
            teVeelSpelersLijst.add(new Speler("Extra" + i, 2007));
        }
        assertThrows(IllegalArgumentException.class, () -> new Spel(teVeelSpelersLijst, false));
    }
    @Test
    void maakSpel_BevatJuisteAantalDobbelstenen_maaktSpelAan() {
        assertEquals(6, spel.getDobbelstenen().size());
    }

    @Test
    void maakSpel_BevatJuisteKleurenEnJuisteAantal_maaktSpelAan() {
        List<Dobbelsteen> dobbelstenen = spel.getDobbelstenen();
        int wit = 0, rood = 0, geel = 0, groen = 0, blauw = 0;
        for (Dobbelsteen d : dobbelstenen) {
            switch (d.getKleur()) {
                case WIT   -> wit++;
                case ROOD  -> rood++;
                case GEEL  -> geel++;
                case GROEN -> groen++;
                case BLAUW -> blauw++;
            }
        }

        assertEquals(2, wit);
        assertEquals(1, rood);
        assertEquals(1, geel);
        assertEquals(1, groen);
        assertEquals(1, blauw);
    }


    @Test
    void verwijderDobbelsteenVanKleur_BestaandeKleur_GeeftTrueEnVerwijdert() {
        assertTrue(spel.verwijderDobbelsteenVanKleur(Kleur.ROOD));
        long rood = spel.getDobbelstenen().stream().filter(d -> d.getKleur() == Kleur.ROOD).count();
        assertEquals(0, rood);
        assertEquals(5, spel.getDobbelstenen().size());
    }

}