package startupGui;

import dtos.SpelerDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;

import static startupGui.QwixxApplication.dc;

public class GameOverController {

    @FXML private Label lblNaamGoud, lblScoreGoud, lblNaamZilver, lblScoreZilver, lblNaamBrons, lblScoreBrons;
    @FXML private VBox vboxOverigeSpelers, brons;
    @FXML private Button btnAnnuleren, btnOpnieuwSpelenGOP;

    @FXML
    public void initialize() {
        List<SpelerDTO> spelers = new ArrayList<>(dc.geefSpelerDTOsInVolgorde());
        spelers.sort(Comparator.comparingInt(SpelerDTO::score).reversed());

        if (!spelers.isEmpty()) {
            lblNaamGoud.setText(spelers.get(0).gebruikersnaam());
            lblScoreGoud.setText(spelers.get(0).score() + " pts");
        }

        if (spelers.size() >= 2) {
            lblNaamZilver.setText(spelers.get(1).gebruikersnaam());
            lblScoreZilver.setText(spelers.get(1).score() + " pts");
        }

        if (spelers.size() >= 3) {
            lblNaamBrons.setText(spelers.get(2).gebruikersnaam());
            lblScoreBrons.setText(spelers.get(2).score() + " pts");
        } else {
            brons.setVisible(false);
        }

        for (int i = 3; i < spelers.size(); i++) {
            SpelerDTO speler = spelers.get(i);
            Label extraSpeler = new Label(String.format("%d. %s: %d pts",
                    i + 1, speler.gebruikersnaam(), speler.score()));
            extraSpeler.getStyleClass().add("extraSpeler");
            vboxOverigeSpelers.getChildren().add(extraSpeler);
        }

        dc.afsluitenSpel();

    }


    @FXML
    void annuleer(ActionEvent event) throws Exception {
        dc.resetSpel();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Stage stage = (Stage) btnAnnuleren.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

    @FXML
    private void opnieuwSpelen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/SpelerKiezen.fxml"));
            loader.setResources(ResourceBundle.getBundle("localisatie", Locale.getDefault()));
            Stage stage = (Stage) btnOpnieuwSpelenGOP.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}