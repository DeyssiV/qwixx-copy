package startupGui;

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

    @FXML private Label lblNaamGoud, lblScoreGoud;
    @FXML private Label lblNaamZilver, lblScoreZilver;
    @FXML private Label lblNaamBrons, lblScoreBrons;
    @FXML private VBox vboxOverigeSpelers;
    @FXML private VBox podiumZilver, podiumBrons;
    @FXML private Button btnHoofdmenu, btnOpnieuw;

    @FXML
    public void initialize() {
        List<String> spelers = dc.volgordeSpelersInStringLijst();
        List<String[]> dataLijst = new ArrayList<>();

        for (String naam : spelers) {
            int score = dc.geefTotaleScore(naam);
            dataLijst.add(new String[]{naam, String.valueOf(score)});
        }

        dataLijst.sort((a, b) -> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));


        if (dataLijst.size() >= 1) {
            lblNaamGoud.setText(dataLijst.get(0)[0]);
            lblScoreGoud.setText(dataLijst.get(0)[1] + " pts");
        }

        if (dataLijst.size() >= 2) {
            lblNaamZilver.setText(dataLijst.get(1)[0]);
            lblScoreZilver.setText(dataLijst.get(1)[1] + " pts");
        } else {
            podiumZilver.setVisible(false); // Verberg podium als speler er niet is
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
    }

    @FXML
    private void handleHoofdmenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));
            loader.setResources(ResourceBundle.getBundle("localisatie", Locale.getDefault()));
            Stage stage = (Stage) btnHoofdmenu.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpnieuw() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/SpelerKiezen.fxml"));
            loader.setResources(ResourceBundle.getBundle("localisatie", Locale.getDefault()));
            Stage stage = (Stage) btnOpnieuw.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}