package domein;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class DobbelsteenTest
{
    @ParameterizedTest
    @ValueSource(strings = {"wit", "rood", "blauw", "geel", "groen"})
    void maakDobbelsteen_GeldigeKleur_MaaktDobbelsteen(String kleur) {
        Dobbelsteen d = new Dobbelsteen(kleur);
        assertEquals(kleur, d.getKleur());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "    ", "bruin", "zwart", "a"})
    void maakDobbelsteen_OngeldigeKleur_WerptException(String kleur) {
        assertThrows(IllegalArgumentException.class, () -> new Dobbelsteen(kleur));
    }
}