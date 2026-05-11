package dtos;

import domein.Dobbelsteen;

import java.util.List;

public record SpelDTO(String spelerAanDeBeurt,
                      int totaleScore,
                      int aantalMislukteWorpen,
                      List<Dobbelsteen> dobbelstenen,
                      int aantalDoorkruisteRijen,
                      boolean alternatiefModus)
{

}
