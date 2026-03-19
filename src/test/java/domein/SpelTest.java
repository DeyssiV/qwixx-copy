package domein;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SpelTest {

    private Spel spel;

    @BeforeEach
    void setUp() {
        spel = new Spel(new ArrayList<>());
    }

    @Test
    void constructor_BevatJuisteKleuren() {
        long aantalWit = spel.dobbelstenen.stream().filter(d -> d.getKleur() == Kleur.WIT).count();
        long aantalRood = spel.dobbelstenen.stream().filter(d -> d.getKleur() == Kleur.ROOD).count();
        long aantalGeel = spel.dobbelstenen.stream().filter(d -> d.getKleur() == Kleur.GEEL).count();
        long aantalGroen = spel.dobbelstenen.stream().filter(d -> d.getKleur() == Kleur.GROEN).count();
        long aantalBlauw = spel.dobbelstenen.stream().filter(d -> d.getKleur() == Kleur.BLAUW).count();

        assertEquals(2, aantalWit);
        assertEquals(1, aantalRood);
        assertEquals(1, aantalGeel);
        assertEquals(1, aantalGroen);
        assertEquals(1, aantalBlauw);
    }

    @Test
    void constructor_Maakt6Dobbelstenen() {
        assertEquals(6, spel.dobbelstenen.size());
    }

    @Test
    void toonDobbelstenen_GooitExceptionOmdatDobbelsteenNullIs() {
        assertThrows(NullPointerException.class, () -> {
            spel.toonDobbelstenen(0, null);
        });
    }

    @Test
    void toonSomWaardeWitteDobbelstenenEnScoreblad_NogNietGeimplementeerd() {
    }

    @Test
    void toonAlleMogelijkeCombinaties_CrashtNiet() {
        assertDoesNotThrow(() -> spel.toonAlleMogelijkeCombinaties());
    }

    @Test
    void geefWaardeDobbelsteen_GooitExceptionOmdatNull() {
        assertThrows(NullPointerException.class, () -> {
            spel.geefWaardeDobbelsteen();
        });
    }
}