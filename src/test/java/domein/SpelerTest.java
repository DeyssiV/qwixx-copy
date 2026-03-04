package domein;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
class SpelerTest {
    private int huidigJaar;
    private static final String EEN_CORRECTE_NAAM = "Speler";
    private static final int MIN_LEEFTIJD = 8;
    private static final int MAX_LEEFTIJD = 100;

    @BeforeEach
    void setUp() {
        huidigJaar = LocalDate.now().getYear();
    }

    //controle of de constructor werkt bij volledige geldige gegevens
    @Test
    void maakSpeler_GeldigeGegevens_MaaktSpelerAan() {
        //Stap 1: Arrange
        int geldigGeboortejaar = huidigJaar - 10; // -> deze geboortejaar is altijd geldig omdat het binnen de grenzen valt
        // Stap 2: Act
        Speler s = new Speler(EEN_CORRECTE_NAAM, geldigGeboortejaar);
        //Stap 3: Assert
        assertEquals(EEN_CORRECTE_NAAM, s.getGebruikersnaam());
        assertEquals(geldigGeboortejaar, s.getGeboortejaar());
    }

    //test om de gebruikersnaam te controleren (GELDIG)
    @ParameterizedTest
    @ValueSource(strings = {"abcde", "abcdefhg"}) //-> geldige gebruikersnaam = net de grens (5), boven de grens (8)
    void maakSpeler_GeldigeNaam_MaaktSpelerAan(String naam) {
        int geldigGeboortejaar = huidigJaar - 10;
        Speler s = new Speler(naam, geldigGeboortejaar);
        assertEquals(naam, s.getGebruikersnaam());
    }

    //test om de gebruikersnaam te controleren (ONGELDIG)
    @ParameterizedTest
    @NullAndEmptySource //-> levert de notatie van lege string "" en null automatisch mee
    @ValueSource(strings = {"   ", "    ", "abcd", "ab"}) //-> ongeldige gebruikersnaam = spaties, tabs, net minder dan ondergrens (4), minder dan ondergrens(2)
    void maakSpeler_NaamOngeldigOfTeKort_WerptException(String naam) {
        int geldigGeboortejaar = huidigJaar - 10;
        assertThrows(IllegalArgumentException.class, () -> new Speler(naam, geldigGeboortejaar));
    }

    //test om de geboortejaar te controleren (GELDIG)
    @ParameterizedTest
    @ValueSource(ints = {MIN_LEEFTIJD, 50, MAX_LEEFTIJD}) //-> binnen de gegeven grenzen
    void maakSpeler_GeldigeLeeftijd_MaaktSpelerAan(int leeftijd) {
        int geboortejaar = huidigJaar - leeftijd; //-> om geboortejaar te verkrijgen vanuit de leeftijd, maak je de verschil met de huidige jaar
        Speler s = new Speler(EEN_CORRECTE_NAAM, geboortejaar);
        assertEquals(geboortejaar, s.getGeboortejaar());
    }

    //test om de geboortejaar te controleren (ONGELDIG)
    @ParameterizedTest
    @ValueSource(ints = {MIN_LEEFTIJD - 1, MAX_LEEFTIJD + 1}) //-> buiten de gegeven grenzen
    void maakSpeler_LeeftijdOngeldig_WerptException(int leeftijd) {
        int geboortejaar = huidigJaar - leeftijd;
        assertThrows(IllegalArgumentException.class, () -> new Speler(EEN_CORRECTE_NAAM, geboortejaar));
    }

}