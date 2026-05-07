package startupGui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static startupGui.QwixxApplication.dc;

public class GameOverController {

    @FXML private Label lblNaamGoud, lblScoreGoud, lblNaamZilver, lblScoreZilver, lblNaamBrons, lblScoreBrons;
    @FXML private VBox vboxOverigeSpelers, podiumZilver, podiumBrons;
    @FXML private Button btnAnnuleren, btnOpnieuwSpelenGOP;

    @FXML
    public void initialize() {
        List<String> spelers = dc.volgordeSpelersInStringLijst();
        List<String[]> dataLijst = new ArrayList<>();

        for (String naam : spelers) {
            int score = dc.geefTotaleScore(naam);
            dataLijst.add(new String[]{naam, String.valueOf(score)});
        }

        dataLijst.sort((a, b) -> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));


        if (!dataLijst.isEmpty()) {
            lblNaamGoud.setText(dataLijst.get(0)[0]);
            lblScoreGoud.setText(dataLijst.get(0)[1] + " pts");
        }

        if (dataLijst.size() >= 2) {
            lblNaamZilver.setText(dataLijst.get(1)[0]);
            lblScoreZilver.setText(dataLijst.get(1)[1] + " pts");
        }

        if (dataLijst.size() >= 3) {
            lblNaamBrons.setText(dataLijst.get(2)[0]);
            lblScoreBrons.setText(dataLijst.get(2)[1] + " pts");
        } else {
            podiumBrons.setVisible(false);
        }

        if (dataLijst.size() > 3) {
            for (int i = 3; i < dataLijst.size(); i++) {
                String[] spelerData = dataLijst.get(i);
                Label extraSpeler = new Label((i + 1) + ". " + spelerData[0] + ": " + spelerData[1] + " pts");
                extraSpeler.setStyle("-fx-font-size: 16px;");
                vboxOverigeSpelers.getChildren().add(extraSpeler);
            }
        }

        List<String> huidigeSpelers = dc.volgordeSpelersInStringLijst();
        dc.verhoogAantalGespeeldVoorSpelers(huidigeSpelers);
        dc.verhoogAantalGewonnenVoorWinnaar();
        dc.slaStatsOpInDB(huidigeSpelers);

    }

    @FXML
    void annuleer(ActionEvent event) throws Exception {
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