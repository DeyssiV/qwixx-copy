package domein;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class DobbelsteenTest
{
    @ParameterizedTest
    @EnumSource (Kleur.class)
    void maakDobbelsteen_GeldigeKleur_MaaktDobbelsteen(Kleur kleur) {
        Dobbelsteen d = new Dobbelsteen(kleur);
        assertEquals(kleur, d.getKleur());
    }

    @ParameterizedTest
    @NullSource
    void maakDobbelsteen_NullKleur_WerptException(Kleur kleur) {
        assertThrows(IllegalArgumentException.class, () -> new Dobbelsteen(kleur));
    }
}