package domein;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Kleur;

import static org.junit.jupiter.api.Assertions.*;

class RijTest {

    private static final int[] RIJ_WAARDEN = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0};

    @Test
    void registreerKruis_LinksVanKruisje_GeeftFalse() {
        Rij rij = new Rij(Kleur.ROOD, RIJ_WAARDEN);
        rij.registreerKruis(5);
        assertFalse(rij.registreerKruis(3));
    }

}