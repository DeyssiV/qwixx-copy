package dtos;

import domein.Dobbelsteen;
import domein.ScoreBlad;
import domein.Speler;

import java.util.List;

public record SpelDTO(List<Speler> spelers, List<Dobbelsteen> dobbelstenen, ScoreBlad scoreBlad)
{

}
