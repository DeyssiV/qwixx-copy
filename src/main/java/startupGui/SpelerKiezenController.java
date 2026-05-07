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
    private ListView<String> lijstSpelers, lijstGekozenSpelers;

    @FXML
    private Button btnAnnuleren, btnStartSpel;

    @FXML
    private Label lblFeedback;

    @FXML
    private RadioButton lblVersieAlternatiefSKP;

    @FXML
    private void initialize() {
        lijstSpelers.getItems().addAll(dc.geefGeregistreerdeSpelers());
    }

    @FXML
    void voegSpelerToe(ActionEvent event) {
        String gekozenSpeler = lijstSpelers.getSelectionModel().getSelectedItem();
        if (gekozenSpeler != null) {
            dc.voegSpelerToeAanSpel(gekozenSpeler);
            lijstGekozenSpelers.getItems().add(gekozenSpeler);
            lijstSpelers.getItems().remove(gekozenSpeler);
            lijstSpelers.getSelectionModel().clearSelection();

            lblFeedback.setText("");
        }
    }

    @FXML
    void verwijderSpeler(ActionEvent event) {
        String geselecteerde = lijstGekozenSpelers.getSelectionModel().getSelectedItem();
        if (geselecteerde != null) {

            lijstGekozenSpelers.getItems().remove(geselecteerde);


            dc.verwijderSpelerUitSpel(geselecteerde);


            if (!lijstSpelers.getItems().contains(geselecteerde)) {
                lijstSpelers.getItems().add(geselecteerde);

            }
        }
    }

    @FXML
    void annuleer(ActionEvent event) throws Exception {
        dc.resetGekozenSpelers();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Stage stage = (Stage) btnAnnuleren.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

    @FXML
    void startSpel(ActionEvent event) throws Exception {
        int aantalSpelers = lijstGekozenSpelers.getItems().size();

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

        boolean alternatief = lblVersieAlternatiefSKP.isSelected();
        dc.startNieuwSpel();
        dc.setAlternatiefModus(alternatief);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/GekozenSpelersPagina.fxml"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localisatie", Locale.getDefault());
        loader.setResources(bundle);

        Stage stage = (Stage) btnStartSpel.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }
}