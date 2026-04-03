package startupGui;

import domein.DomeinController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;


public class SpelerKiezenController {

    private final DomeinController dc = QwixxApplication.dc;

    @FXML
    private ListView<String> lvLijstSpelers;

    @FXML
    private ListView<String> lvGekozenSpelers;

    @FXML
    private Button btnAnnuleer;

    @FXML
    private Label lblFeedback;

    @FXML
    private Button btnStartSpel;

    @FXML
    private RadioButton rbNormaal;

    @FXML
    private RadioButton rbAlternatief;

    @FXML
    private void initialize() {
        lvLijstSpelers.getItems().addAll(dc.geefGeregistreerdeSpelers());
    }

    @FXML
    void normaalSpel(ActionEvent event) {

    }

    @FXML
    void alternatiefSpel(ActionEvent event) {

    }

    @FXML
    void voegSpelerToe(ActionEvent event) {
        String gekozenSpeler = lvLijstSpelers.getSelectionModel().getSelectedItem();
        if (gekozenSpeler != null) {
            dc.voegSpelerToeAanSpel(gekozenSpeler);
            lvGekozenSpelers.getItems().add(gekozenSpeler);
            lvLijstSpelers.getItems().remove(gekozenSpeler);
            lvLijstSpelers.getSelectionModel().clearSelection();

            lblFeedback.setText("");
        }
    }

    @FXML
    void verwijderSpeler(ActionEvent event) {
        String geselecteerde = lvGekozenSpelers.getSelectionModel().getSelectedItem();
        if (geselecteerde != null) {

            lvGekozenSpelers.getItems().remove(geselecteerde);


            dc.verwijderSpelerUitSpel(geselecteerde);


            if (!lvLijstSpelers.getItems().contains(geselecteerde)) {
                lvLijstSpelers.getItems().add(geselecteerde);

//                // Optional: Sort the ComboBox items alphabetically so the list stays organized
//                javafx.collections.FXCollections.sort(cmbLijstSpelers.getItems());
            }
        }
    }

    @FXML
    void annuleer(ActionEvent event) throws Exception {
        dc.resetGekozenSpelers();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Stage stage = (Stage) btnAnnuleer.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

    @FXML
    void startSpel(ActionEvent event) throws Exception {
        int aantalSpelers = lvGekozenSpelers.getItems().size();

        if (aantalSpelers < 2) {
            ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());
            lblFeedback.setText(bundle.getString("AlertTeWeinigSpelers"));
            lblFeedback.setStyle("-fx-text-fill: red;");
            return;
        }

        if (aantalSpelers > 5) {
            ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());
            lblFeedback.setText(bundle.getString("AlertTeVeelSpelers"));
            lblFeedback.setStyle("-fx-text-fill: red;");
            return;
        }

        lblFeedback.setText("");

        boolean alternatief = rbAlternatief.isSelected();
        dc.startNieuwSpel();
        dc.setAlternatiefModus(alternatief);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/Spel.fxml"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Stage stage = (Stage) btnStartSpel.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }
}