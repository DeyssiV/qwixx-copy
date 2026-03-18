package domein;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreBladTest {
    private ScoreBlad scoreblad;

    @BeforeEach
    void setUp() {
        scoreblad = new ScoreBlad("TestGebruiker");
    }

    @Test
    void scoreblad_bevat_juist_gebruikersnaam() {
        assertEquals("TestGebruiker", scoreblad.getGebruikersnaam());
    }

    @Test
    void scoreblad_start_met_score_nul() {
        assertEquals(0, scoreblad.getScore());
    }

    @Test
    void scoreblad_start_met_mislukteWorpen_nul() {
        assertEquals(0, scoreblad.getMislukteWorpen());
    }



}